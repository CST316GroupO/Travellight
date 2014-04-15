package groupo.travellight.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
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
    EventsBag eventsBag;
    ListView listView;
    View view;
    Instrumentation mInstrumentation;

    public EventsBagTest()
    {
        super(EventsBag.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        eventsBag = getActivity();

        mInstrumentation = getInstrumentation();

        listView = (ListView) eventsBag.findViewById(R.id.eventsListView);
        view = listView.getChildAt(2); //get view at position : will be used for testEventsBagRemove
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
    public void testEventsBagRemove()
    {
        TouchUtils.longClickView(this, view);

    }
}