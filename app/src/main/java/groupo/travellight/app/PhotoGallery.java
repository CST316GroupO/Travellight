package groupo.travellight.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    //Cursor used to access the results from querying for images on the SD card.
    private Cursor cursor;
    //Column index for the Thumbnails Image IDs.
    private int columnIndex;

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

        //set up an array of the Thumbnail Image ID column we want
        String[] projection = {MediaStore.Images.Thumbnails._ID};
        //Create the cursor pointing to the SD Card
        cursor = getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Thumbnails.IMAGE_ID);
        //get the column index of the Thumbnails Image ID
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
        gallery = (Gallery) findViewById(R.id.gallery_layout);
        gallery.setAdapter(new ImageAdapter(this));

        imageView = (ImageView) findViewById(R.id.gallery_display);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //get the current value for the requested column
                int imageID = cursor.getInt(columnIndex);
                //obtain the image URI
                Uri uri = Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, Integer.toString(imageID));
                String url = uri.toString();
                // set the content of the image based on the image URI
                int originalImageId = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1, url.length()));
                Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), originalImageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
                imageView.setImageBitmap(bitmap);
                Toast.makeText(getApplicationContext(), "pic: " + position, Toast.LENGTH_SHORT).show();
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
            return cursor.getCount();
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
            ImageView imageView;
            if(view == null)
            {
                imageView = new ImageView(context);
                //move cursor to current position
                cursor.moveToPosition(position);
                //get the current value for the requested column
                int imageID = cursor.getInt(columnIndex);
                //set the content of the image based on the provided URI
                imageView.setImageURI(Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID));
                imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(10, 10, 10, 10);
            }
            else
            {
                imageView = (ImageView) view;
            }
            //imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
            //imageView.setPadding(10, 10, 10, 10);
            //imageView.setImageResource(imageIds[position]);
            return imageView;
        }
    }
}
