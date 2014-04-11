package groupo.travellight.app;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import groupo.travellight.yelp.CustomAdapter;
import groupo.travellight.yelp.JSONResponse;
import groupo.travellight.yelp.Yelp;

/**
 * This class handles calls from the SEARCH_SERVICE
 * and displays results in a listview
 * @author Brant Unger
 * @version 0.3
 */
public class YelpResultsActivity extends ListActivity {
    private JSONResponse jsonResponse = new JSONResponse();

    //  Constants for custom data adapter keys
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_DESCRIPTION = "KEY_DESCRIPTION";
    public static final String KEY_RATINGURL = "KEY_RATINGURL";
    public static final String KEY_RATINGINT = "KEY_RATINGINT";
    public static final String KEY_THUMBURL = "KEY_THUMBURL";

    CustomAdapter adapter;

    // Callback on creation of results activity
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
        ArrayList<HashMap<String, String>> sList = new ArrayList<HashMap<String, String>>();

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

        adapter = new CustomAdapter(this, sList); //send the details to the data adapter
        setListAdapter(adapter); //set the adapter for this activity as the custom adapter
    }

    public void onListItemClick(ListView l, View v, int position, long id)
    {
        // call detail activity for clicked entry
        Toast.makeText(getApplicationContext(), "Added to event bag",
                Toast.LENGTH_LONG).show();
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
                stringJSON = new Yelp().search(query[0], "Mesa, AZ");
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
