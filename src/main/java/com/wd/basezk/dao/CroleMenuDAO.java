package com.wd.basezk.dao;

import java.util.List;
import java.util.Map;

import com.wd.basezk.model.CroleMenu;

public interface CroleMenuDAO {
    public Boolean insertData(CroleMenu objNya);
    public Boolean deleteData(String idNya);
    public CroleMenu getById(String idNya);
    public List<CroleMenu> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
    public String generateIdForModel();
}
