package com.service;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**使用简单工厂模式，生成唯一的SessionFactory
 * Created by lszhen on 2017/3/12.
 */
 public final class SessionFactoryUtil {

     private static SessionFactory sessionFactory = null;
     private SessionFactoryUtil(){}

     static {
         sessionFactory = new Configuration().configure().buildSessionFactory();
     }

     public static SessionFactory getSessionFactory(){
         return sessionFactory;
     }
}
