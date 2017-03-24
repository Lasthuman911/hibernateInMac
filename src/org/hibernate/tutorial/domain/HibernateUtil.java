package org.hibernate.tutorial.domain;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * hibernate 4.1 生成单例SessionFactory
 * Name: admin
 * Date: 2017/3/15
 * Time: 11:07
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory =buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        try{
            SessionFactory sessionFactory= new Configuration().configure().buildSessionFactory();
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
