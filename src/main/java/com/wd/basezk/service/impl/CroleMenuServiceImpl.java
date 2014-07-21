package com.wd.basezk.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wd.basezk.dao.CroleDAO;
import com.wd.basezk.dao.CroleMenuDAO;
import com.wd.basezk.model.Cmenu;
import com.wd.basezk.model.Crole;
import com.wd.basezk.model.CroleMenu;
import com.wd.basezk.service.CroleMenuService;

@Service("croleMenuService")
public class CroleMenuServiceImpl implements CroleMenuService {

    @Autowired
    private CroleMenuDAO croleMenuDAO;
    @Autowired
    private CroleDAO croleDAO;

    @SuppressWarnings("rawtypes")
    @Transactional
    public void insertData(Crole objNya, Set<Cmenu> objNya2) {
        //Delete dulu semua data yang bersangkutan
        deleteData(objNya.getCroleId());

        CroleMenu objToSave;
        List<CroleMenu> croleMenuNya = new ArrayList<CroleMenu>();

        //Step 1: Get Object Parent Untuk Kepentingan Proxy (Lazy)
        Crole roleNya = croleDAO.getById(objNya.getCroleId());

        //Step 2: Save Semua CroleMenu
        Iterator iterator = objNya2.iterator();
        while (iterator.hasNext()){
            Cmenu cmenuNya = (Cmenu) iterator.next();

            objToSave = new CroleMenu();
            objToSave.setCroleMenuId(croleMenuDAO.generateIdForModel());
            objToSave.setCrole(roleNya);
            objToSave.setCmenu(cmenuNya);

            croleMenuDAO.insertData(objToSave);
            croleMenuNya.add(objToSave);
        }

        //Step 3: Update Parent
        Set<CroleMenu> objToSave2 = new HashSet<CroleMenu>(croleMenuNya);
        roleNya.setCroleMenus(objToSave2);
        croleDAO.updateData(roleNya);
    }

    @Transactional
    public void updateData(Crole objNya, Set<Cmenu> objNya2) {
        insertData(objNya, objNya2);
    }

    @SuppressWarnings("rawtypes")
    @Transactional
    public void deleteData(String idNya) {
        //Step 1: Get Object Parent Untuk Kepentingan Proxy (Lazy)
        Crole roleNya = croleDAO.getById(idNya);

        //Step 2: Delete Semua Child
        Iterator iterator = roleNya.getCroleMenus().iterator();
        while (iterator.hasNext()){
            CroleMenu objToDel = (CroleMenu) iterator.next();
            croleMenuDAO.deleteData(objToDel.getCroleMenuId());
        }
        roleNya.getCroleMenus().clear();

        //Step 3: Update Parent
        croleDAO.updateData(roleNya);
    }

    @Transactional
    public CroleMenu getById(String idNya) {
        return croleMenuDAO.getById(idNya);
    }

    @Transactional
    public List<CroleMenu> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        return croleMenuDAO.getByRequest(requestMap, useLikeKeyword, whereArgs);
    }

    @Transactional
    public String generateIdForModel() {
        return croleMenuDAO.generateIdForModel();
    }

}
