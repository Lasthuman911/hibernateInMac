package refactor.ch7.hidedelegate;

/**
 * Name: admin
 * Date: 2017/5/19
 * Time: 17:40
 */
public class Person {
    public Person(refactor.ch7.hidedelegate.Department department) {
        Department = department;
    }

    Department Department;

    public refactor.ch7.hidedelegate.Department getDepartment() {
        return Department;
    }

    public void setDepartment(refactor.ch7.hidedelegate.Department department) {
        Department = department;
    }
}
