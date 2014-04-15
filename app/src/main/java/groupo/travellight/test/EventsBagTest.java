package groupo.travellight.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;

import groupo.travellight.app.EventsBag;
import groupo.travellight.app.Events;

/*
    Created by Tommy Pham on 4/15/14
    TEST WITH COMMAND: gradlew.bat connectedAndroidTest (windows)
 */
public class EventsBagTest extends ActivityInstrumentationTestCase2<EventsBag>
{
    private EventsBag eventsBag;
    private Instrumentation mInstrumentation;

    public EventsBagTest()
    {
        super(EventsBag.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        eventsBag = getActivity();

        mInstrumentation = getInstrumentation();
    }

    /*
        Test to see if it shows the listview of the events bag
     */
    public void testEventsBagView()
    {
        
    }

    /*
        Test if the remove is working
     */
    public void testEventsBagRemove()
    {

    }
}