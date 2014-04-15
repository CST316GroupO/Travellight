package groupo.travellight.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.TextView;


import groupo.travellight.app.PackingListActivity;
import groupo.travellight.app.R;

/**
 * Packing Test
 * A test for the PackingListActivity 
 */
public class PackingTest extends ActivityInstrumentationTestCase2<PackingListActivity> {

    PackingListActivity activity;
    Button buttonAdd;
    Button buttonSave;
    Instrumentation mInstrumentation;

    public PackingTest(){
        super(PackingListActivity.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        activity = getActivity();

        mInstrumentation = getInstrumentation();

        buttonAdd = (Button)activity.findViewById(R.id.button_addPacking);
        buttonSave = (Button)activity.findViewById(R.id.button_savePacking);
    }

    //Test if view is availible(?)
    @SmallTest
    public void testViewPacking(){
        TextView textView = (TextView) activity.findViewById(R.id.textView);
        assertNotNull(textView);
    }

    //Test if these elements are present
    public void testOnScreen(){
        ViewAsserts.assertOnScreen(buttonAdd.getRootView(), buttonAdd);
        ViewAsserts.assertOnScreen(buttonSave.getRootView(),buttonSave );
    }
}


