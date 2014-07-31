package com.wd.basezk.dao.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wd.basezk.dao.CmoduleDAO;
import com.wd.basezk.model.Cmodule;

@Repository("cmoduleDAO")
public class CmoduleDAOImpl implements CmoduleDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public Boolean insertData(Cmodule objNya) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d1 = new Date();
        String formattedDate = df.format(d1);
        final Timestamp nowTs = Timestamp.valueOf(formattedDate);

        //Set defaut Deleteable
        objNya.setCmoduleDeleteable(true);
        //Set defaut InputBy
        objNya.setCmoduleInputby("System");
        //Set defaut InputOn
        objNya.setCmoduleInputon(nowTs);
        //Finaly Save

        try {
            sessionFactory.getCurrentSession().save(objNya);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Boolean updateData(Cmodule objNya) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d1 = new Date();
        String formattedDate = df.format(d1);
        final Timestamp nowTs = Timestamp.valueOf(formattedDate);

        //Set defaut UpdateBy
        objNya.setCmoduleUpdateby("System");
        //Set defaut UpdateOn
        objNya.setCmoduleUpdateon(nowTs);
        //Finaly Save

        try {
            sessionFactory.getCurrentSession().update(objNya);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean deleteData(String idNya) {
        Cmodule objNya = getById(idNya);

        try {
            //Cek apakah data BOLEH di-delete
            if (objNya.getCmoduleDeleteable() == true) {
                //Untuk Delete Hanya Status, uncomment statement dibawah ini.
                //===========================================================
                /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date d1 = new Date();
                String formattedDate = df.format(d1);
                final Timestamp nowTs = Timestamp.valueOf(formattedDate);
                //Set defaut DeleteBy
                objNya.setCmoduleDeleteby("System");
                //Set defaut DeleteOn
                objNya.setCmoduleDeleteon(nowTs);
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

    public Cmodule getById(String idNya) {
        return (Cmodule) sessionFactory.getCurrentSession().get(Cmodule.class, idNya);
    }

    @SuppressWarnings("unchecked")
    public List<Cmodule> getByRequest(Map<String, String> requestMap, boolean useLikeKeyword, String[] whereArgs) {
        List<Cmodule> result = null; // init

        String finalQuery = "";
        String queryFrom = " FROM Cmodule ";
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
        queryWhere = queryWhere + " 1=1 " + queryWhereExt + " ORDER BY cmodule_name ASC";

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
