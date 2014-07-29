package com.wd.basezk.dao;

import java.util.List;
import java.util.Map;

import com.wd.basezk.model.Cmodule;

public interface CmoduleDAO {
    public Boolean insertData(Cmodule objNya);
    public Boolean updateData(Cmodule objNya);
    public Boolean deleteData(String idNya);
    public Cmodule getById(String idNya);
    public List<Cmodule> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
}
