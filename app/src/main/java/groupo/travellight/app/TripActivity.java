package groupo.travellight.app;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class TripActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks{
    private ArrayList<String> trips = new ArrayList();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String mEmail;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        String email = b.getString("LOGIN_EMAIL");
        mEmail = email;
        File folder = new File(getApplicationContext().getFilesDir().getPath().toString() + "/" + email);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++)
        {

            if (listOfFiles[i].isDirectory())
            {
                trips.add(listOfFiles[i].getName());

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
                if (mTitle.equals("Trips")){
                    Toast.makeText(getApplicationContext(), "Select or create a trip.", Toast.LENGTH_LONG).show();
                }
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);




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
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {


        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(trips.get(position));
        mDrawerLayout.closeDrawer(mDrawerList);
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
    public void gotoEvent(MenuItem item){
        Intent intent = new Intent(this,EventsBag.class);
        startActivity(intent);
    }
    //comment
    public void gotoPacking(MenuItem item){
        Intent intent = new Intent(this,PackingListActivity.class);
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
        helpBuilder.setTitle("Remove trip " + getActionBar().getTitle()+ "?");


        //Save button
        helpBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                File f = new File(getApplicationContext().getFilesDir().getPath().toString() + "/" + mEmail + "/" + getActionBar().getTitle());
                if (f.exists()){

                    f.delete();
                    trips.remove(getActionBar().getTitle());
                    getActionBar().setTitle("Trips");
                    mTitle = "Trips";

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
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new FriendsList());//change add to replace?
                ft.addToBackStack(null);
                ft.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    /**
     * A placeholder fragment containing a simple view.
     */


}
