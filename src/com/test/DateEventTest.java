package com.test;

import com.domain.dateTime.DateEvent;
import com.domain.dateTime.TimeEvent;
import com.service.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;

/**
 * Name: admin
 * Date: 2017/3/21
 * Time: 11:21
 */
public class DateEventTest {
    public static void main(String[] args) {
        Session session =  SessionFactoryUtil.getSessionFactory().openSession();
        try{
            Transaction transaction = session.beginTransaction();

            //case1: Date类型
            /*DateEvent dateEvent = new DateEvent();
            dateEvent.setTimestamp(new Date());
            session.save(dateEvent);*/

            TimeEvent dateEvent = new TimeEvent();
            dateEvent.setTimestamp(new Date());
            session.save(dateEvent);


            transaction.commit();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }finally {
            if (session!=null && session.isOpen()){
                session.close();
            }
        }

    }
}
