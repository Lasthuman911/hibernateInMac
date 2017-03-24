package util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Name: admin
 * Date: 2017/3/22
 * Time: 13:46
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory = null;

    private HibernateUtil(){}

    //获取SessionFactory-case 1
/*    public static SessionFactory getSessionFaction() {
        if (sessionFactory == null) {
        //new Configuration()-程序会去找hibernate.properties, .configure()-程序默认加载hibernate.cfg.xml
        //若两个文件同时存在，以xml文件为准
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }*/

    //获取SessionFactory-case 2
    /*static {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public static SessionFactory getSessionFaction() {
        return sessionFactory;
    }*/

    //获取SessionFactory-case 3-Hibernate官方
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
