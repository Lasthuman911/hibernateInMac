package refactor.ch7.hidedelegate;

/**
 * Name: admin
 * Date: 2017/5/19
 * Time: 17:40
 */
public class Person {
    public Person(Department arg) {
        department = arg;
    }

    Department department;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * 改进后，添加委托函数
     * @return
     */
    public String getManager(){
        return  department.getManager();
    }
}
