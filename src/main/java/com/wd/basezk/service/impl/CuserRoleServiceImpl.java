package com.wd.basezk.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wd.basezk.dao.CuserRoleDAO;
import com.wd.basezk.model.CuserRole;
import com.wd.basezk.service.CuserRoleService;

@Service("cuserRoleService")
public class CuserRoleServiceImpl implements CuserRoleService {

    @Autowired
    private CuserRoleDAO cuserRoleDAO;

    @Transactional
    public void insertData(CuserRole objNya) {
        cuserRoleDAO.insertData(objNya);
    }

    @Transactional
    public void updateData(CuserRole objNya) {
        cuserRoleDAO.updateData(objNya);
    }

    @Transactional
    public void deleteData(String idNya) {
        cuserRoleDAO.deleteData(idNya);
    }

    @Transactional
    public CuserRole getById(String idNya) {
        return cuserRoleDAO.getById(idNya);
    }

    @Transactional
    public List<CuserRole> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        return cuserRoleDAO.getByRequest(requestMap, useLikeKeyword, whereArgs);
    }

}
