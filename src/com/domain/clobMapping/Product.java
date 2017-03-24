package com.domain.clobMapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.sql.Clob;

/**
 * Name: admin
 * Date: 2017/3/21
 * Time: 8:59
 */
@Entity(name = "Product")
public class Product {
    @Id
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Clob getWarranty() {
        return warranty;
    }

    public void setWarranty(Clob warranty) {
        this.warranty = warranty;
    }

    @Lob
    @Column(name = "image")
    private Clob warranty;
}
