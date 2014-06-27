package com.wd.basezk.service;

import java.util.List;
import java.util.Map;

import com.wd.basezk.model.CuserRole;

public interface CuserRoleService {
    public void insertData(CuserRole objNya);
    public void updateData(CuserRole objNya);
    public void deleteData(String idNya);
    public CuserRole getById(String idNya);
    public List<CuserRole> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
}
