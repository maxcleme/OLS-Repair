package introclassJava;

import org.junit.Test;

public class median_BlackboxTest {
    @Test(timeout = 1000)
    public void test1() throws Exception {
        median mainClass = new median();
        int expected =  6 ;
        int[] input = {2,6,8};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }

    @Test(timeout = 1000)
    public void test2() throws Exception {
        median mainClass = new median();
        int expected =  6 ;
        int[] input = {2,8,6};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }

    @Test(timeout = 1000)
    public void test3() throws Exception {
        median mainClass = new median();
        int expected =  6 ;
        int[] input = {6,2,8};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }

    @Test(timeout = 1000)
    public void test4() throws Exception {
        median mainClass = new median();
        int expected =  6 ;
        int[] input = {6,8,2};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }

    @Test(timeout = 1000)
    public void test5() throws Exception {
        median mainClass = new median();
        int expected =  6 ;
        int[] input = {8,2,6};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }

    @Test(timeout = 1000)
    public void test6() throws Exception {
        median mainClass = new median();
        int expected =  6 ;
        int[] input = {8,6,2};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }

    @Test(timeout = 1000)
    public void test7() throws Exception {
        median mainClass = new median();
        int expected =  9 ;
        int[] input = {9,9,9};
        org.junit.Assert.assertEquals(expected, mainClass.median(input));
    }
}

