package com.test;

import com.domain.Employee;
import com.service.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Date;

/**
 * Created by lszhen on 2017/3/12.
 */
public class EmployeeTest {
    public static void main(String[] args) {



        //loadAndGetTest();


        //getCurrentSessionTest();

        //openSessionTest();

        //addEmployee2();
        //addEmplyee();
        //updateEmplyee();
        //deleteEmplyee();

    }

    /**
     * 观察sql的输出方式，比较load和get 方法的区别
     */
    private static void loadAndGetTest() {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Employee employee = session.load(Employee.class,1);
        System.out.println(employee.getName());

        Employee employee1 = session.load(Employee.class,1);
        System.out.println(employee1.getEmail());

        Employee employee2 = session.load(Employee.class,3);
        System.out.println(employee2.getHireDate());

        Employee employee3 = session.get(Employee.class,4);

        Employee employee4 = session.get(Employee.class,4);
    }

    /**
     * 每次都返回同一个会话
     */
    private static void getCurrentSessionTest() {
        Session session1 = SessionFactoryUtil.getSessionFactory().getCurrentSession();
        Session session2 = SessionFactoryUtil.getSessionFactory().getCurrentSession();
        System.out.println(session1.hashCode() + " " +session2.hashCode());
    }

    /**
     * openSession() 每次都会创建一个新的会话
     */
    private static void openSessionTest() {
        Session session1 = SessionFactoryUtil.getSessionFactory().openSession();
        Session session2 = SessionFactoryUtil.getSessionFactory().openSession();
        System.out.println(session1.hashCode() + " " + session2.hashCode());
    }

    /**
     * 用了单例模式，一个应用程序只生成一个SessionFactory
     */
    private static void addEmployee2() {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        try {
            Transaction transaction = session.beginTransaction();
            Employee employee = new Employee();
            employee.setName("wzm");
            employee.setEmail("wzm@163.com");
            employee.setHireDate(new Date());
            session.save(employee);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    private static void deleteEmplyee() {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Employee employee = session.load(Employee.class, 2);
        session.delete(employee);
        transaction.commit();
    }

    private static void updateEmplyee() {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        /*Transaction transaction = session.beginTransaction();
        Employee employee = session.load(Employee.class,1);
        employee.setName("lishuzhen");
        employee.setEmail("lishuzhen@163.com");
        transaction.commit();*/
        //改进后的模版代码
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Employee employee = session.load(Employee.class, 1);
            employee.setName("lishuzhen");
            employee.setEmail("lishuzhen@163.com");
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }//最好是抛出runntime异常，提供给上层，由上层来决定是否对异常进行处理
            throw new RuntimeException(e.getMessage());
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
    }

    private static void addEmplyee() {
        Configuration configuration = new Configuration().configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Employee employee = new Employee();
        employee.setName("wzm");
        employee.setEmail("wzm@163.com");
        employee.setHireDate(new Date());
        session.save(employee);
        transaction.commit();
        session.close();
    }
}
