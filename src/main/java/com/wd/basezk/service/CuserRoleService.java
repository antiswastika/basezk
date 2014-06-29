package com.wd.basezk.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wd.basezk.model.Crole;
import com.wd.basezk.model.Cuser;
import com.wd.basezk.model.CuserRole;

public interface CuserRoleService {
    public void insertData(Cuser objNya, Set<Crole> objNya2);
    public void updateData(Cuser objNya, Set<Crole> objNya2);
    public void deleteData(String idNya);
    public CuserRole getById(String idNya);
    public List<CuserRole> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
    public String generateIdForModel();
}
