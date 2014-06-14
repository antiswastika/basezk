package com.wd.basezk.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wd.basezk.dao.CroleDAO;
import com.wd.basezk.model.Crole;
import com.wd.basezk.service.CroleService;

@Service("croleService")
public class CroleServiceImpl implements CroleService {

    @Autowired
    private CroleDAO croleDAO;

    @Transactional
    public void insertData(Crole objNya) {
        croleDAO.insertData(objNya);
    }

    @Transactional
    public void updateData(Crole objNya) {
        croleDAO.updateData(objNya);
    }

    @Transactional
    public void deleteData(String idNya) {
        croleDAO.deleteData(idNya);
    }

    @Transactional
    public Crole getById(String idNya) {
        return croleDAO.getById(idNya);
    }

    @Transactional
    public List<Crole> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        return croleDAO.getByRequest(requestMap, useLikeKeyword, whereArgs);
    }

}
