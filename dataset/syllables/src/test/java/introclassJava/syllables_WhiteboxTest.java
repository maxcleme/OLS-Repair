package introclassJava;

import org.junit.Test;

public class syllables_WhiteboxTest {
    @Test(timeout = 1000)
    public void test1() throws Exception {
        syllables mainClass = new syllables();
        int expected = 1;
        String input = "a";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test10() throws Exception {
        syllables mainClass = new syllables();
        int expected = 3;
        String input = "snow,white,123,><";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test2() throws Exception {
        syllables mainClass = new syllables();
        int expected = 2;
        String input = "i,o";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test3() throws Exception {
        syllables mainClass = new syllables();
        int expected = 0;
        String input = "mnhd";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test4() throws Exception {
        syllables mainClass = new syllables();
        int expected = 3;
        String input = "hello,world";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test5() throws Exception {
        syllables mainClass = new syllables();
        int expected = 5;
        String input = "aeiou";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test6() throws Exception {
        syllables mainClass = new syllables();
        int expected = 6;
        String input = "seasons,greetings!";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test7() throws Exception {
        syllables mainClass = new syllables();
        int expected = 4;
        String input = "which,witch,is,which?";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test8() throws Exception {
        syllables mainClass = new syllables();
        int expected = 0;
        String input = "!@#$%^,";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test9() throws Exception {
        syllables mainClass = new syllables();
        int expected = 0;
        String input = "123zdh";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }
}

