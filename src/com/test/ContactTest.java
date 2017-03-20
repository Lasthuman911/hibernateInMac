package com.test;

import com.service.SessionFactoryUtil;

/**
 * Name: admin
 * Date: 2017/3/20
 * Time: 15:26
 */
public class ContactTest {
    public static void main(String[] args) {
        SessionFactoryUtil.getSessionFactory().openSession();
    }
}
