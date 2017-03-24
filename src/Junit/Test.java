package Junit;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Name: admin
 * Date: 2017/3/23
 * Time: 13:12
 */
public class Test {
    public static void main(String[] args) throws Exception {
//        int a = 1;
//        assertEquals(a,1);

        testStringSplit();
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

    public static void testStringSplit() throws Exception{
        String line;
        BufferedReader rdr = new BufferedReader(new FileReader("D:\\MIT\\java\\hibernateInMac\\src\\testdata.txt"));
        while ((line = rdr.readLine()) != null)
        {
            String[] s =line.split("\\s+");
            for (int i = 0;i<s.length;i++)
                System.out.println(s[i]);
        }
    }
}
