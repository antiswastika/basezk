package com.wd.basezk.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wd.basezk.dao.CuserGrpDAO;
import com.wd.basezk.model.CuserGrp;
import com.wd.basezk.service.CuserGrpService;

@Service("cuserGrpService")
public class CuserGrpServiceImpl implements CuserGrpService {

    @Autowired
    private CuserGrpDAO cuserGrpDAO;

    @Transactional
    public void insertData(CuserGrp objNya) {
        cuserGrpDAO.insertData(objNya);
    }

    @Transactional
    public void updateData(CuserGrp objNya) {
        cuserGrpDAO.updateData(objNya);
    }

    @Transactional
    public void deleteData(String idNya) {
        cuserGrpDAO.deleteData(idNya);
    }

    @Transactional
    public CuserGrp getById(String idNya) {
        return cuserGrpDAO.getById(idNya);
    }

    @Transactional
    public List<CuserGrp> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        return cuserGrpDAO.getByRequest(requestMap, useLikeKeyword, whereArgs);
    }

}
