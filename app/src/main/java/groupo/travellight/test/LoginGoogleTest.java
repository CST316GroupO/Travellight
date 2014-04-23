package groupo.travellight.test;

/**
 * Created by Austin on 4/23/14.
 */
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;

import groupo.travellight.app.LoginActivity;
import groupo.travellight.app.LoginGoogle;
import groupo.travellight.app.R;
public class LoginGoogleTest extends ActivityInstrumentationTestCase2<LoginGoogle> {

    LoginGoogle activity;
    EditText gLogin;
    Instrumentation mInstrumentation;
    Button buttonSignin;


    public LoginGoogleTest() {
        super(LoginGoogle.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        activity = getActivity();
        mInstrumentation = getInstrumentation();
        gLogin = (EditText)activity.findViewById(groupo.travellight.app.R.id.btn_sign_in);
    }

    @SmallTest
    public void testViews(){

        assertNotNull(buttonSignin);

    }

    public void testOnScreen(){
        ViewAsserts.assertOnScreen(buttonSignin.getRootView(),buttonSignin);
        TouchUtils.tapView(this, buttonSignin);

    }

}
