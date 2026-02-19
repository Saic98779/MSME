package com.metaverse.msme.addressfinders.extractor;

import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.repository.MsmeUnitDetailsRepository;
import com.metaverse.msme.addressfinders.service.AddressParseResult;
import com.metaverse.msme.addressfinders.service.AddressParseService;
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
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    private static final List<String> PARSED_COLUMNS = List.of(
            "PARSED_VILLAGE",
            "PARSED_MANDAL",
            "PARSED_DISTRICT",
            "ADDRESS_STATUS",
            "DETAILS"
    );

    /* =========================================================
       EXCEL GENERATION
       ========================================================= */
    public byte[] generateExcel(Integer pageNo,Integer pageSize) {

        ExecutorService pool =
                Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors()));

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("MSME ADDRESS PARSE");
            createHeader(sheet);

            List<MsmeUnitDetails> records = repository.findAll(PageRequest.of(pageNo,pageSize)).getContent();
            int rowIndex = 1;

            List<CompletableFuture<AddressParseResult>> futures =
                    records.stream()
                            .map(r -> CompletableFuture.supplyAsync(
                                    () -> parseSafely(r), pool))
                            .collect(Collectors.toList());

            CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0])
            ).join();

            for (int i = 0; i < records.size(); i++) {

                MsmeUnitDetails u = records.get(i);
                AddressParseResult result = futures.get(i).join();

                Row row = sheet.createRow(rowIndex++);
                int col = 0;

                // ALL MSME COLUMNS
                for (String field : MSME_COLUMNS) {
                    row.createCell(col++).setCellValue(getFieldValue(u, field));
                }

                // PARSED COLUMNS
                row.createCell(col++).setCellValue(value(result.getVillage()));
                row.createCell(col++).setCellValue(value(result.getMandal()));
                row.createCell(col++).setCellValue(u.getDistrict());
                row.createCell(col++).setCellValue(resolveAddressStatus(result));
                row.createCell(col).setCellValue(buildDetails(result));
            }

            setColumnWidths(sheet);
            workbook.write(out);
            workbook.dispose();

            return out.toByteArray();

        } catch (Exception e) {
            log.error("Excel generation failed", e);
            throw new RuntimeException(e);
        } finally {
            pool.shutdown();
        }
    }

    /* =========================================================
       HELPERS
       ========================================================= */

    private AddressParseResult parseSafely(MsmeUnitDetails u) {
        try {
            if (u.getUnitAddress() != null &&
                    !"null".equalsIgnoreCase(u.getUnitAddress())) {
                return addressParseService.parse("mulugu", u.getUnitAddress());
            }
        } catch (Exception e) {
            log.debug("Parse failed for slno={}", u.getMsmeUnitId(), e);
        }
        return AddressParseResult.fromMandalResult(
                MandalDetectionResult.notFound());
    }

    private String getFieldValue(MsmeUnitDetails u, String fieldName) {
        try {
            Field f = MsmeUnitDetails.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            Object v = f.get(u);
            return v == null ? "" : v.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private void createHeader(Sheet sheet) {
        Row header = sheet.createRow(0);
        int col = 0;

        for (String c : MSME_COLUMNS) {
            header.createCell(col++).setCellValue(c.toUpperCase());
        }
        for (String c : PARSED_COLUMNS) {
            header.createCell(col++).setCellValue(c);
        }
    }

    private void setColumnWidths(Sheet sheet) {
        int totalCols = MSME_COLUMNS.size() + PARSED_COLUMNS.size();
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

    private String resolveAddressStatus(AddressParseResult result) {

        // Mandal ambiguity overrides village success
        if (result.getMandalStatus() != null
                && result.getMandalStatus() != MandalDetectionStatus.SINGLE_MANDAL) {

            return result.getMandalStatus().name();
        }

        // Mandal resolved → now village matters
        if (result.getVillageStatus() != null) {
            return result.getVillageStatus().name();
        }

        // Fallback
        return result.getMandalStatus() != null
                ? result.getMandalStatus().name()
                : "NOT_FOUND";
    }

}
