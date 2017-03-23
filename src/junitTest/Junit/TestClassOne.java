package Junit;

import junit.framework.TestCase;
import org.junit.*;

/**
 * Name: admin
 * Date: 2017/3/23
 * Time: 14:33
 */
public class TestClassOne extends TestCase {
    public TestClassOne(String method){
        super(method);
    }

    public void testAddition(){
        assertEquals(4, 2+2);
    }

    public void testSubtraction(){
        assertEquals(0, 2-2);
    }
}
