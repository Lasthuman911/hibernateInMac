package com.caveat;

import java.io.Serializable;

/**
 * Created by lszhen on 2017/3/22.
 */
//当对象存储在一个HttpSession中，或者用RMI按值传递时，就需要序列化
public class User implements Serializable{

    private Integer userID;
    private String userName;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    private Address address;

    public User(){}

}
