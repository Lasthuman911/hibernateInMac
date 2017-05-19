package refactor.ch7;

/**
 * Name: admin
 * Date: 2017/5/15
 * Time: 10:23
 */
public class PersonNew {
    private String name;
    TelephoneNumber telephoneNumber = new TelephoneNumber();

    public String getTelephoneNumber(){
        return (this.getOfficeCode() + this.getOfficeNumber());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficeCode() {
        return telephoneNumber.getAreaCode();
    }

    public String getOfficeNumber() {
        return telephoneNumber.getNumber();
    }

}
