package introclassJava;

import org.junit.Test;

public class checksum_WhiteboxTest {
    @Test(timeout = 1000)
    public void test1() throws Exception {
        checksum mainClass = new checksum();
        char expected = ']';
        String input = "hello world!";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test10() throws Exception {
        checksum mainClass = new checksum();
        char expected = ',';
        String input = "We the people...";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test2() throws Exception {
        checksum mainClass = new checksum();
        char expected = '7';
        String input = "qwertyuiopasdfghjkl";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test3() throws Exception {
        checksum mainClass = new checksum();
        char expected = 'K';
        String input = "A*";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test4() throws Exception {
        checksum mainClass = new checksum();
        char expected = 'F';
        String input = "O Brother Where Art Thou?";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test5() throws Exception {
        checksum mainClass = new checksum();
        char expected = '4';
        String input = "~!@#$%^&*()_+";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test6() throws Exception {
        checksum mainClass = new checksum();
        char expected = '@';
        String input = "100 Degrees and sunny";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test7() throws Exception {
        checksum mainClass = new checksum();
        char expected = 'V';
        String input = "?? water the plants !!";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test8() throws Exception {
        checksum mainClass = new checksum();
        char expected = 'X';
        String input = "12894.389239";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test9() throws Exception {
        checksum mainClass = new checksum();
        char expected = '#';
        String input = "! word 12 :)";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }
}

