package com.wd.basezk.dao;

import java.util.List;
import java.util.Map;

import com.wd.basezk.model.Cmenu;

public interface CmenuDAO {
    public void insertData(Cmenu objNya);
    public void updateData(Cmenu objNya);
    public void deleteData(String idNya);
    public Cmenu getById(String idNya);
    public List<Cmenu> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
}
