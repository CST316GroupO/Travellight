package groupo.travellight.test;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import groupo.travellight.app.EventsBag;
import groupo.travellight.app.Events;

/*
    Created by Tommy Pham on 4/15/14
    TEST WITH COMMAND: gradlew.bat connectedAndroidTest (windows)
 */
public class EventsBagTest extends ActivityInstrumentationTestCase2<EventsBag>
{
    private EventsBag eventsBag;
    private ListView listView;
    private View view;
    private Instrumentation mInstrumentation;
    private Button posButton;
    private Button negButton;

    public EventsBagTest()
    {
        super(EventsBag.class);
    }

    /*
        setup for the events bag testing
     */
    @Override
    protected void setUp() throws Exception{
        super.setUp();
        eventsBag = getActivity();

        mInstrumentation = getInstrumentation();

        listView = (ListView) eventsBag.findViewById(groupo.travellight.app.R.id.eventsListView);
    }

    /*
        Test to see if it shows the listview of the events bag
     */
    public void testEventsBagView()
    {
        assertNotNull(listView);
    }

    /*
        Test if the remove is working
             may need to break up method to separate AlertDialog test from this
     */
    public void testDialogAlertPopUp()
    {
        view = listView.getChildAt(2); //get view at position
        TouchUtils.longClickView(this, view);

    }
}