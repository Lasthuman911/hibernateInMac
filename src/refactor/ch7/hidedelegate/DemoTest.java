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

        //原始
        System.out.println(john.getDepartment().getManager());

        //改进后，对客户隐藏了实现的细节，对客户隐藏了Department，减少耦合
        System.out.println(john.getManager());
    }
}
