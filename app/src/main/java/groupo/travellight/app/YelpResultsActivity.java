package groupo.travellight.app;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import groupo.travellight.yelp.*;

/**
 * This class handles calls from the SEARCH_SERVICE
 * and displays results in a listview
 * @author Brant Unger
 * @version 0.4
 */
public class YelpResultsActivity extends ActionBarActivity
{
    //  Constants for custom data resultsAdapter keys
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_DESCRIPTION = "KEY_DESCRIPTION";
    public static final String KEY_RATINGURL = "KEY_RATINGURL";
    public static final String KEY_RATINGINT = "KEY_RATINGINT";
    public static final String KEY_THUMBURL = "KEY_THUMBURL";

    // Results variables
    private CustomAdapter resultsAdapter, drawerAdapter;
    private ArrayList<HashMap<String, String>> sList = new ArrayList<HashMap<String, String>>();
    private JSONResponse jsonResponse = new JSONResponse();

    // Drawer variables
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // View variables
    private ListView list;

    // GPS variables
    private LocationManager locationManager;
    private String provider;
    private Location location;
    private String cityName = "Seattle";

    // Callback when options menu needs to be created
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.yelp_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.action_filter:
                // call detail activity for clicked entry
                return true;
            case R.id.action_search:
                onSearchRequested(); //call search dialog
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Callback on creation of results activity
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        updateGPS();

        /**************************************/
        setContentView(R.layout.activity_yelp_results);

        list = (ListView)findViewById(R.id.list);
        list.setOnItemClickListener(new onListViewItemCLick());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.yelp_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.yelp_left_drawer);

        TravelLight myApp = (TravelLight) getApplication();
        drawerAdapter = new CustomAdapter(this, myApp.getEventList());
        mDrawerList.setAdapter(drawerAdapter);

        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
               R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("Results");
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Event Bag");
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        /************************************************************/

        setTitle("Results"); //set the actionbar title
        handleIntent(getIntent());
    }

    // Callback detected from activity thread
    public void onNewIntent(Intent intent)
    {
        setIntent(intent);
        handleIntent(intent);
    }

    // Callback detected from the search dialog
    // call the search logic
    private void handleIntent(Intent intent)
    {
        // If the resultsAdapter is not null it means it has items in it
        // this means we are re-searching and need to reset the list
        // and resultsAdapter
        if (resultsAdapter != null)
        {
            sList.clear();
            finish();
            startActivity(intent);
        }

        // If it was the action search intent search yelp
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);
            new SearchYelp().execute(query); //execute new thread and call the query
        }
    }

    /**
     * Post the results of the SearchYelp thread
     */
    private void postResults()
    {
        // For every item in the JSON bundle
        // add details from JSON string to a hashmap
        for (int i = 0; i < jsonResponse.getBundleSize(); i++)
        {
            HashMap<String, String> map = new HashMap<String, String>();

            // Yelp result strings, post parsed
            map.put(KEY_NAME, jsonResponse.getBusinessName(i));
            map.put(KEY_DESCRIPTION, jsonResponse.getSnippet(i));
            map.put(KEY_RATINGURL, jsonResponse.getRatingURL(i));
            map.put(KEY_THUMBURL, jsonResponse.getThumbURL(i));
            map.put(KEY_RATINGINT, jsonResponse.getRating(i));

            sList.add(map); //add the hashmap to the list
        }

        resultsAdapter = new CustomAdapter(this, sList); //send the details to the data resultsAdapter
        list.setAdapter(resultsAdapter); //set the resultsAdapter for this activity as the custom resultsAdapter
    }


    private class onListViewItemCLick implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            // call detail activity for clicked entry
            TravelLight myApp = (TravelLight) getApplication();
            myApp.addEventToList(sList.get(position));
            drawerAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Added to event bag ",
                    Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Update location to be used for search
     */
    private void updateGPS()
    {
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        provider = locationManager.getBestProvider(c, false);
        location = locationManager.getLastKnownLocation(provider);

        if (location != null)
        {
            double lng = location.getLongitude();
            double lat = location.getLatitude();
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try
            {
                addresses = gcd.getFromLocation(lat, lng, 1);
                cityName = addresses.get(0).getLocality();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Thread that accesses yelp API via JSON
     *
     * @author Brant Unger
     * @version 0.1
     */
    class SearchYelp extends AsyncTask<String, Void, String>
    {
        String stringJSON;

        /**
         * Functionality called in a seperate thread
         * @param query
         * @return String JSON formatted response
         */
        protected String doInBackground(String... query)
        {
            try
            {
                stringJSON = new Yelp().search(query[0], cityName);
                jsonResponse.setResponse(stringJSON);
                jsonResponse.parseBusiness(); //parse JSON data
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            return stringJSON;
        }

        /**
         * Post the results of the query
         * @param query Search parameter called from above
         */
        protected void onPostExecute(String query)
        {
            postResults();
        }
    }
}
