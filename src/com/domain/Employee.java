package com.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lszhen on 2017/3/12.
 */
public class Employee implements Serializable{
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    Integer id;
    String name;
    String email;
    Date hireDate;
}
