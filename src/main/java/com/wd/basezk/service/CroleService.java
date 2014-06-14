package com.wd.basezk.service;

import java.util.List;
import java.util.Map;

import com.wd.basezk.model.Crole;

public interface CroleService {
    public void insertData(Crole objNya);
    public void updateData(Crole objNya);
    public void deleteData(String idNya);
    public Crole getById(String idNya);
    public List<Crole> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
}
