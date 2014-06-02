package com.wd.basezk.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wd.basezk.dao.CuserGrpDAO;
import com.wd.basezk.model.CuserGrp;

@Repository("cuserGrpDAO")
public class CuserGrpDAOImpl implements CuserGrpDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void insertData(CuserGrp objNya) {
        sessionFactory.getCurrentSession().save(objNya);
    }

    public void updateData(CuserGrp objNya) {
        sessionFactory.getCurrentSession().update(objNya);
    }

    public void deleteData(String idNya) {
        sessionFactory.getCurrentSession().delete(getById(idNya));
    }

    public CuserGrp getById(String idNya) {
        return (CuserGrp) sessionFactory.getCurrentSession().get(CuserGrp.class, idNya);
    }

    @SuppressWarnings("unchecked")
    public List<CuserGrp> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        List<CuserGrp> result = null; // init

        String finalQuery = "";
        String queryFrom = " FROM CuserGrp ";
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
        queryWhere = queryWhere + " 1=1 " + queryWhereExt + " ORDER BY cuser_grp_name ASC";

        finalQuery = queryFrom + queryWhere;
        Query query = sessionFactory.getCurrentSession().createQuery(finalQuery);
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++)
                 query.setParameter( i, params[i] );
         }

        result = query.list();
        return result;
    }

    @Override
    public int getMaxPKByRequest(Map<String, String> requestMap, int manyDigit) {
        String finalQuery = "";
        int result = 1; // init
        String queryFrom = "SELECT MAX( CAST(SUBSTRING(cuser_grp_id, LENGTH(cuser_grp_id) - " + (manyDigit-1) + ", " + manyDigit + " ), int) ) FROM CuserGrp ";
        String queryWhere = " WHERE ";

        Object params[] = new Object[requestMap.size()];
        int a = 0;

        for (Map.Entry<String, String> entry : requestMap.entrySet()) {
            String q = entry.getKey();
            String v = entry.getValue();

            if (q=="section1") {
                queryWhere = queryWhere + " SUBSTRING(cuser_grp_id, LENGTH(cuser_grp_id) - 16, 13) = ? AND ";
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

        result = (Integer) (query.list()).get(0);
        return result;
    }

}
