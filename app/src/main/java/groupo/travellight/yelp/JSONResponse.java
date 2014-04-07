package groupo.travellight.yelp;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONResponse
{
    private String JSONString;
    private Bundle bundle = new Bundle();
    private ArrayList<String> keys = new ArrayList<String>();

    /**
     * Set the raw JSON response string
     * @param str Raw JSON String to store
     */
    public void setResponse(String str)
    {
        JSONString = str;
    }

    /**
     * Returns the raw JSON response string
     * @return String JSONString
     */
    public String getResponse()
    {
        return JSONString;
    }

    /**
     * Parse response for the business name; mobile url; and ratings url.
     * Mobile url and ratings url are separated by " ,,, "
     * @sets bundle(key = business name)
     * @sets keys arraylist with business name
     * @throws org.json.JSONException
     */
    public void parseBusiness() throws JSONException
    {
        JSONObject jObj = new JSONObject(JSONString); //parse response to JSON object
        JSONArray businesses = jObj.getJSONArray("businesses"); //separate to array
        String tmpString;

        // For every business; grab details about it and put it to a bundle
        for (int i = 0; i < businesses.length(); i++)
        {
            tmpString = businesses.getJSONObject(i).get("mobile_url").toString() +
                    " ,,, " + businesses.getJSONObject(i).get("rating_img_url").toString() +
            ",,," + businesses.getJSONObject(i).get("image_url").toString();

            keys.add(businesses.getJSONObject(i).get("name").toString());
            bundle.putString(keys.get(i), tmpString);
        }
    }

    /**
     * This gets the business's name, which is stored in the ArrayList keys, using
     * this class's stored results.
     * @param i The index number for the business
     * @return String Business name
     */
    public String getBusinessName(int i)
    {
        return keys.get(i);
    }

    /**
     * Get the image url for the business photo
     * @param i
     * @return image_url
     */
    public String getImageURL(int i)
    {
        String tmp = bundle.getString(keys.get(i));
        int x = tmp.indexOf(" ,,, ") + 10;
        String imageURL = tmp.substring(0, x);
        return imageURL;
    }

    /**
     * This returns the mobile URL using this class's internally stored variables at int i.
     *
     * @param i
     * @return mobile_url
     */
    public String getBusinessMobileURL(int i)
    {
        String tmp = bundle.getString(keys.get(i));
        int x = tmp.indexOf(" ,,, ");
        String mobileURL = tmp.substring(0, x);
        return mobileURL;
    }

    /**
     * This will return the rating URL using this class's internal variables.
     * @param i
     * @return rating_url
     */
    public String getRatingURL (int i){
        String tmp = bundle.getString(keys.get(i));
        int x = tmp.indexOf(" ,,, ") + 5;
        String ratingURL = tmp.substring(x);
        return ratingURL;
    }

    /**
     * Returns the bundle, key is the business name.
     * @return bundle
     */
    public Bundle getYelpBundle(){return bundle;}

    /**
     * Returns the keys (business names) for the yelpBundle.
     * @return keys (business names)
     */
    public ArrayList<String> getBundleKeys(){return keys;}

    /**
     * This will return the keys.size(), and is designed to be used with loops
     * @return keys.size()
     */
    public int getBundleKeysSize(){int size = keys.size(); return size; }
}
