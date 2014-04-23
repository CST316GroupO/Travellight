package groupo.travellight.app;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This acts as a shared object for the entire
 * TravelLight app. It is used to share data in-between
 * activities.
 * @author Brant Unger
 * @version 0.1
 */
public class TravelLight extends Application
{
    private ArrayList<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();

    /**
     * Add an event to the current event bag
     * @param map The hashmap of the event to add
     */
    public void addEventToList(HashMap<String, String> map)
    {
        eventList.add(map);
    }

    /**
     * Return the entire event bag as an ArrayList
     * containing a hashmap with Key, Value pair of
     * <String, String>
     * @return Event bag
     */
    public ArrayList<HashMap<String, String>> getEventList()
    {
        return eventList;
    }

    /**
     * Set the entire event bag by doing a deep copy of
     * the input list
     * @param list Shadow list
     */
    public void setEventList(ArrayList<HashMap<String, String>> list)
    {
        eventList = list;
    }
}
