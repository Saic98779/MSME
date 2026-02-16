package com.metaverse.msme.controller;

import com.metaverse.msme.model.SangareddyMsmeUnitEntity;

public class CsvHelper {

    public static SangareddyMsmeUnitEntity mapRowToEntity(String[] row) {

        SangareddyMsmeUnitEntity user = new SangareddyMsmeUnitEntity();

        user.setSlno(parseInt(row, 0));
        user.setUniqueNo(get(row, 1));
        user.setDepartmentName(get(row, 2));
        user.setMsmeState(get(row, 3));
        user.setDistrict(get(row, 4));
        user.setMsmeSector(get(row, 5));
        user.setUnitName(get(row, 6));
        user.setCategory(get(row, 7));
        user.setUnitAddress(get(row, 8));
        user.setDoorNo(get(row, 9));
        user.setLocality(get(row, 10));
        user.setStreet(get(row, 11));
        user.setVillage(get(row, 12));
        user.setWard(get(row, 13));
        user.setMandal(get(row, 14));
        user.setPinCode(get(row, 16));
        user.setOfficeEmail(get(row, 17));
        user.setOfficeContact(get(row, 18));
//        user.setFemaleEmpsTotal(parseInt(row, 20));
//        user.setMaleEmpsTotal(parseInt(row, 21));
       /* user.setFirmRegYear(parseInt(row, 81));
        user.setExtractedVillage(get(row, 82));
        user.setExtractedMandal(get(row, 83));
        user.setExtractedDistrict(get(row, 84));
        user.setProcessedFlag(get(row, 85));
        user.setMuncipality(get(row, 86));
        user.setUlbname(get(row, 87));
*/
        return user;
    }

    private static String get(String[] row, int index) {
        if (index >= row.length) return null;
        String val = row[index];
        return (val == null || val.isBlank()) ? null : val.trim();
    }

    private static Integer parseInt(String[] row, int index) {
        try {
            String val = get(row, index);
            if (val == null) return null;
            return Integer.parseInt(val.replace(".0", ""));
        } catch (Exception e) {
            return null;
        }
    }
}

