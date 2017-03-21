package com.test;

import com.domain.mappingEnums.Phone;
import com.domain.mappingEnums.PhoneType;
import com.service.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Name: admin
 * Date: 2017/3/20
 * Time: 15:26
 */
public class ContactTest {
    public static void main(String[] args) {
       Session session =  SessionFactoryUtil.getSessionFactory().openSession();
       try{
           Transaction transaction = session.beginTransaction();
           Phone phone = new Phone();
           phone.setId(1L);
           phone.setNumber("2328765");
           phone.setType(PhoneType.MOBILE);
          // entityManager.persist(phone);
           session.save(phone);
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
