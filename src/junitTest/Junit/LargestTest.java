package Junit;

import junit.extensions.TestSetup;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

/**
 * Name: admin
 * Date: 2017/3/23
 * Time: 14:02
 */
public class LargestTest extends TestCase {

    public LargestTest(String method) {
        super(method);
    }

    /**
     * 在每个测试之前调用
     * @throws Exception
     */
    @Override
    public void setUp() throws Exception {
        System.out.println("before测试");
    }

    /**
     * 在每个测试之后调用
     * @throws Exception
     */
    @Override
    public void tearDown() throws Exception {
        System.out.println("after测试");
    }

    /**
     * 单独的测试
     * @throws Exception
     */
    @Test
    public void testGetLargest() throws Exception {
        assertEquals(9, Largest.getLargest(new int[]{8, 7, 9}));
        assertEquals(9, Largest.getLargest(new int[]{7, 7, 9}));
        assertEquals(9, Largest.getLargest(new int[]{7, 8, 9}));
        assertEquals(-7, Largest.getLargest(new int[]{-7, -8, -9}));
        assertEquals(-7, Largest.getLargest(new int[]{-7}));
        assertEquals(7, Largest.getLargest(new int[]{7}));
        assertEquals("Shoublebe", 3.33, 10.0 / 3.0, 0.01);
        assertNotNull(new int[]{});
    }

    @Test
    public void testEmptyOfList() {
        try {
            Largest.getLargest(new int[]{});//应该要抛异常，若抛了异常则我们认为测试通过
            fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }
/**
 * 和suit 一起使用
 */
    public static void main(String[] args) {
        //方式1：没增加一个测试，在suite中增加一个
        //junit.textui.TestRunner.run(suite());
        /**
         *  TODO方式2：直接调用带test前缀的函数---结果打脸了
         */
        junit.textui.TestRunner.run(new TestSuite(LargestTest.class));

    }

    /**
     * 测试套件，可以自行决定测试哪些内容
     * @return
     */
    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTest(new LargestTest("testGetLargest"));
       // suite.addTest(new LargestTest("testEmptyOfList"));

        TestSetup wrapper = new TestSetup(suite) {
            protected void setUp() {
                oneTimeSetUp();
            }

            protected void tearDown() {
                oneTimeTearDown();
            }
        };

        return wrapper;
    }

    public static void oneTimeSetUp() {
        System.out.println("整个项目启动前执行");
    }

    public static void oneTimeTearDown() {
        System.out.println("整个项目测试结束后执行");
    }


    @Test
    public void testDetal() throws FileNotFoundException {
        FileInputStream in = new FileInputStream("data.txt");
    }

}