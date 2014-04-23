package groupo.travellight.app;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class TripActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks{
    private ArrayList<String> trips = new ArrayList();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String mEmail;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private BufferedReader bRead;
    private File folder;

    private File[] listOfFiles;
    //Calendar
    public GregorianCalendar month, itemmonth;// calendar instances.

    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
    // marker.
    public ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker
    ArrayList<String> event;
    LinearLayout rLayout;
    ArrayList<String> date;
    ArrayList<String> desc;
    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Calendar
        setContentView(R.layout.activity_trip);
        //setContentView(R.layout.activity_trip);
        Locale.setDefault(Locale.US);

        rLayout = (LinearLayout) findViewById(R.id.text);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();

        adapter = new CalendarAdapter(this, month);

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        handler = new Handler();
        handler.post(calendarUpdater);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

        previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // removing the previous view if added
                if (((LinearLayout) rLayout).getChildCount() > 0) {
                    ((LinearLayout) rLayout).removeAllViews();
                }
                desc = new ArrayList<String>();
                date = new ArrayList<String>();
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous month on clicking offdays.
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);

                for (int i = 0; i < Utility.startDates.size(); i++) {
                    if (Utility.startDates.get(i).equals(selectedGridDate)) {
                        desc.add(Utility.nameOfEvent.get(i));
                    }
                }

                if (desc.size() > 0) {
                    for (int i = 0; i < desc.size(); i++) {
                        TextView rowTextView = new TextView(TripActivity.this);

                        // set some properties of rowTextView or something
                        rowTextView.setText(desc.get(i));
                        rowTextView.setTextColor(Color.BLACK);

                        // add the textview to the linearlayout
                        rLayout.addView(rowTextView);

                    }

                }

                desc = null;

            }

        });

        Intent in = getIntent();
        Bundle b = in.getExtras();
        String email = "";
        if (b == null){
            email = "testEmail@test.com";
        }
        else{
            email = b.getString("LOGIN_EMAIL");
        }
        mEmail = email;

        folder = new File(getApplicationContext().getFilesDir().getPath().toString() + "/" + email);

        listOfFiles = folder.listFiles();
        if (listOfFiles != null){
            for (int i = 0; i < listOfFiles.length; i++)
            {

                if (listOfFiles[i].isDirectory())
                {
                    trips.add(listOfFiles[i].getName());

                }
            }
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.popup_layout, trips));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);

                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                Toast.makeText(getApplicationContext(), "Select or create a trip.", Toast.LENGTH_LONG).show();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        if (listOfFiles != null){
            if (listOfFiles.length <= 1){
                mDrawerLayout.openDrawer(mDrawerList);
                showPopUp();
                Toast.makeText(getApplicationContext(), "Create a new trip.", Toast.LENGTH_LONG).show();
            }
        }
        String t = "";
        File f = new File(getApplicationContext().getFilesDir().getPath().toString() + "/" + mEmail + "/" + "recent.txt");
        if (f.exists()){
            try {
                bRead = new BufferedReader(new FileReader(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                t = bRead.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!t.equals("Trips")){
                Log.d("================trip name==============","..." + t);
                try {
                    selectItem(trips.indexOf(t));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

            try {
                selectItem(position);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) throws IOException {


        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(trips.get(position));
        mDrawerLayout.closeDrawer(mDrawerList);
        PrintWriter writer = null;

        writer = new PrintWriter(getApplicationContext().getFilesDir().getPath().toString() + "/" + mEmail + "/" + "recent.txt", "UTF-8");

        writer.println(trips.get(position).toString());
        writer.close();
        Utility.nameOfEvent.clear();
        Utility.startDates.clear();
        File f = new File(getApplicationContext().getFilesDir().getPath().toString() + "/" + mEmail + "/" + trips.get(position).toString() + "/" + "events.txt");
        Log.d("================trips POS==============",trips.get(position).toString());
        if (f.exists()){
            BufferedReader in = new BufferedReader(new FileReader(f));

            String x = in.readLine();
            Utility.nameOfEvent.add(x);
            if (x == null){
                Log.d("================NULLL==============","...");
            }

            Log.d("first line","..." + x);
            Utility.startDates.add(Utility.getDate(Long.parseLong(in.readLine())));

        }
        else{
            writer = new PrintWriter(getApplicationContext().getFilesDir().getPath().toString() + "/" + mEmail + "/" + trips.get(position).toString() + "/" + "events.txt", "UTF-8");
            writer.println("Biking");
            writer.println("1398051792000");
            writer.close();
        }
        Log.d("startyear", Integer.toString(adapter.getStartYear()));
        if (!Utility.startDates.isEmpty()){
            String[] separatedTime = Utility.startDates.get(0).split("-");
        month.set(Integer.parseInt(separatedTime[0]), Integer.parseInt(separatedTime[0])-1, 1);
        }
        refreshCalendar();

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(title);
    }

    public void openPopup(MenuItem item){
        showPopUp();
        Toast.makeText(this, "Enter trip name.", Toast.LENGTH_LONG).show();
    }
    public void gotoEvent(MenuItem item){
        Intent intent = new Intent(this,EventsBag.class);
        startActivity(intent);
    }
    //comment
    public void gotoPacking(MenuItem item){
        Intent intent = new Intent(this,PackingListActivity.class);
        //Pass the Email Field
        intent.putExtra("LOGIN_EMAIL", mEmail);
        //Pass the Trip Field
        intent.putExtra("TRIP_NAME", mTitle);

        startActivity(intent);
    }
    public void removeTrip(MenuItem item){
        if (!getActionBar().getTitle().equals("Trips")){
            showRemove();
            Toast.makeText(this, "This will permanently remove your trip.", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "You must select a Trip to remove.", Toast.LENGTH_LONG).show();
        }
    }
    private void showPopUp() {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Create Trip");
        final EditText input = new EditText(this);
        input.setSingleLine();
        helpBuilder.setView(input);
        //Save button
        helpBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                File f = new File(getApplicationContext().getFilesDir().getPath().toString() + "/" + mEmail + "/" + input.getText().toString());
                if (!f.exists()){

                    f.mkdir();
                    trips.add(input.getText().toString());

                    try {
                        selectItem(trips.size() - 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mDrawerList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.popup_layout, trips));


                }
                else{
                    Toast.makeText(getApplicationContext(), "Trip name exists! Try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
        //Cancel button
        helpBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, just close the dialog box
            }
        });
        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

    }
    private void showRemove() {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Remove trip " + getActionBar().getTitle() + "?");


        //Save button
        helpBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                File f = new File(getApplicationContext().getFilesDir().getPath().toString() + "/" + mEmail + "/" + getActionBar().getTitle());
                if (f.exists()){

                    f.delete();
                    trips.remove(getActionBar().getTitle());
                    getActionBar().setTitle("Trips");
                    mTitle = "Trips";
                    PrintWriter writer = null;

                    try {
                        writer = new PrintWriter(getApplicationContext().getFilesDir().getPath().toString() + "/" + mEmail + "/" + "recent.txt", "UTF-8");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    writer.println("Trips");
                    writer.close();

                    Utility.nameOfEvent.clear();
                    Utility.startDates.clear();

                    refreshCalendar();
                    mDrawerList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.popup_layout, trips));
                    mDrawerLayout.openDrawer(mDrawerList);


                }
                else{
                    Toast.makeText(getApplicationContext(), "Trip doesn't exist.", Toast.LENGTH_LONG).show();
                }
            }
        });
        //Cancel button
        helpBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, just close the dialog box
            }
        });
        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trip, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }
        Log.d("itemid", Integer.toString(item.getItemId()));
        Log.d("yelp", Integer.toString(R.id.menu_yelpsearch));
        switch (item.getItemId()) {
            case R.id.menu_yelpsearch:
                onSearchRequested(); //call search dialog
                return true;
            case R.id.action_friends:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new FriendsList());//change add to replace?
                ft.addToBackStack(null);
                ft.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        Log.d("month", Integer.toString(month.get(GregorianCalendar.MONTH)));
        Log.d("year", Integer.toString(month.get(GregorianCalendar.YEAR)));
    }

    protected void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

    }
    public void refreshCalendar() {
        TextView title = (TextView) findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }
    @Override
    public void onResume(){
        super.onResume();
        File folder = new File(getApplicationContext().getFilesDir().getPath().toString() + "/" + mEmail);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null){
            for (int i = 0; i < listOfFiles.length; i++)
            {

                if (listOfFiles[i].isDirectory())
                {
                    if (!trips.contains(listOfFiles[i].getName())) {
                        trips.add(listOfFiles[i].getName());
                    }

                }
            }
        }

        ArrayAdapter adapter =(ArrayAdapter) mDrawerList.getAdapter();//mDrawerAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();

            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String itemvalue;
            event = Utility.readCalendarEvent(TripActivity.this);
            Log.d("=====Event====", event.toString());
            Log.d("=====Date ARRAY====", Utility.startDates.toString());

            for (int i = 0; i < Utility.startDates.size(); i++) {
                Log.d("=====Date ARRAY====", Utility.startDates.get(i).toString());
                itemvalue = df.format(itemmonth.getTime());
                itemmonth.add(GregorianCalendar.DATE, 1);
                items.add(Utility.startDates.get(i).toString());
            }
            adapter.setItems(items);
            adapter.notifyDataSetChanged(); 
            //adapter.clickFocus();
        }
    };

}
