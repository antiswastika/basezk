package com.wd.basezk.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wd.basezk.dao.CuserDAO;
import com.wd.basezk.model.Cuser;
import com.wd.basezk.service.CuserService;

@Service("cuserService")
public class CuserServiceImpl implements CuserService {

    @Autowired
    private CuserDAO cuserDAO;

    @Transactional
    public void insertData(Cuser objNya) {
        cuserDAO.insertData(objNya);
    }

    @Transactional
    public void updateData(Cuser objNya) {
        cuserDAO.updateData(objNya);
    }

    @Transactional
    public void deleteData(String idNya) {
        cuserDAO.deleteData(idNya);
    }

    @Transactional
    public Cuser getById(String idNya) {
        return cuserDAO.getById(idNya);
    }

    @Transactional
    public List<Cuser> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        return cuserDAO.getByRequest(requestMap, useLikeKeyword, whereArgs);
    }

}
