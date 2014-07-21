package com.wd.basezk.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wd.basezk.model.Crole;
import com.wd.basezk.model.Cmenu;
import com.wd.basezk.model.CroleMenu;

public interface CroleMenuService {
    public void insertData(Crole objNya, Set<Cmenu> objNya2);
    public void updateData(Crole objNya, Set<Cmenu> objNya2);
    public void deleteData(String idNya);
    public CroleMenu getById(String idNya);
    public List<CroleMenu> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs);
    public String generateIdForModel();
}
