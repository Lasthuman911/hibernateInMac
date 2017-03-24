package org.hibernate.tutorial.domain;

import java.util.Date;

/**
 * Name: admin
 * Date: 2017/3/15
 * Time: 10:17
 */
public class Event {

    public Event(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Long id;
    private String title;
    private Date date;
}
