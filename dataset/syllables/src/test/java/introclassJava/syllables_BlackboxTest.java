package introclassJava;

import org.junit.Test;

public class syllables_BlackboxTest {
    @Test(timeout = 1000)
    public void test1() throws Exception {
        syllables mainClass = new syllables();
        int expected = 0;
        String input = "khd";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test2() throws Exception {
        syllables mainClass = new syllables();
        int expected = 6;
        String input = "aeiouy";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test3() throws Exception {
        syllables mainClass = new syllables();
        int expected = 5;
        String input = "here,and,there";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test4() throws Exception {
        syllables mainClass = new syllables();
        int expected = 1;
        String input = "bbbbbbb,a";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test5() throws Exception {
        syllables mainClass = new syllables();
        int expected = 0;
        String input = "9876543210";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }

    @Test(timeout = 1000)
    public void test6() throws Exception {
        syllables mainClass = new syllables();
        int expected = 3;
        String input = "1,a,2,e,3,$#@,u";
        org.junit.Assert.assertEquals(expected, mainClass.syllables(input));
    }
}

