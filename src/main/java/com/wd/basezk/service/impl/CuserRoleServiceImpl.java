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

import com.wd.basezk.dao.CuserDAO;
import com.wd.basezk.dao.CuserRoleDAO;
import com.wd.basezk.model.Crole;
import com.wd.basezk.model.Cuser;
import com.wd.basezk.model.CuserRole;
import com.wd.basezk.service.CuserRoleService;

@Service("cuserRoleService")
public class CuserRoleServiceImpl implements CuserRoleService {

    @Autowired
    private CuserRoleDAO cuserRoleDAO;
    @Autowired
    private CuserDAO cuserDAO;

    @SuppressWarnings("rawtypes")
    @Transactional
    public void insertData(Cuser objNya, Set<Crole> objNya2) {
        CuserRole objToSave;
        List<CuserRole> cuserRoleNya = new ArrayList<CuserRole>();

        //Step 1: Get Object Parent Untuk Kepentingan Proxy (Lazy)
        Cuser userNya = cuserDAO.getById(objNya.getCuserId());

        //Step 2: Save Semua CuserRole
        Iterator iterator = objNya2.iterator();
        while (iterator.hasNext()){
            Crole croleNya = (Crole) iterator.next();

            objToSave = new CuserRole();
            objToSave.setCuserRoleId(cuserRoleDAO.generateIdForModel());
            objToSave.setCuser(userNya);
            objToSave.setCrole(croleNya);

            cuserRoleDAO.insertData(objToSave);
            cuserRoleNya.add(objToSave);
        }

        //Step 3: Update Parent
        Set<CuserRole> objToSave2 = new HashSet<CuserRole>(cuserRoleNya);
        userNya.setCuserRoles(objToSave2);
        cuserDAO.updateData(userNya);
    }

    @Transactional
    public void updateData(Cuser objNya, Set<Crole> objNya2) {
        deleteData(objNya.getCuserId());
        insertData(objNya, objNya2);
    }

    @SuppressWarnings("rawtypes")
    @Transactional
    public void deleteData(String idNya) {
        //Step 1: Get Object Parent Untuk Kepentingan Proxy (Lazy)
        Cuser userNya = cuserDAO.getById(idNya);

        //Step 2: Delete Semua Child
        Iterator iterator = userNya.getCuserRoles().iterator();
        while (iterator.hasNext()){
            CuserRole objToDel = (CuserRole) iterator.next();
            cuserRoleDAO.deleteData(objToDel.getCuserRoleId());
        }
        userNya.getCuserRoles().clear();

        //Step 3: Update Parent
        cuserDAO.updateData(userNya);
    }

    @Transactional
    public CuserRole getById(String idNya) {
        return cuserRoleDAO.getById(idNya);
    }

    @Transactional
    public List<CuserRole> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        return cuserRoleDAO.getByRequest(requestMap, useLikeKeyword, whereArgs);
    }

    @Transactional
    public String generateIdForModel() {
        return cuserRoleDAO.generateIdForModel();
    }

}
