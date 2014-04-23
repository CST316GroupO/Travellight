package groupo.travellight.test;

import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;

import groupo.travellight.app.NavigationDrawerFragment;
import groupo.travellight.app.R;
import groupo.travellight.app.TripActivity;

/**
 * Created by Gabriel on 4/13/14.
 * TEST WITH COMMAND: gradlew.bat connectedAndroidTest (windows)
 */
public class TripActivityTest extends ActivityInstrumentationTestCase2<TripActivity> {
    TripActivity activity;
    EditText editLogin, editPassword;
    ListView tripDrawer;
    Instrumentation mInstrumentation;
    public TripActivityTest(){
        super(TripActivity.class);


    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        activity = getActivity();

        mInstrumentation = getInstrumentation();
        tripDrawer = (ListView)activity.findViewById(R.id.left_drawer);

    }






}