package com.example.stelios.songle;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class StringSimilarityTest {

    // Test with the same Strings, threshold 0.66
    @Test
    public void sameStrings() throws Exception {
        assertTrue("Similar Strings",StringSimilarity.similarity("Bohemian Rhapsody","Bohemian Rhapsody")>0.66);
        assertEquals(StringSimilarity.similarity("Bohemian Rhapsody","Bohemian Rhapsody"),1.0);
    }

    // Test with similar Strings, threshold 0.66
    @Test
    public void similarStrings() throws Exception {
        assertTrue("Similar Strings",StringSimilarity.similarity("Bohemian Rhapsody","Boemian Rhsody")>0.66);
        assertTrue("Similar Strings",StringSimilarity.similarity("Nothing Compares 2 U","Nothing Compares to you")>0.66);


    }

    // Test with different String not even similar, threshold 0.66
    @Test
    public void differentStrings() throws Exception {
        assertFalse("Similar Strings",StringSimilarity.similarity("Bohemian Rhapsody","Song 2")>0.66);
        assertFalse("Similar Strings",StringSimilarity.similarity("Nothing Compares 2 U","Everything Compares to you")>0.66);
    }
}