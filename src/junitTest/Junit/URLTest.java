package Junit;

import junit.framework.TestCase;
import org.junit.*;
import org.junit.Test;

import java.util.Timer;

/**
 * Name: admin
 * Date: 2017/3/24
 * Time: 9:57
 */
public class URLTest extends TestCase {

    public URLTest(String name){
        super(name);
    }

    @Test
    public void testURLFilter(){
        Timer timer = new Timer();

        String naughty_url = "http://www.baidu.com";

       // URLFilter
    }
}
