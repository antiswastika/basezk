package com.wd.basezk.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wd.basezk.dao.CuserDAO;
import com.wd.basezk.model.Cuser;

@Repository("cuserDAO")
public class CuserDAOImpl implements CuserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void insertData(Cuser objNya) {
        sessionFactory.getCurrentSession().save(objNya);
    }

    public void updateData(Cuser objNya) {
        sessionFactory.getCurrentSession().update(objNya);
    }

    public void deleteData(String idNya) {
        sessionFactory.getCurrentSession().delete(getById(idNya));
    }

    public Cuser getById(String idNya) {
        return (Cuser) sessionFactory.getCurrentSession().get(Cuser.class, idNya);
    }

    @SuppressWarnings("unchecked")
    public List<Cuser> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        List<Cuser> result = null; // init

        String finalQuery = "";
        String queryFrom = " FROM Cuser ";
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
        queryWhere = queryWhere + " 1=1 " + queryWhereExt + " ORDER BY cuser_username ASC";

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
    public int getMaxIdByRequest(Map<String, String> requestMap, int manyDigit) {
        String finalQuery = "";
        int result = 1; // init
        String queryFrom = "SELECT MAX( CAST(SUBSTRING(cuser_id, LENGTH(cuser_id) - " + (manyDigit-1) + ", " + manyDigit + " ), int) ) FROM cuser ";
        String queryWhere = " WHERE ";

        Object params[] = new Object[requestMap.size()];
        int a = 0;

        for (Map.Entry<String, String> entry : requestMap.entrySet()) {
            String q = entry.getKey();
            String v = entry.getValue();

            if (q=="cuser_id") {
                queryWhere = queryWhere + " SUBSTRING(cuser_id, 1, " + v.length() + ") = ? AND ";
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
