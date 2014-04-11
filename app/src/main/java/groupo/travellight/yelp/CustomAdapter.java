package groupo.travellight.yelp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import groupo.travellight.app.R;
import groupo.travellight.app.YelpResultsActivity;

/**
 * Custom data adapter that places yelp data into the proper layout fields
 * @author Brant Unger
 * @version 0.2
 */
public class CustomAdapter extends BaseAdapter
{
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater;
    private Activity activity;

    /**
     * Constructor for a custom data adapter to handle
     * posting yelp results to a custom user interface
     * @param a Activity The activity where the data is called from
     * @param d ArrayList The list containing a hashmap of yelp results
     */
    public CustomAdapter(Activity a, ArrayList<HashMap<String, String>> d)
    {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * Inherited method from BaseAdapter that returns the size of
     * the data in the adapter
     * @return int The number of yelp results
     */
    public int getCount()
    {
        return data.size();
    }

    /**
     * Get the object
     * @param position The index of where the data is being held
     * @return The item's position
     */
    public Object getItem(int position)
    {
        return position;
    }

    /**
     * Get the item's numerical ID
     * @param position The index of where the data is being held
     * @return The item's position
     */
    public long getItemId(int position)
    {
        return position;
    }

    /**
     * Inherited method from BaseAdapter that returns an inflated
     * view generated via data held in the adapter
     * @param position The index of where the data is being held
     * @param convertView The view to grab and convert
     * @param parent The parent where the view can be found
     * @return View The view populated with data
     */
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View viewInflater = convertView;
        if (convertView == null)
        {
            viewInflater = inflater.inflate(R.layout.custom_list_row, null);
        }

        // Define activity objects for use in posting the data into the UI
        TextView businessName = (TextView) viewInflater.findViewById(R.id.businessName);
        TextView description = (TextView) viewInflater.findViewById(R.id.description);
        TextView ratingInt = (TextView) viewInflater.findViewById(R.id.ratingInt);
        ImageView thumbImg = (ImageView) viewInflater.findViewById(R.id.list_image);
        ImageView ratingStars = (ImageView) viewInflater.findViewById(R.id.ratingImg);

        // Define a new result and set it at the current adapter index
        HashMap<String, String> result = new HashMap<String, String>();
        result = data.get(position);

        // Set all the UI details into the view
        businessName.setText(result.get(YelpResultsActivity.KEY_NAME));
        description.setText(result.get(YelpResultsActivity.KEY_DESCRIPTION));
        ratingInt.setText(result.get(YelpResultsActivity.KEY_RATINGINT));

        // Download and set the rating star and thumbnail images
        new DownloadImageTask(ratingStars)
                .execute(result.get(YelpResultsActivity.KEY_RATINGURL));
        new DownloadImageTask(thumbImg)
                .execute(result.get(YelpResultsActivity.KEY_THUMBURL));

        return viewInflater;
    }

    /**
     * Download thumbnail image and set it into the UI
     * via a separate thread
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage)
        {
            this.bmImage = bmImage;
        }

        /**
         * Download and set the image into the UI in the background.
         * @param urls String URL of the image location
         * @return Bitmap Returns the image to post
         */
        protected Bitmap doInBackground(String... urls)
        {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try
            {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e)
            {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        /**
         * Call cleanup and set the image to the UI
         * @param result Image result
         */
        protected void onPostExecute(Bitmap result)
        {
            bmImage.setImageBitmap(result);
        }

    }
}
