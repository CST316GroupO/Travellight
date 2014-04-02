package groupo.travellight.app;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

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
 *
 */
public class YelpResultsActivity extends ListActivity
{
    private JSONResponse jsonResponse = new JSONResponse();

    public static final String KEY_NAME = "The business name";
    public static final String KEY_DESCRIPTION = "The business description";
    CustomAdapter adapter;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    public void onNewIntent(Intent intent)
    {
        setIntent(intent);
        handleIntent(intent);
    }

    // Callback detected from the search dialog
    // call the search logic
    private void handleIntent(Intent intent)
    {
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
        {
            String query = intent.getStringExtra(SearchManager.QUERY);
            new SearchYelp().execute(query);
        }
    }

    private void postResults()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        ArrayList<HashMap<String, String>> t1 = new ArrayList<HashMap<String, String>>();
        String[] values = jsonResponse.getBundleKeys().toArray(new String[0]);
        for(int i = 0; i < values.length; i++)
        {
            System.out.println(jsonResponse.getBusinessName(i));
            map.put(KEY_NAME, jsonResponse.getBusinessName(i));
            t1.add(map);
        }

        adapter = new CustomAdapter(this, t1);
        setListAdapter(adapter);

        /*String[] values = jsonResponse.getBundleKeys().toArray(new String[0]);

        for(int i = 0; i < values.length; i++)
        System.out.println(jsonResponse.getBusinessName(i));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);*/
    }

      /*public void onListItemClick(ListView l, View v, int position, long id) {
        // call detail activity for clicked entry
    }*/

    /**
     * Thread that accesses yelp API via JSON
     *
     * @author Brant Unger
     * @version 0.1
     */
    class SearchYelp extends AsyncTask<String, Void, String>
    {
        String stringJSON;

        protected String doInBackground(String... query)
        {
            try
            {
                stringJSON = new Yelp().search(query[0], "Mesa, AZ");
                jsonResponse.setResponse(stringJSON);
                jsonResponse.parseBusiness();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }

            return stringJSON;
        }

        protected void onPostExecute(String query)
        {
            postResults();
        }
    }

}
