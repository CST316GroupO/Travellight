package groupo.travellight.test;

import android.test.InstrumentationTestCase;

import junit.framework.TestCase;

public class ExampleTest extends TestCase {
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertNotSame(expected, reality);
    }
}