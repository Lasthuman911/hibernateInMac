package com.service;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.MetadataSource;


/**使用简单工厂模式，生成唯一的SessionFactory
 * Created by lszhen on 2017/3/12.
 */
 public final class SessionFactoryUtil {
//case1:
/*     private static SessionFactory sessionFactory = null;
     private SessionFactoryUtil(){}

     static {
         sessionFactory = new Configuration().configure().buildSessionFactory();
     }

     public static SessionFactory getSessionFactory(){
         return sessionFactory;
     }*/
//case2:hibernate 官方文档推荐写法
    private static SessionFactory sessionFactory = null;

    private SessionFactoryUtil(){}

    static{
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()// configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }catch (Exception e){
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

}
