package groupo.travellight.yelp;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This singleton acts as an interface between yelp results
 * and other activities to share data.
 * @author Brant Unger
 * @version 0.1
 */
public class EventSingleton extends Application
{
    //list of events
    private ArrayList<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();
    private int size = 0;

    public void addEvent(HashMap<String, String> map)
    {
        eventList.add(map);
        size = eventList.size();
    }

    public ArrayList<HashMap<String, String>> getEventList()
    {
        return eventList;
    }


}
