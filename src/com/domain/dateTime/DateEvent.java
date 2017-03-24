package com.domain.dateTime;

import javax.persistence.*;
import java.util.Date;

/**
 * Name: admin
 * Date: 2017/3/21
 * Time: 11:16
 */
@Entity(name = "DateEvent")
public class DateEvent {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "timestamp")
    @Temporal(TemporalType.DATE)//java.util.Date mapped as DATE，只包含年月日
    private Date timestamp;
}
