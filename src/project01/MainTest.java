package project01;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;



public class MainTest {

    public MainTest(){}

    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testValidatezip(){
        String zip = "64468";
        boolean expected = true;
        boolean result = Project01.validateZip(zip);
        assertEquals(expected, result);
    }

    @Test
    public void testValidatezipFail(){
        String zip = "s64469";
        boolean expected = false;
        boolean result = Project01.validateZip(zip);
        assertEquals(expected, result);
    }

    @Test
    public void testValidateZipFaillength(){
        String long_zip = "129498029";
        boolean expected = false;
        boolean result = Project01.validateZip(long_zip);
        assertEquals(expected,result);

    }

        @Test
    public void testValidateMiles(){
        String miles = "60";
        boolean expected = true;
        boolean result = Project01.validateMiles(miles);
        assertEquals(expected, result);
    }

    @Test
    public void testValidateMilefail(){
        String miles = "0";
        boolean expected = false;
        boolean result = Project01.validateMiles(miles);
        assertEquals(expected, result);
    }

    @Test
    public void testValidateMileFailString(){
        String miles = "fail";
        boolean expected = false;
        boolean result = Project01.validateMiles(miles);
        assertEquals(expected, result);
    }


    
}
