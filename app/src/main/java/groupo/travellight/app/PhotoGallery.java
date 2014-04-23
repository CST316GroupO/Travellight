package groupo.travellight.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Tommy Pham on 4/11/2014.
 */
public class PhotoGallery extends Activity {
    Gallery gallery;
    ImageView imageView;
    //testing if the gallery will show images, will be removed later.
    public Integer[] imageIds = {
            R.drawable.ic_launcher,
            R.drawable.images,
            R.drawable.badge,
            R.drawable.index
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        gallery = (Gallery) findViewById(R.id.gallery_layout);
        gallery.setAdapter(new ImageAdapter(this));

        imageView = (ImageView) findViewById(R.id.gallery_display);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(), "pic: " + position, Toast.LENGTH_SHORT).show();
                imageView.setImageResource(imageIds[position]);
            }
        });
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.trip, menu);
        //return true;
    //}

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

    public class ImageAdapter extends BaseAdapter
    {
        private Context context;

        public  ImageAdapter(Context context)
        {
            this.context = context;
        }

        @Override
        public int getCount() {
            return imageIds.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
            imageView.setPadding(10, 10, 10, 10);
            imageView.setImageResource(imageIds[position]);
            return imageView;
        }
    }
}
