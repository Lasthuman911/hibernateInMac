package com.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Name: admin
 * Date: 2017/3/20
 * Time: 15:54
 */
@Entity(name = "Product")
public class Product {
    @Id
    private Integer id;
    private String sku;
    @org.hibernate.annotations.Type(type = "nstring")//This tells Hibernate to store the Strings as nationalized data
    private String name;
    @Column(name = "NOTES")//手动指定对应的数据库列名
    @org.hibernate.annotations.Type(type = "materialized_nclob")//the description is to be handled as a LOB
    private String description;
}
