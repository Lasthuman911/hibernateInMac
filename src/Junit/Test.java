package Junit;

/**
 * Name: admin
 * Date: 2017/3/23
 * Time: 13:12
 */
public class Test {
    public static void main(String[] args) {
        int a = 1;
        assertEquals(a,1);
    }

    /**
     * 假如没有junit框架
     * @param a
     * @param b
     */
    public static void assertEquals(int a, int b){
        if (a == b)
            System.out.println(true);
        else
            System.out.println(false);
    }
}
