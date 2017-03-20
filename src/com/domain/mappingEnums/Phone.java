package com.domain.mappingEnums;

import javax.persistence.*;

/**
 * Name: admin
 * Date: 2017/3/20
 * Time: 17:44
 */
@Entity(name = "Phone")
public class Phone {
    @Id
    private Long id;

    @Column(name = "phone_number")
    private String number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public PhoneType getType() {
        return type;
    }

    public void setType(PhoneType type) {
        this.type = type;
    }

   //@Enumerated(EnumType.ORDINAL)//按序号
    @Enumerated(EnumType.STRING)//按enum值
    @Column(name = "phone_type")
    private PhoneType type;
}
