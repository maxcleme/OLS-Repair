package introclassJava;

import org.junit.Test;

public class digits_WhiteboxTest {
    @Test(timeout = 1000)
    public void test1() throws Exception {
        digits mainClass = new digits();
        String expected = "0";
        String input = "0";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test10() throws Exception {
        digits mainClass = new digits();
        String expected = "2668655001";
        String input = "1005568662";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test2() throws Exception {
        digits mainClass = new digits();
        String expected = "91";
        String input = "19";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test3() throws Exception {
        digits mainClass = new digits();
        String expected = "096";
        String input = "690";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test4() throws Exception {
        digits mainClass = new digits();
        String expected = "0253";
        String input = "3520";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test5() throws Exception {
        digits mainClass = new digits();
        String expected = "86723";
        String input = "32768";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test6() throws Exception {
        digits mainClass = new digits();
        String expected = "000215";
        String input = "512000";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test7() throws Exception {
        digits mainClass = new digits();
        String expected = "1506251";
        String input = "1526051";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test8() throws Exception {
        digits mainClass = new digits();
        String expected = "13606904";
        String input = "40960631";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test9() throws Exception {
        digits mainClass = new digits();
        String expected = "025976041";
        String input = "140679520";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }
}

