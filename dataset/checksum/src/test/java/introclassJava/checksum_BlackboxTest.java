package introclassJava;

import org.junit.Test;

public class checksum_BlackboxTest {
    @Test(timeout = 1000)
    public void test1() throws Exception {
        checksum mainClass = new checksum();
        char expected = '-';
        String input = "1234567890";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test2() throws Exception {
        checksum mainClass = new checksum();
        char expected = 'I';
        String input = "abcefghi";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test3() throws Exception {
        checksum mainClass = new checksum();
        char expected = 'K';
        String input = ")(*&^%$#";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test4() throws Exception {
        checksum mainClass = new checksum();
        char expected = 'E';
        String input = "abc 123 %^&";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test5() throws Exception {
        checksum mainClass = new checksum();
        char expected = '2';
        String input = "~+{\"s1213skane";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }

    @Test(timeout = 1000)
    public void test6() throws Exception {
        checksum mainClass = new checksum();
        char expected = '\'';
        String input = "ASDF_1234";
        org.junit.Assert.assertEquals(expected, mainClass.checksum(input));
    }
}

