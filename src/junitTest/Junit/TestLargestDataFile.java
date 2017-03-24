package Junit;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Name: admin
 * Date: 2017/3/24
 * Time: 8:23
 */
public class TestLargestDataFile extends TestCase {

    public TestLargestDataFile(String name) {
        super(name);
    }

    public void testFromFile() throws Exception {

        String line;
        int testNumber = 1;
        BufferedReader rdr = new BufferedReader(new FileReader("D:\\MIT\\java\\hibernateInMac\\src\\testdata.txt"));

        while ((line = rdr.readLine()) != null) {//Reads a line of text
            if (line.startsWith("#"))
                continue;

            StringTokenizer st = new StringTokenizer(line);//StringTokenizer已经不建议使用了，改为用split

            if (!st.hasMoreTokens())
                continue;

            String val = st.nextToken();
            int expected = Integer.valueOf(val);

            ArrayList argument_list = new ArrayList();
            while (st.hasMoreTokens()) {
                argument_list.add(Integer.valueOf(st.nextToken()));
            }

            int[] arguments = new int[argument_list.size()];
            for (int i = 0; i < argument_list.size(); i++) {
                arguments[i] = ((Integer) (argument_list.get(i))).intValue();
            }

            System.out.println("第" + testNumber + "个测试开始---------");
            assertEquals(expected, Largest.getLargest(arguments));
            System.out.println("第" + testNumber + "个测试OK");
            System.out.println("第" + testNumber + "个测试结束---------\n");
            testNumber++;
        }
    }
}
