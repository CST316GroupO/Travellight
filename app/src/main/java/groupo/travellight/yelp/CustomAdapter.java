package groupo.travellight.yelp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import groupo.travellight.app.R;
import groupo.travellight.app.YelpResultsActivity;

/**
 * Custom data adapter that places yelp data into the proper layout fields
 * @author Brant Unger
 * @version 0.1
 */
public class CustomAdapter extends BaseAdapter
{
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater;
    private Activity activity;

    public CustomAdapter(Activity a, ArrayList<HashMap<String, String>> d)
    {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //TODO:
        //imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    /**
     * Inherited method from BaseAdapter that returns the size of
     * the data in the adapter
     * @return
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

        TextView businessName = (TextView) viewInflater.findViewById(R.id.businessName);
        TextView description = (TextView) viewInflater.findViewById(R.id.description);
        //ImageView thumbImg = (ImageView) viewInflater.findViewById(R.id.thumbnail);
        ImageView ratingStars = (ImageView) viewInflater.findViewById(R.id.ratingImg);

        // Define a new result and set it at the current adapter index
        HashMap<String, String> result = new HashMap<String, String>();
        result = data.get(position);

        // Set all the UI details into the view
        businessName.setText(result.get(YelpResultsActivity.KEY_NAME));
        //description.setText(result.get(YelpResultsActivity.KEY_DESCRIPTION));
        //TODO: add image loader for thumbnail and rating stars

        return viewInflater;
    }
}
