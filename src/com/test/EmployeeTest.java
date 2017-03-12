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
        addEmplyee();
        //updateEmplyee();
        //deleteEmplyee();

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
    }
}
