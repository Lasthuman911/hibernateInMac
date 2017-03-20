package com.domain;

import org.hibernate.annotations.Table;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.net.URL;

/**
 * Name: admin
 * Date: 2017/3/20
 * Time: 14:32
 */
@Entity(name = "Contact" )
public class Contact implements Serializable{
    @Id//用于设定主键属性
    private Integer id;
    private Name name;
    private String notes;
    private URL website;
    private boolean starred;

    public Contact() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public URL getWebsite() {
        return website;
    }

    public void setWebsite(URL website) {
        this.website = website;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }
}
