package hibernateInAction.caveat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Name: admin
 * Date: 2017/3/23
 * Time: 8:29
 */
@Entity(name = "User")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    private String userName;
    private Address address;

    public User(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
