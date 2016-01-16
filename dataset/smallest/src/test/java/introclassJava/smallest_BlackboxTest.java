package introclassJava;

import org.junit.Test;

public class smallest_BlackboxTest {
    @Test(timeout = 1000)
    public void test1() throws Exception {
        smallest mainClass = new smallest();
        int expected =  1 ;
        int[] input = {1,2,3,4};
        org.junit.Assert.assertEquals(expected, mainClass.smallest(input));
    }

    @Test(timeout = 1000)
    public void test2() throws Exception {
        smallest mainClass = new smallest();
        int expected =  1 ;
        int[] input = {4,3,2,1};
        org.junit.Assert.assertEquals(expected, mainClass.smallest(input));
    }

    @Test(timeout = 1000)
    public void test3() throws Exception {
        smallest mainClass = new smallest();
        int expected =  1 ;
        int[] input = {3,4,2,1};
        org.junit.Assert.assertEquals(expected, mainClass.smallest(input));
    }

    @Test(timeout = 1000)
    public void test4() throws Exception {
        smallest mainClass = new smallest();
        int expected =  1 ;
        int[] input = {3,2,4,1};
        org.junit.Assert.assertEquals(expected, mainClass.smallest(input));
    }

    @Test(timeout = 1000)
    public void test5() throws Exception {
        smallest mainClass = new smallest();
        int expected =  1 ;
        int[] input = {1,1,1,1};
        org.junit.Assert.assertEquals(expected, mainClass.smallest(input));
    }

    @Test(timeout = 1000)
    public void test6() throws Exception {
        smallest mainClass = new smallest();
        int expected =  2 ;
        int[] input = {2,2,2,3};
        org.junit.Assert.assertEquals(expected, mainClass.smallest(input));
    }

    @Test(timeout = 1000)
    public void test7() throws Exception {
        smallest mainClass = new smallest();
        int expected =  -1 ;
        int[] input = {0,0,0,-1};
        org.junit.Assert.assertEquals(expected, mainClass.smallest(input));
    }

    @Test(timeout = 1000)
    public void test8() throws Exception {
        smallest mainClass = new smallest();
        int expected =  -1 ;
        int[] input = {0,-1,0,0};
        org.junit.Assert.assertEquals(expected, mainClass.smallest(input));
    }
}

