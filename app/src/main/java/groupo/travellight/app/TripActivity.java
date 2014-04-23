package groupo.travellight.app;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import groupo.travellight.yelp.*;
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

import groupo.travellight.yelp.CustomAdapter;

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
    private String currentTrip;
    private int tripPosition;

    private File[] listOfFiles;
    //Calendar
    public GregorianCalendar month, itemmonth;// calendar instances.

    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
    // marker.
    public ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker
    ArrayList<String> event;
    ListView rLayout;
    ArrayList<String> date;
    ArrayList<String> desc;
    private GridView gridview;
    private String selectedGridDate;
    private int currentView = -1;
    private View currentV;
    private View previousV;
    private int previousView = -1;
    private DrawerLayout mDrawerLayout1;
    private ListView mDrawerList1;
    private CustomAdapter drawerAdapter1;
    private ActionBarDrawerToggle mDrawerToggle1;
    private Activity thisActivity;
    ArrayList<HashMap<String, String>> sList = new ArrayList<HashMap<String, String>>();
    private TravelLight myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Calendar
        setContentView(R.layout.activity_trip);
        //setContentView(R.layout.activity_trip);
        Locale.setDefault(Locale.US);
        myApp = (TravelLight) getApplication();
        //drawer 2
         sList = new ArrayList<HashMap<String, String>>();
        mDrawerLayout1 = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList1 = (ListView) findViewById(R.id.yelp_left_drawer);
        thisActivity = this;
        myApp = (TravelLight) getApplication();
        drawerAdapter1 = new CustomAdapter(this, myApp.getEventList());
        if (drawerAdapter1 != null){
        mDrawerList1.setAdapter(drawerAdapter1);

        }
        mDrawerList1.setOnItemClickListener(new DrawerItemClickListener1());
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle1 = new ActionBarDrawerToggle(this, mDrawerLayout1,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout1.setDrawerListener(mDrawerToggle1);
        //drawer 2 end
        rLayout = (ListView) findViewById(R.id.event);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();

        adapter = new CalendarAdapter(this, month);

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        handler = new Handler();
        handler.post(calendarUpdater);
        TextView txtV = (TextView) findViewById(R.id.textView);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x; // int screenWidth = display.getWidth(); on API < 13
        int screenHeight = size.y; // int screenHeight = display.getHeight(); on API <13

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Math.round(screenWidth/7), ViewGroup.LayoutParams.WRAP_CONTENT); // This should set the width and height of the TextView
        lp.setMargins(0, 40, 0, 0); // This serves as the settings for the left and top position

        txtV.setLayoutParams(lp);
        txtV = (TextView) findViewById(R.id.textView2);
        lp = new RelativeLayout.LayoutParams(Math.round(screenWidth/7), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(Math.round(screenWidth/7), 40, 0, 0);
        txtV.setLayoutParams(lp);
        txtV = (TextView) findViewById(R.id.textView3);
        lp = new RelativeLayout.LayoutParams(Math.round(screenWidth/7), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(Math.round(2 * screenWidth / 7), 40, 0, 0);
        txtV.setLayoutParams(lp);
        txtV = (TextView) findViewById(R.id.textView4);
        lp = new RelativeLayout.LayoutParams(Math.round(screenWidth/7), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(Math.round(3 * screenWidth / 7), 40, 0, 0);
        txtV.setLayoutParams(lp);
        txtV = (TextView) findViewById(R.id.textView5);
        lp = new RelativeLayout.LayoutParams(Math.round(screenWidth/7), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(Math.round(4 * screenWidth / 7), 40, 0, 0);
        txtV.setLayoutParams(lp);
        txtV = (TextView) findViewById(R.id.textView6);
        lp = new RelativeLayout.LayoutParams(Math.round(screenWidth/7), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(Math.round(5 * screenWidth / 7), 40, 0, 0);
        txtV.setLayoutParams(lp);
        txtV = (TextView) findViewById(R.id.textView7);
        lp = new RelativeLayout.LayoutParams(Math.round(screenWidth/7), ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(Math.round(6 * screenWidth / 7), 40, 0, 0);
        txtV.setLayoutParams(lp);
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
                previousView = currentView;
                currentView = position;
                previousV = currentV;
                currentV = v;
                myApp.loadCalendar(currentTrip, mEmail);
                selectedGridDate = CalendarAdapter.dayString
                        .get(position);
                if (selectedGridDate != null){
                    if (myApp.GetEventListToCalendar(selectedGridDate) != null){
                    CustomAdapter drawerAdapter2 = new CustomAdapter(thisActivity, myApp.GetEventListToCalendar(selectedGridDate));

                    rLayout.setAdapter(drawerAdapter2);
                    }
                    else{
                        CustomAdapter drawerAdapter2 = new CustomAdapter(thisActivity, new ArrayList<HashMap<String, String>>());
                        rLayout.setAdapter(drawerAdapter2);
                    }


                }
                desc = new ArrayList<String>();
                date = new ArrayList<String>();
                adapter.setSelected(v);

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
                if (mDrawerLayout.isDrawerOpen(mDrawerList)){
                getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                Toast.makeText(getApplicationContext(), "Select or create a trip.", Toast.LENGTH_LONG).show();
                }
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
    public void clickItem(View v,
                            int position) {


        desc = new ArrayList<String>();
        date = new ArrayList<String>();
        adapter.setSelected(v);
        selectedGridDate = CalendarAdapter.dayString
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
        adapter.setSelected(v);


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
    private class DrawerItemClickListener1 implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

            if (selectedGridDate != null){


            sList.add(myApp.getEventList().get(position));
            CustomAdapter drawerAdapter2 = new CustomAdapter(thisActivity, sList);
            rLayout.setAdapter(drawerAdapter2);
                adapter.setV(currentV);
                myApp.addEventListToCalendar(sList, selectedGridDate);
                myApp.saveCalendar(currentTrip, mEmail);
                File log = new File(getApplicationContext().getFilesDir().getPath().toString() + "/" + mEmail + "/" + currentTrip + "/" + "events.txt");

                try{
                    if(!log.exists()){

                        log.createNewFile();
                    }

                    FileWriter fileWriter = new FileWriter(log, true);

                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(selectedGridDate);
                    bufferedWriter.newLine();
                    bufferedWriter.close();


                } catch(IOException e) {

                }

                try {
                    selectItem(tripPosition);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(thisActivity, "Select a day to add events to.", Toast.LENGTH_LONG).show();
            }


        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) throws IOException {
        currentTrip = trips.get(position);
        myApp.loadCalendar(currentTrip, mEmail);
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(trips.get(position));
        mDrawerLayout.closeDrawer(mDrawerList);
        PrintWriter writer = null;

        writer = new PrintWriter(getApplicationContext().getFilesDir().getPath().toString() + "/" + mEmail + "/" + "recent.txt", "UTF-8");

        writer.println(trips.get(position).toString());
        writer.close();

        Utility.startDates.clear();
        File f = new File(getApplicationContext().getFilesDir().getPath().toString() + "/" + mEmail + "/" + trips.get(position).toString() + "/" + "events.txt");

        if (f.exists()){
            BufferedReader in = new BufferedReader(new FileReader(f));

            String x = in.readLine();
            if (myApp.getDates() != null){
            for  (int i = 0; i <myApp.getDates().size();i ++){
                if (!Utility.startDates.contains(myApp.getDates().get(i))){
                Utility.startDates.add(myApp.getDates().get(i));
            }
            }



        if (!Utility.startDates.isEmpty() && tripPosition!= position){
            String[] separatedTime = Utility.startDates.get(0).split("-");
            Log.d("startyear", Integer.toString(Integer.parseInt(separatedTime[0])));
            Log.d("startyear1", Integer.toString(Integer.valueOf(separatedTime[0])));
        month.set(Integer.parseInt(separatedTime[0]), Integer.parseInt(separatedTime[1])-1, 1);
        }
        tripPosition = position;
        refreshCalendar();


    }
    }
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
    public void gotoEvent(MenuItem item) throws FileNotFoundException, UnsupportedEncodingException {
        //Intent intent = new Intent(this,EventsBag.class);
        //startActivity(intent);
        if(mDrawerLayout1.isDrawerOpen(mDrawerList1)){
            mDrawerLayout1.closeDrawer(mDrawerList1);
        }
        else{
        mDrawerLayout1.openDrawer(mDrawerList1);
        }

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
                Intent intent = new Intent(this,FriendsListActivity.class);
                intent.putExtra("LOGIN_EMAIL", mEmail);
                intent.putExtra("TRIP_NAME", mTitle);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
    protected void setNextMonth() {
        selectedGridDate = null;
        currentView = -1;
        adapter.setV(null);
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
        selectedGridDate = null;
        currentView = -1;
        adapter.setV(null);
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
        drawerAdapter1.notifyDataSetChanged();
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
            if (currentV != null){
                Log.d("=====One====", event.toString());

            }
            adapter.notifyDataSetChanged();
            if (currentV != null){
                Log.d("=====Two====", event.toString());

            }

            //adapter.clickFocus();
        }
    };

}
