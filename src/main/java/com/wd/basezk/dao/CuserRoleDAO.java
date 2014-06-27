package com.wd.basezk.dao;

import java.util.List;
import java.util.Map;

import com.wd.basezk.model.CuserRole;

public interface CuserRoleDAO {
    public Boolean insertData(CuserRole objNya);
    public Boolean updateData(CuserRole objNya);
    public Boolean deleteData(String idNya);
    public CuserRole getById(String idNya);
    public List<CuserRole> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
}
