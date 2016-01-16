package introclassJava;

import org.junit.Test;

public class median_WhiteboxTest {
    @Test(timeout = 1000)
    public void test1() throws Exception {
        median mainClass = new median();
        int expected =  0 ;
        int[] input = {0,0,0};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }

    @Test(timeout = 1000)
    public void test2() throws Exception {
        median mainClass = new median();
        int expected =  1 ;
        int[] input = {2,0,1};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }

    @Test(timeout = 1000)
    public void test3() throws Exception {
        median mainClass = new median();
        int expected =  0 ;
        int[] input = {0,0,1};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }

    @Test(timeout = 1000)
    public void test4() throws Exception {
        median mainClass = new median();
        int expected =  0 ;
        int[] input = {0,1,0};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }

    @Test(timeout = 1000)
    public void test5() throws Exception {
        median mainClass = new median();
        int expected =  1 ;
        int[] input = {0,2,1};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }

    @Test(timeout = 1000)
    public void test6() throws Exception {
        median mainClass = new median();
        int expected =  2 ;
        int[] input = {0,2,3};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }
}

