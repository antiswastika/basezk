package com.wd.basezk.service;

import java.util.List;
import java.util.Map;

import com.wd.basezk.model.Cmodule;

public interface CmoduleService {
    public void insertData(Cmodule objNya);
    public void updateData(Cmodule objNya);
    public void deleteData(String idNya);
    public Cmodule getById(String idNya);
    public List<Cmodule> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
}
