package com.test;

import com.domain.clobMapping.Product;
import com.service.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.jdbc.ClobProxy;

import java.io.Reader;

/**
 * Name: admin
 * Date: 2017/3/21
 * Time: 9:03
 */
public class ProductTest {
    public static void main(String[] args) {

        //判断product的clob值是否OK
        //assertEqualsImage();

        //创建一个product
        //createProduct();
    }

    private static void assertEqualsImage() {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        try {
            Transaction transaction = session.beginTransaction();

            Product product = session.load(Product.class,1);
            //TODO
            try(Reader reader = product.getWarranty().getCharacterStream()){
                System.out.println(reader.read());//涉及到IO流的读取，TBD
            }

            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    private static void createProduct() {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        try {
            Transaction transaction = session.beginTransaction();
            String warranty = "My product warranty";
            final Product product = new Product();
            product.setId(1);
            product.setName("Mobile phone");
            //doWork(Work work):Controller for allowing users to perform JDBC related work using the Connection managed by this Session.
          /*  session.doWork(connection -> {
                product.setWarranty(ClobProxy.generateProxy(warranty
                ));
            });*///使用纯jdbc生成clob数据
            session.save(product);

            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
