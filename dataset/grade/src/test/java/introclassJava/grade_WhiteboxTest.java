package introclassJava;

import org.junit.Test;

public class grade_WhiteboxTest {
    @Test(timeout = 1000)
    public void test1() throws Exception {
        grade mainClass = new grade();
        String expected = "Student has an A grade";
        int[] input = {90,80,70,60,95};
        org.junit.Assert.assertEquals(expected, mainClass.grade(input));
    }

    @Test(timeout = 1000)
    public void test2() throws Exception {
        grade mainClass = new grade();
        String expected = "Student has an A grade";
        int[] input = {90,80,70,60,90};
        org.junit.Assert.assertEquals(expected, mainClass.grade(input));
    }

    @Test(timeout = 1000)
    public void test3() throws Exception {
        grade mainClass = new grade();
        String expected = "Student has an B grade";
        int[] input = {90,80,70,60,85};
        org.junit.Assert.assertEquals(expected, mainClass.grade(input));
    }

    @Test(timeout = 1000)
    public void test4() throws Exception {
        grade mainClass = new grade();
        String expected = "Student has an B grade";
        int[] input = {90,80,70,60,80};
        org.junit.Assert.assertEquals(expected, mainClass.grade(input));
    }

    @Test(timeout = 1000)
    public void test5() throws Exception {
        grade mainClass = new grade();
        String expected = "Student has an C grade";
        int[] input = {90,80,70,60,75};
        org.junit.Assert.assertEquals(expected, mainClass.grade(input));
    }

    @Test(timeout = 1000)
    public void test6() throws Exception {
        grade mainClass = new grade();
        String expected = "Student has an C grade";
        int[] input = {90,80,70,60,70};
        org.junit.Assert.assertEquals(expected, mainClass.grade(input));
    }

    @Test(timeout = 1000)
    public void test7() throws Exception {
        grade mainClass = new grade();
        String expected = "Student has an D grade";
        int[] input = {90,80,70,60,65};
        org.junit.Assert.assertEquals(expected, mainClass.grade(input));
    }

    @Test(timeout = 1000)
    public void test8() throws Exception {
        grade mainClass = new grade();
        String expected = "Student has an D grade";
        int[] input = {90,80,70,60,60};
        org.junit.Assert.assertEquals(expected, mainClass.grade(input));
    }

    @Test(timeout = 1000)
    public void test9() throws Exception {
        grade mainClass = new grade();
        String expected = "Student has failed the course";
        int[] input = {90,80,70,60,55};
        org.junit.Assert.assertEquals(expected, mainClass.grade(input));
    }
}

