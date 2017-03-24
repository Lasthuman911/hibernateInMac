package com.domain.dateTime;

import javax.persistence.*;
import java.util.Date;

/**
 * Name: admin
 * Date: 2017/3/21
 * Time: 11:16
 */
@Entity(name = "TimeEvent")
public class TimeEvent {

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
    //@Temporal(TemporalType.TIME)//java.util.Date mapped as DATE,包含时分秒
    @Temporal(TemporalType.TIMESTAMP)//包含纳秒
    private Date timestamp;
}
