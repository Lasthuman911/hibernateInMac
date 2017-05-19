package refactor.ch7.hidedelegate;

/**
 * Name: admin
 * Date: 2017/5/19
 * Time: 17:42
 */
public class DemoTest {
    public static void main(String[] args) {
        Department department = new Department();
        department.setManager("wzm");

        Person john = new Person(department);
       System.out.println(john.getDepartment().getManager());
    }
}
