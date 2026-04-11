package com.metaverse.msme.district_and_mandal.service;

import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.model.Sector;

import java.util.List;

public interface DistrictAndMandalService {
    List<DistrictResponse> getAllDistricts();
    List<MandalResponse> getMandalsByDistrictName(String districtId);
    List<MsmeUnitDetails> getByVillage(String village, String mandal);
    List<VillageResponse> getVillagesByMandalName(String districtName,String mandalName);
    List<Sector> getAllSectors();
}
