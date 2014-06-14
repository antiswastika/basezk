package com.wd.basezk.dao;

import java.util.List;
import java.util.Map;

import com.wd.basezk.model.Crole;

public interface CroleDAO {
    public Boolean insertData(Crole objNya);
    public Boolean updateData(Crole objNya);
    public Boolean deleteData(String idNya);
    public Crole getById(String idNya);
    public List<Crole> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
}
