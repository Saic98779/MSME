package com.metaverse.msme.controller;

import com.metaverse.msme.model.SangareddyMsmeUnitEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static javax.management.openmbean.SimpleType.STRING;

public class ExcelHelper {

    public static List<SangareddyMsmeUnitEntity> excelToUsers(InputStream is) {
        List<SangareddyMsmeUnitEntity> users = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                SangareddyMsmeUnitEntity user = new SangareddyMsmeUnitEntity();

                user.setSlno(getInteger(row, 0));
                user.setUniqueNo(getString(row, 1));
                user.setDepartmentName(getString(row, 2));
                user.setMsmeState(getString(row, 3));
                user.setDistrict(getString(row, 4));
                user.setMsmeSector(getString(row, 5));
                user.setUnitName(getString(row, 6));
                user.setCategory(getString(row, 7));
                user.setUnitAddress(getString(row, 8));
                user.setDoorNo(getString(row, 9));
                user.setLocality(getString(row, 10));
                user.setStreet(getString(row, 11));
                user.setVillage(getString(row, 12));
                user.setWard(getString(row, 13));
                user.setMandal(getString(row, 14));
                user.setDistrict(getString(row, 15));
                user.setPinCode(getString(row, 16));
                user.setOfficeEmail(getString(row, 17));
                user.setOfficeContact(getString(row, 18));
                user.setPrincipalBusinessPlace(getString(row, 19));
                user.setFemaleEmpsTotal(getInteger(row, 20));
                user.setMaleEmpsTotal(getInteger(row, 21));
                user.setInstitutionDetails(getString(row, 24));
                user.setPurpose(getString(row, 25));
                user.setOrgnType(getString(row, 26));
                user.setEnterpriseType(getString(row, 27));
                user.setNatureOfBusiness(getString(row, 28));
                user.setProductDesc(getString(row, 29));
                user.setRegistrationUnder(getString(row, 30));
                user.setRegistrationNo(getString(row, 31));
                user.setDateOfRegistration(getString(row, 32));
                user.setUdyamRegistrationNo(getString(row, 33));
                user.setNicCode(getString(row, 34));
                user.setIncorporationDate(getString(row, 35));
                user.setCommmenceDate(getString(row, 36));
                user.setUdyamAadharRgistrationNo(getString(row, 37));
                user.setGstRegNo(getString(row, 38));
                user.setDin(getString(row, 38));
                user.setPhotograph(getString(row, 39));
                user.setUnitHolderOrOwnerName(getString(row, 40));
                user.setFirstMiddleLastName(getString(row, 41));
                user.setDesignation(getString(row, 42));
                user.setCaste(getString(row, 43));
                user.setSpecialCategory(getString(row, 44));
                user.setGender(getString(row, 45));
                user.setDateofBirth(getString(row, 45));
                user.setQualification(getString(row, 46));
                user.setNationality(getString(row, 47));
                user.setPan(getString(row, 48));
                user.setAadharNo(getString(row, 49));
                user.setPassportNo(getString(row, 51));
                user.setCommunicationAddress(getString(row, 52));
                user.setCommDoorNo(getString(row, 53));
                user.setCommLocality(getString(row, 54));
                user.setCommStreet(getString(row, 55));
                user.setCommLandmark(getString(row, 56));
                user.setCommNameofthebuilding(getString(row, 57));
                user.setFloorNo(getString(row, 58));
                user.setCommVillage(getString(row, 59));
                user.setCommMandal(getString(row, 60));
                user.setCommDistrict(getString(row, 61));
                user.setCommPINcode(getString(row, 62));
                user.setCommMobileNo(getString(row, 63));
                user.setCommAlternateNo(getString(row, 64));
                user.setEmailAddress(getString(row, 65));
                user.setLtHt(getString(row, 66));
                user.setServiceNo(getString(row, 68));
                user.setCurrentStatus(getString(row, 69));
                user.setTypeofLoan(getString(row, 72));
                user.setSourceofLoan(getString(row, 73));
                user.setLoanAppliedDate(getString(row, 73));
                user.setLoanSanctionDate(getString(row, 74));
                user.setSubsidyApplicationDate(getString(row, 75));
                user.setBankName(getString(row, 76));
                user.setBranchNameAddress(getString(row, 77));
                user.setIfsCcode(getString(row, 78));
                user.setReleaseDateDoc(getString(row, 79));
               /* user.setRemarks(getString(row, 80));
                user.setFirmRegYear(getInteger(row, 81));
                user.setExtractedVillage(getString(row, 82));
                user.setExtractedMandal(getString(row, 83));
                user.setExtractedDistrict(getString(row, 84));
                user.setProcessedFlag(getString(row, 85));
                user.setMuncipality(getString(row, 86));
                user.setUlbname(getString(row, 87));
*/
                System.err.println(user);
                users.add(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file", e);
        }

        return users;
    }


    private static String getString(Row row, int index) {
        if (row == null) return null;

        Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                yield BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> null;
        };
    }

    private static Integer getInteger(Row row, int index) {
        String value = getString(row, index);
        if (value == null || value.isBlank()) return null;

        try {
            return Integer.parseInt(value.replace(".0", ""));
        } catch (Exception e) {
            return null;
        }
    }

}

