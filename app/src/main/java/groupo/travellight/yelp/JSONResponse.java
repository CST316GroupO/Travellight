package groupo.travellight.yelp;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JSONResponse
{
    private String JSONString;
    private ArrayList<HashMap<String, String>> bundle = new ArrayList<HashMap<String, String>>();

    /**
     * Returns the size of the the list of businesses
     * @return int bundle size
     */
    public int getBundleSize() { return bundle.size(); }

    /**
     * Returns the raw JSON response string
     * @return String JSONString
     */
    public String getResponse()
    {
        return JSONString;
    }

    /**
     * Parse response for the business name; mobile url; and ratings url, etc..
     * @throws org.json.JSONException
     */
    public void parseBusiness() throws JSONException
    {
        JSONObject jObj = new JSONObject(JSONString); //parse response to JSON object
        JSONArray JSONlist = jObj.getJSONArray("businesses"); //separate to array
        String tmpString;

        // For every business; grab details about it and put it to a hash map
        for (int i = 0; i < JSONlist.length(); i++)
        {
            // Create table of businesses
            HashMap<String, String> business = new HashMap<String, String>();
            // Add details for each business
            business.put("name", JSONlist.getJSONObject(i).get("name").toString());
            business.put("mobile_url", JSONlist.getJSONObject(i).get("mobile_url").toString());
            business.put("rating_img_url", JSONlist.getJSONObject(i).get("rating_img_url").toString());
            business.put("rating", JSONlist.getJSONObject(i).get("rating").toString());
            business.put("snippet_text", JSONlist.getJSONObject(i).get("snippet_text").toString());

            bundle.add(business); //add each business to the bundle
        }
    }

    /**
     * This gets the business's name, which is stored in the ArrayList bundle, using
     * this class's stored results.
     * @param i The index number for the business
     * @return String Business name
     */
    public String getBusinessName(int i) { return bundle.get(i).get("name"); }

    /**
     * Get the image url for the business photo
     * @param i
     * @return image_url
     */
    public String getImageURL(int i) { return bundle.get(i).get("rating_img_url"); }

    public String getRating(int i) { return bundle.get(i).get("rating"); }
    public String getSnippet(int i) { return bundle.get(i).get("snippet_text"); }

    /**
     * Set the raw JSON response string
     * @param str Raw JSON String to store
     */
    public void setResponse(String str)
    {
        JSONString = str;
    }
}
