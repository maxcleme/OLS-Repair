package introclassJava;

import org.junit.Test;

public class digits_BlackboxTest {
    @Test(timeout = 1000)
    public void test1() throws Exception {
        digits mainClass = new digits();
        String expected = "4321";
        String input = "1234";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test2() throws Exception {
        digits mainClass = new digits();
        String expected = "678-9";
        String input = "-9876";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test3() throws Exception {
        digits mainClass = new digits();
        String expected = "2720373";
        String input = "3730272";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test4() throws Exception {
        digits mainClass = new digits();
        String expected = "8";
        String input = "8";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test5() throws Exception {
        digits mainClass = new digits();
        String expected = "47398";
        String input = "89374";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }

    @Test(timeout = 1000)
    public void test6() throws Exception {
        digits mainClass = new digits();
        String expected = "6666666";
        String input = "6666666";
        org.junit.Assert.assertEquals(expected, mainClass.digits(input));
    }
}

