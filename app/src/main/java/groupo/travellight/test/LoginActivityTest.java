package groupo.travellight.test;

import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.KeyEvent;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;

import groupo.travellight.app.LoginActivity;
import groupo.travellight.app.R;

/**
 * Created by Gabriel on 4/13/14.
 * TEST WITH COMMAND: gradlew.bat connectedAndroidTest (windows)
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    LoginActivity activity;
    EditText editLogin, editPassword;
    Instrumentation mInstrumentation;
    public LoginActivityTest(){
        super(LoginActivity.class);


    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        activity = getActivity();

        mInstrumentation = getInstrumentation();

        editLogin = (EditText)activity.findViewById(groupo.travellight.app.R.id.email);
        editPassword = (EditText)activity.findViewById(groupo.travellight.app.R.id.password);
    }

    @SmallTest
    public void testViews(){
        TextView textView = (TextView) activity.findViewById(R.id.textView);
        assertNotNull(textView);
        assertNotNull(editLogin);
        assertNotNull(editPassword);
    }

    public void testOnScreen(){
        ViewAsserts.assertOnScreen(editLogin.getRootView(),editLogin );
        ViewAsserts.assertOnScreen(editPassword.getRootView(),editPassword );


    }

    public void testLoginRequirements(){



        //no @ symbol
        TouchUtils.tapView(this, editLogin);
        getInstrumentation().sendStringSync("123");

        TouchUtils.clickView(this, activity.findViewById(R.id.sign_in_button));
        assertEquals("This email address is invalid", editLogin.getError());


        //no input
        TouchUtils.tapView(this, editLogin);
        for (int i = 0; i <3; i++){
            getInstrumentation().sendCharacterSync(KeyEvent.KEYCODE_DPAD_LEFT);
            getInstrumentation().sendCharacterSync(KeyEvent.KEYCODE_FORWARD_DEL);
        }
         TouchUtils.clickView(this, activity.findViewById(R.id.sign_in_button));
        assertEquals("This field is required", editLogin.getError());

        /*TouchUtils.tapView(this, editLogin);
        //no @ symbol
        sendKeys("brant.unger");
        TouchUtils.clickView(this, activity.findViewById(R.id.sign_in_button));

        editLogin.clearComposingText();
        //TEST PASSWORD
        TouchUtils.tapView(this, editPassword);
        //no input
        sendKeys("");
        TouchUtils.clickView(this, activity.findViewById(R.id.sign_in_button));

        assertEquals("This field is required", editPassword.getError());
        editPassword.clearComposingText();
        TouchUtils.tapView(this, editPassword);
        //password too short
        sendKeys("123");
        TouchUtils.clickView(this, activity.findViewById(R.id.sign_in_button));
        assertEquals("This password is too short", editPassword.getError());
        editPassword.clearComposingText();*/
    }
}