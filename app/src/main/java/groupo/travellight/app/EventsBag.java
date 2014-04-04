package groupo.travellight.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
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

        //populate the list
        populateListView();
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
        myEvents.add((new Events("Bungee Jump")));
        myEvents.add((new Events("Skiing")));
        myEvents.add((new Events("Biking")));
        myEvents.add((new Events("Eating")));
        myEvents.add((new Events("Adventuring")));
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
            TextView eventNameText = (TextView) itemView.findViewById(R.id.event_Name);
            eventNameText.setText(currentEvent.getName());

            return itemView;
        }
    }

}
