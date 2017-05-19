package refactor.ch7;

/**
 * Name: admin
 * Date: 2017/5/15
 * Time: 10:23
 */
public class Person {
    private String name;
    private String officeCode;
    private String officeNumber;

    public String getTelephoneNumber(){
        return (officeCode + officeNumber);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

}
