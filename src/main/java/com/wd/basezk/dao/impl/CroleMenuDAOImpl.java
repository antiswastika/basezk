package com.wd.basezk.dao.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wd.basezk.dao.CroleMenuDAO;
import com.wd.basezk.model.CroleMenu;

@Repository("croleMenuDAO")
public class CroleMenuDAOImpl implements CroleMenuDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public Boolean insertData(CroleMenu objNya) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d1 = new Date();
        String formattedDate = df.format(d1);
        final Timestamp nowTs = Timestamp.valueOf(formattedDate);

        //Set defaut Deleteable
        objNya.setCroleMenuDeleteable(true);
        //Set defaut InputBy
        objNya.setCroleMenuInputby("System");
        //Set defaut InputOn
        objNya.setCroleMenuInputon(nowTs);
        //Finaly Save

        try {
            sessionFactory.getCurrentSession().save(objNya);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Boolean deleteData(String idNya) {
        CroleMenu objNya = getById(idNya);

        try {
            //Cek apakah data BOLEH di-delete
            if (objNya.getCroleMenuDeleteable() == true) {

                //Untuk Delete Hanya Status, uncomment statement dibawah ini.
                //===========================================================
                /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date d1 = new Date();
                String formattedDate = df.format(d1);
                final Timestamp nowTs = Timestamp.valueOf(formattedDate);
                //Set defaut DeleteBy
                objNya.setCroleMenuDeleteby("System");
                //Set defaut DeleteOn
                objNya.setCroleMenuDeleteon(nowTs);
                //Finaly Save
                sessionFactory.getCurrentSession().update(objNya);*/

                //Untuk Delete Permanent, uncomment statement dibawah ini.
                //===========================================================
                sessionFactory.getCurrentSession().delete(objNya);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public CroleMenu getById(String idNya) {
        return (CroleMenu) sessionFactory.getCurrentSession().get(CroleMenu.class, idNya);
    }

    @SuppressWarnings("unchecked")
    public List<CroleMenu> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        List<CroleMenu> result = null; // init

        String finalQuery = "";
        String queryFrom = " FROM CroleMenu ";
        String queryWhere = " WHERE ";
        String queryWhereExt = "";

        Object params[] = new Object[requestMap.size()];
        int a = 0;

        for (Map.Entry<String, String> entry : requestMap.entrySet()) {
            String q = entry.getKey();
            String v = entry.getValue();

            if (useLikeKeyword == true) {
                if (q.equalsIgnoreCase("null")) {
                    queryWhere = queryWhere + " 1 = ? AND ";
                    params[a] = 1;
                } else {
                    queryWhere = queryWhere + " LOWER(" + q + ") LIKE ? AND ";
                    params[a] = "%" + v.toLowerCase() + "%";
                }
            } else {
                if (q.equalsIgnoreCase("null")) {
                    queryWhere = queryWhere + " 1 = ? AND ";
                    params[a] = 1;
                } else {
                    queryWhere = queryWhere + " LOWER(" + q + ") = ? AND ";
                    params[a] = v.toLowerCase();
                }
            }

            a++;
        }
        if (whereArgs != null) { for (int i=0; i < whereArgs.length; i++) { queryWhereExt = queryWhereExt + whereArgs[i]; } }
        queryWhere = queryWhere + " 1=1 " + queryWhereExt + " ORDER BY crole_menu_id ASC";

        finalQuery = queryFrom + queryWhere;
        Query query = sessionFactory.getCurrentSession().createQuery(finalQuery);
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++)
                 query.setParameter( i, params[i] );
         }

        result = query.list();
        return result;
    }

    private int getMaxPKByRequest(Map<String, String> requestMap, int manyDigit) {
        String finalQuery = "";
        int result = 1; // init
        String queryFrom = "SELECT MAX( CAST(SUBSTRING(crole_menu_id, LENGTH(crole_menu_id) - " + (manyDigit-1) + ", " + manyDigit + " ), int) ) FROM CroleMenu ";
        String queryWhere = " WHERE ";

        Object params[] = new Object[requestMap.size()];
        int a = 0;

        for (Map.Entry<String, String> entry : requestMap.entrySet()) {
            String q = entry.getKey();
            String v = entry.getValue();

            if (q=="section1") {
                queryWhere = queryWhere + " SUBSTRING(crole_menu_id, LENGTH(crole_menu_id) - 16, 13) = ? AND ";
                params[a] = v;
            }

            a++;
        }
        queryWhere = queryWhere + " 1=1 ";

        finalQuery = queryFrom + queryWhere;
        Query query = sessionFactory.getCurrentSession().createQuery(finalQuery);
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++)
                 query.setParameter( i, params[i] );
        }

        if ((query.list()).get(0) != null) {
            result = (Integer) (query.list()).get(0);
        }

        return result;
    }

    private String createPrimaryKey() {
        Date d1 = new Date();
        SimpleDateFormat yf = new SimpleDateFormat("yyyy");
        SimpleDateFormat mf = new SimpleDateFormat("MM");
        SimpleDateFormat df = new SimpleDateFormat("dd");

        final String section1 = "RMN" + yf.format(d1) + mf.format(d1) + df.format(d1);
        final String strDigits = "0000";
        int queryRet = 0;
        String retVal = "0000";
        String tempRetVal = "";

        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("section1", section1);
        queryRet = getMaxPKByRequest(requestMap, strDigits.length());

        if (queryRet > 0) {
            tempRetVal = strDigits + Integer.toString(queryRet + 1);
        } else {
            tempRetVal = strDigits + "1";
        }
        retVal = section1 + (tempRetVal.substring(tempRetVal.length()-strDigits.length()));

        return retVal;
    }

    public String generateIdForModel() {
        return createPrimaryKey();
    }

}
