package com.wd.basezk.dao;

import java.util.List;
import java.util.Map;

import com.wd.basezk.model.Cuser;

public interface CuserDAO {
    public Boolean insertData(Cuser objNya);
    public Boolean updateData(Cuser objNya);
    public Boolean deleteData(String idNya);
    public Cuser getById(String idNya);
    public List<Cuser> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
}
