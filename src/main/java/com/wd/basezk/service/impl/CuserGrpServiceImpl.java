package com.wd.basezk.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d1 = new Date();
        String formattedDate = df.format(d1);
        final Timestamp nowTs = Timestamp.valueOf(formattedDate);

        if (objNya.getCuserGrpId() == null || objNya.getCuserGrpId().equals(null)) {
            //Set default new ID
            objNya.setCuserGrpId( createPrimaryKey() );
        }

        System.out.println( "createPrimaryKey() = " + createPrimaryKey() );

        //Set defaut Deleteable
        objNya.setCuserGrpDeleteable(true);
        //Set defaut InputBy
        objNya.setCuserGrpInputby("System");
        //Set defaut InputOn
        objNya.setCuserGrpInputon(nowTs);

        //Finaly Save
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

    private String createPrimaryKey() {
        Date d1 = new Date();
        SimpleDateFormat yf = new SimpleDateFormat("yyyy");
        SimpleDateFormat mf = new SimpleDateFormat("MM");
        SimpleDateFormat df = new SimpleDateFormat("dd");

        final String section1 = "GRP" + yf.format(d1) + mf.format(d1) + df.format(d1);
        final String strDigits = "0000";
        int queryRet = 0;
        String retVal = "0001";
        String tempRetVal = "";

        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("section1", section1);
        queryRet = cuserGrpDAO.getMaxPKByRequest(requestMap, strDigits.length());

        if (queryRet > 0) {
            tempRetVal = strDigits + Integer.toString(queryRet + 1);
        } else {
            tempRetVal = strDigits + "1";
        }
        retVal = section1 + (tempRetVal.substring(tempRetVal.length()-strDigits.length()));

        return retVal;
    }

}
