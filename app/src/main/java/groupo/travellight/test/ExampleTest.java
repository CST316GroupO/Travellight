package groupo.travellight.test;

        import android.test.ActivityInstrumentationTestCase2;
        import android.test.InstrumentationTestCase;

        import groupo.travellight.app.LoginActivity;

public class ExampleTest extends InstrumentationTestCase {


    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}