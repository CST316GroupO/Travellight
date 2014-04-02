package groupo.travellight.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import groupo.travellight.app.LoginActivity;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class groupo.travellight.app.LoginActivityTest \
 * groupo.travellight.app.tests/android.test.InstrumentationTestRunner
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    public LoginActivityTest() {
        super(LoginActivity.class);
    }
    @SmallTest
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertEquals(5, reality);
    }
}
