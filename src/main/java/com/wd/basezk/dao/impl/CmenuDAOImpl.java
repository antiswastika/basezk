package com.wd.basezk.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wd.basezk.dao.CmenuDAO;
import com.wd.basezk.model.Cmenu;

@Repository("cmenuDAO")
public class CmenuDAOImpl implements CmenuDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void insertData(Cmenu objNya) {
        sessionFactory.getCurrentSession().save(objNya);
    }

    public void updateData(Cmenu objNya) {
        sessionFactory.getCurrentSession().update(objNya);
    }

    public void deleteData(String idNya) {
        sessionFactory.getCurrentSession().delete(getById(idNya));
    }

    public Cmenu getById(String idNya) {
        return (Cmenu) sessionFactory.getCurrentSession().get(Cmenu.class, idNya);
    }

    @SuppressWarnings("unchecked")
    public List<Cmenu> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        List<Cmenu> result = null; // init

        String finalQuery = "";
        String queryFrom = " FROM Cmenu ";
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
        queryWhere = queryWhere + " 1=1 " + queryWhereExt + " ORDER BY cmenu_seq ASC";

        finalQuery = queryFrom + queryWhere;
        Query query = sessionFactory.getCurrentSession().createQuery(finalQuery);
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++)
                 query.setParameter( i, params[i] );
         }

        result = query.list();
        return result;
    }

}
