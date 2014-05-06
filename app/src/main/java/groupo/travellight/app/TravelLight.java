package groupo.travellight.app;

import android.app.Application;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private HashMap<String,ArrayList<HashMap<String, String>>> calendarList = new  HashMap<String,ArrayList<HashMap<String, String>>>();
    private String currentTrip;
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

    public void addEventListToCalendar(ArrayList<HashMap<String, String>> list, String date)
    {
        calendarList.put(date, list);
    }
    public void setEventListToCalendar(ArrayList<HashMap<String, String>> list, String date)
    {
        calendarList.remove(date);
        calendarList.put(date, list);
    }
    /**
     * Gets the currently active trip
     * @return Trip location
     */
    public String getCurrentTrip()
    {
        return currentTrip;
    }

    public ArrayList<HashMap<String, String>> GetEventListToCalendar(String date)
    {
        return calendarList.get(date);
    }

    public void saveCalendar(String tripName, String email)
    {
        try {


            FileOutputStream fos = new FileOutputStream(new File(getApplicationContext().getFilesDir().getPath().toString() + "/" + email + "/" + tripName + "/" + "events_hash.txt"));
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(calendarList);
            os.close();
        }
        catch (IOException e)
        {

        }
    }
    public ArrayList<String> getDates()
{
    ArrayList<String> dates = new ArrayList<String>();
    for ( String key : calendarList.keySet() ) {
        dates.add(key);
    }

    return dates;
}

    public void loadCalendar(String tripName, String email) {
       calendarList = new HashMap<String,ArrayList<HashMap<String, String>>>();
        try {
            FileInputStream fis = new FileInputStream(new File(getApplicationContext().getFilesDir().getPath().toString() + "/" + email + "/" + tripName + "/" + "events_hash.txt"));
            ObjectInputStream is = new ObjectInputStream(fis);
            calendarList = (HashMap<String,ArrayList<HashMap<String, String>>>) is.readObject();
            is.close();
        }
        catch (Exception e)
        {

        }
    }
    //commit

    /**
     * Set the currently active trip
     * @param currentTrip Active trip location
     */
    public void setCurrentTrip(String currentTrip)
    {
        this.currentTrip = currentTrip;
    }
}