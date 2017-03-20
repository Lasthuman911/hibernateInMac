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

    @Enumerated(EnumType.ORDINAL)//按序号
    @Column(name = "phone_type")
    private PhoneType type;
}
