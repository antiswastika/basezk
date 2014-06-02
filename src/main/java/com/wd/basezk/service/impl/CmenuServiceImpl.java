package com.wd.basezk.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wd.basezk.dao.CmenuDAO;
import com.wd.basezk.model.Cmenu;
import com.wd.basezk.service.CmenuService;

@Service("cmenuService")
public class CmenuServiceImpl implements CmenuService {

    @Autowired
    private CmenuDAO cmenuDAO;

    @Transactional
    public void insertData(Cmenu objNya) {
        cmenuDAO.insertData(objNya);
    }

    @Transactional
    public void updateData(Cmenu objNya) {
        cmenuDAO.updateData(objNya);
    }

    @Transactional
    public void deleteData(String idNya) {
        cmenuDAO.deleteData(idNya);
    }

    @Transactional
    public Cmenu getById(String idNya) {
        return cmenuDAO.getById(idNya);
    }

    @Transactional
    public List<Cmenu> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        return cmenuDAO.getByRequest(requestMap, useLikeKeyword, whereArgs);
    }

}
