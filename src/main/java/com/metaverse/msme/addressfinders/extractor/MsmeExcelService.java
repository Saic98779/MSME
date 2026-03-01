package com.metaverse.msme.addressfinders.extractor;

import com.metaverse.msme.addressfinders.service.AddressParseResult;
import com.metaverse.msme.addressfinders.service.AddressParseService;
import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.repository.MsmeUnitDetailsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class MsmeExcelService {

    private static final Logger log = LoggerFactory.getLogger(MsmeExcelService.class);

    private static final int CHUNK_SIZE = 2000;

    private final MsmeUnitDetailsRepository repository;
    private final AddressParseService addressParseService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public MsmeExcelService(
            MsmeUnitDetailsRepository repository,
            AddressParseService addressParseService) {
        this.repository = repository;
        this.addressParseService = addressParseService;
    }

    /* =========================================================
       ALL MSME TABLE COLUMNS (ORDERED)
       ========================================================= */
    private static final List<String> MSME_COLUMNS = List.of(
            "slno","uniqueno","departmentname","msmestate","msmedist","msmesector",
            "unitname","category","unitaddress","doorno","locality","street",
            "village","ward","mandal","district","pincode","officeemail",
            "officecontact","principalbusinessplace","femaleempstotal",
            "maleempstotal","lattitude","longitute","institutiondetails",
            "purpose","orgntype","enterprisetype","natureOfbusiness",
            "productdesc","registrationunder","registrationno",
            "dateofregistration","udyamregistrationNo","niccode",
            "incorporationdate","commmencedate","udyamaadharrgistrationno",
            "gstregno","din","photograph","unitholderorownername",
            "firstmiddlelastName","designation","caste","specialcategory",
            "gender","dateofbirth","qualification","nationality","pan",
            "aadharno","passportno","communicationaddress","commdoorno",
            "commlocality","commstreet","commlandmark",
            "commnameofthebuilding","floorno","commvillage","commmandal",
            "commdistrict","commPINcode","commmobileno","commalternateno",
            "emailaddress","ltHt","loadKva","serviceno","currentstatus",
            "unitcostorinvestment","netturnoverrupees","typeofloan",
            "sourceofloan","loanapplieddate","loansanctiondate",
            "subsidyapplicationdate","bankname","branchnameaddress",
            "ifsccode","releasedatedoc","workingcapital","remarks",
            "firmregyear","extractedvillage","extractedmandal",
            "extracteddistrict","processedflag","muncipality","ulbname",
            "employee","pcbcategory","manufacturingactivity","lawprovision",
            "circlename","managername","riskcategory","age","isdisabled",
            "paidupcapital"
    );



    /* =========================================================
       HELPERS
       ========================================================= */

    private AddressParseResult parseSafely(MsmeUnitDetails u, String districtName) {
        try {
            if (u.getUnitAddress() != null &&
                    !"null".equalsIgnoreCase(u.getUnitAddress())) {
                return addressParseService.parse(districtName, u.getUnitAddress());
            }
        } catch (Exception e) {
            log.debug("Parse failed for slno={}", u.getMsmeUnitId(), e);
        }
        return AddressParseResult.fromMandalResult(
                MandalDetectionResult.notFound());
    }

    private void createHeader(Sheet sheet) {
        Row header = sheet.createRow(0);
        int col = 0;

        for (String c : PARSED_COLUMNS) {
            header.createCell(col++).setCellValue(c);
        }
    }

    private void setColumnWidths(Sheet sheet) {
        int totalCols = PARSED_COLUMNS.size();
        for (int i = 0; i < totalCols; i++) {
            sheet.setColumnWidth(i, 6000);
        }
    }

    private String buildDetails(AddressParseResult r) {
        return "Mandal: " + value(r.getMandal()) + "\n" +
                "Mandal Status: " + value(r.getMandalStatus()) + "\n" +
                "Multiple Mandals: " +
                (r.getMultipleMandals() != null
                        ? String.join(", ", r.getMultipleMandals())
                        : "NOT_FOUND") + "\n" +
                "Village: " + value(r.getVillage()) + "\n" +
                "Village Status: " + value(r.getVillageStatus()) + "\n" +
                "Multiple Villages: " +
                (r.getMultipleVillages() != null
                        ? String.join(", ", r.getMultipleVillages())
                        : "NOT_FOUND");
    }

    private String value(Object o) {
        return o == null ? "" : o.toString();
    }


    public byte[] generateExcelUnitAddress(Integer startAfterSlno, int totalRecords,String districtName) {

        ExecutorService pool = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()));
        try (
                SXSSFWorkbook workbook = new SXSSFWorkbook(1000); // keep a larger window to reduce flush I/O
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("MSME ADDRESS PARSE");
            createHeader(sheet);

            int rowIndex = 1;
            int processed = 0;
            Integer lastId = Objects.requireNonNullElse(startAfterSlno, 0);

            while (processed < totalRecords) {

                // fetch next chunk without OFFSET
                List<MsmeUnitDetails> chunk = repository.findNextChunk(lastId, PageRequest.of(0, CHUNK_SIZE));

                System.err.println(chunk);
                if (chunk == null || chunk.isEmpty()) {
                    break;
                }

                // parse in parallel but preserve order
                List<CompletableFuture<AddressParseResult>> futures = new ArrayList<>(chunk.size());

                for (MsmeUnitDetails u : chunk) {
                    CompletableFuture<AddressParseResult> f = CompletableFuture.supplyAsync(() -> parseSafely(u,districtName), pool);
                    futures.add(f);
                }

                // wait for all to complete
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

                // collect results in same order as chunk
                List<AddressParseResult> results = futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());

                for (int i = 0; i < chunk.size(); i++) {
                    System.out.println("Processed row number: " + rowIndex);
                    MsmeUnitDetails u = chunk.get(i);

                    AddressParseResult result = results.get(i);

                    Row row = sheet.createRow(rowIndex++);

                    row.createCell(0).setCellValue(value(u.getMsmeUnitId()));
                    row.createCell(1).setCellValue(value(u.getDepartmentName()));
                    row.createCell(2).setCellValue(value(u.getUnitAddress()));
                    row.createCell(3).setCellValue(value(u.getVillage()));
                    row.createCell(4).setCellValue(value(result.getVillage()));
                    row.createCell(5).setCellValue(value(result.getMandal()));
                    row.createCell(6).setCellValue(districtName);
                    row.createCell(7).setCellValue(result.getVillageStatus() != null ? result.getVillageStatus().name() :  result.getMandalStatus().name());
                    row.createCell(8).setCellValue(buildDetails(result));

                    lastId = Integer.valueOf(Math.toIntExact(u.getMsmeUnitId()));
                }

                processed += chunk.size();

                log.debug("Processed {} records, lastId={}", processed, lastId);
            }

            setColumnWidths(sheet);

            workbook.write(out);
            workbook.dispose();

            return out.toByteArray();

        } catch (Exception e) {
            log.error("Excel generation failed", e);
            throw new RuntimeException("Excel generation failed", e);
        } finally {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                }
            } catch (InterruptedException ignored) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    private static final List<String> PARSED_COLUMNS = List.of(
            "MSME_UNIT_ID",
            "DEPARTMENT_NAME",
            "UNIT_ADDRESS",
            "VILLAGE",
            "PARSED_VILLAGE",
            "PARSED_MANDAL",
            "PARSED_DISTRICT",
            "ADDRESS_STATUS",
            "DETAILS"
    );

}
