package com.wd.basezk.dao;

import java.util.List;
import java.util.Map;

import com.wd.basezk.model.CuserGrp;

public interface CuserGrpDAO {
    public Boolean insertData(CuserGrp objNya);
    public Boolean updateData(CuserGrp objNya);
    public Boolean deleteData(String idNya);
    public CuserGrp getById(String idNya);
    public List<CuserGrp> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
}
