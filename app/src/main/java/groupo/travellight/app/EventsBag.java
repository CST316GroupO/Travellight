package groupo.travellight.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
    Bag of events listed to user
 */
public class EventsBag extends ActionBarActivity {
    private List<Events> myEvents = new ArrayList<Events>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_bag);

        //create a test events list
        populateEventsList();

        //sort the events list
        Collections.sort(myEvents, new EventRatingComparator());

        //change the sort to be in descending order
        Collections.reverse(myEvents);

        //populate the list
        populateListView();

        //ask if you want to remove item.
        //if yes remove, if no dismiss the long click
        checkForRemove();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
        Create test list of Events class
     */
    private void populateEventsList()
    {
        myEvents.add((new Events("Bungee Jump", 3)));
        myEvents.add((new Events("Skiing", 4)));
        myEvents.add((new Events("Biking", 5)));
        myEvents.add((new Events("Eating", 5)));
        myEvents.add((new Events("Adventuring", 1)));
    }

    /*
        Show the list of events to the user
     */
    private void populateListView()
    {
        ArrayAdapter<Events> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.eventsListView);
        list.setAdapter(adapter);
    }

    /*
        Check whether the user wants to remove the item or not.
     */
    private void checkForRemove()
    {
       ListView list = (ListView) findViewById(R.id.eventsListView);
       final ArrayAdapter<Events> adapter = new MyListAdapter();
       list.setAdapter(adapter);
       list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
       {
           /*
                Made int position final int position
            */
           @Override
           public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l)
           {
               AlertDialog.Builder adb = new AlertDialog.Builder(EventsBag.this);
               adb.setTitle("Are you sure you want to delete this item?");
               adb.setPositiveButton("Yes", new DialogInterface.OnClickListener()
               {
                   /*
                        remove item at the located position and refresh the list view
                    */
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i)
                   {
                       myEvents.remove(position); //remove item at the current position
                       adapter.notifyDataSetChanged(); //refreshing the list view with the changes
                   }
               });

               adb.setNegativeButton("No", new DialogInterface.OnClickListener()
               {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i)
                   {
                       dialogInterface.dismiss();
                   }
               });

               adb.show();
               return false;
           }
       });
    }

    /*
        Be able to list events classes in the listView
     */
    private class MyListAdapter extends  ArrayAdapter<Events>
    {
        public MyListAdapter()
        {
            super(EventsBag.this, R.layout.event_view, myEvents);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            //Check that we have a view to work with, if not create one
            View itemView = convertView;
            if (itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.event_view, parent, false);
            }

            //Find which event to work with
            Events currentEvent = myEvents.get(position);

            //Name:
            //possible bug with itemView.findViewById(R.id.eventName) where it can throw a null pointer exception
            //bug fixed with assert itemView != null statement
            assert itemView != null;
            TextView eventNameText = (TextView) itemView.findViewById(R.id.event_Name);
            eventNameText.setText(currentEvent.getName());

            //Rating:
            TextView eventRatingText = (TextView) itemView.findViewById(R.id.event_Rating);
            eventRatingText.setText("" + currentEvent.getRating());

            return itemView;
        }
    }

    /*
        comparator class to sort events based on rating
     */
    private class EventRatingComparator implements Comparator<Events>
    {
        /*
            the compare method for the events array list
         */
        @Override
        public int compare(Events event1, Events event2)
        {
            int result = Float.compare(event1.getRating(), event2.getRating());
            return result;
        }
    }

}
