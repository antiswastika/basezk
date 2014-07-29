package com.wd.basezk.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wd.basezk.dao.CmoduleDAO;
import com.wd.basezk.model.Cmodule;
import com.wd.basezk.service.CmoduleService;

@Service("cmoduleService")
public class CmoduleServiceImpl implements CmoduleService {

    @Autowired
    private CmoduleDAO cmoduleDAO;

    @Transactional
    public void insertData(Cmodule objNya) {
        cmoduleDAO.insertData(objNya);
    }

    @Transactional
    public void updateData(Cmodule objNya) {
        cmoduleDAO.updateData(objNya);
    }

    @Transactional
    public void deleteData(String idNya) {
        cmoduleDAO.deleteData(idNya);
    }

    @Transactional
    public Cmodule getById(String idNya) {
        return cmoduleDAO.getById(idNya);
    }

    @Transactional
    public List<Cmodule> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        return cmoduleDAO.getByRequest(requestMap, useLikeKeyword, whereArgs);
    }

}
