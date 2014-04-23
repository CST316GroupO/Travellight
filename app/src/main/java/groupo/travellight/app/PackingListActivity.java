package groupo.travellight.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph Bandola on 2/25/14.
 * Packing List Activity
 * This Activity handles the Packing List Screen, including
 * viewing the list as well as modifying the elements
 * contained in the list.
 * TODO: Save List,
 *          -Currently Working on This on a Seprate Project
 */
public class PackingListActivity extends ActionBarActivity {

    TextView userEmail, userTrip;
    Button saveListButton;
    EditText itemText;
    ImageView imgViewPackingImage;
    String statusText, filename, email, title;
    List<PackingItem> PackingItems = new ArrayList();
    ListView packingListView;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_list);

        //File Management Stuff
        //filename = "packingList.txt";

        //Initialize List
        //PackingItems = readPackingListFromFile();

        //Fetches the Email Field From Previous Activity
        final Intent in = getIntent();
        final Bundle b = in.getExtras();
        CharSequence title;
        if (b == null){
            email = "Test Email";
            title = "Test Name";
        }
        else{
            email = b.getString("LOGIN_EMAIL");
            title = b.getCharSequence("TRIP_NAME");
        }

        setTitle( email  + "'s Packing List");

        //Email Loaded
        userTrip = (TextView) findViewById(R.id.textViewUserTrip);
        userTrip.setText(title);

        //Load Name of Trip
        itemText = (EditText) findViewById(R.id.itemText);
        imgViewPackingImage = (ImageView) findViewById(R.id.imgViewPackingImage);
        statusText = "Unpacked";
        packingListView = (ListView) findViewById(R.id.packingListView);


        //Set up the Add Item and Packing List Tabs Respectively
        final TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Packing List");
        tabSpec.setContent(R.id.tabPackList);
        tabSpec.setIndicator("Packing List");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("Add Item");
        tabSpec.setContent(R.id.tabAdd);
        tabSpec.setIndicator("Add Item");
        tabHost.addTab(tabSpec);

        //Loading default items
        addPackingItem("Passport", "Unpacked");
        addPackingItem("Clothes", "Unpacked");
        populateList();

        //Prep up the removal methods
        callRemove();

        //populateListInitial();

        final Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPackingItem(itemText.getText().toString(), statusText.toString());
                populateList();
                Toast.makeText(getApplicationContext(), "Your Item: " + itemText.getText().toString() + " Has Been Added!", Toast.LENGTH_SHORT).show();
            }
        });

        //'Change Image' interface.
/*        imgViewPackingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Item Image"), 1);
            }
        });*/


        itemText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                btnAdd.setEnabled(!itemText.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

/*
        saveListButton = (Button) findViewById(R.id.buttonSavePackingList); {

            saveListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    savePackingListToFile();
                }
            });
        }
*/

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.packing_context_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.action_calendar: {
                final Intent intent = new Intent(this, TripActivity.class);
                intent.putExtra("LOGIN_EMAIL", email);
                intent.putExtra("TRIP_NAME", title);
                startActivity(intent);
            }
            default:
                break;
        }
        return true;
    }


    //Remove Item Derived From Tommy's checkForRemove Method
    private void callRemove(){
        final ListView list = (ListView) findViewById(R.id.packingListView);
        final PackingListAdapter adapter = new PackingListAdapter();
        list.setAdapter(adapter);

        //Method for delete
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l)
            {
                final AlertDialog.Builder adb = new AlertDialog.Builder(PackingListActivity.this);
                adb.setTitle("Are you sure you want to delete the item: " + PackingItems.get(position).getName() + "?");

                adb.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                });

                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        PackingItems.remove(position);
                        populateList();
                    }
                });

                adb.show();
                return false;
            }
        });

        //Method for delete
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                final AlertDialog.Builder adb = new AlertDialog.Builder(PackingListActivity.this);
                adb.setTitle("Are you sure you want to delete the item: " + PackingItems.get(position).getName() + "?");

                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PackingItems.remove(position);
                        populateList();
                    }
                });

                adb.show();
                return false;
            }
        });



        //Method for Change to Packed
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                final AlertDialog.Builder adb = new AlertDialog.Builder(PackingListActivity.this);
                adb.setTitle("Are you sure you want to set the item: " + PackingItems.get(position).getName() + " to Packed?");

                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PackingItems.get(position).setStatus("Packed");
                        populateList();
                    }
                });

                adb.show();

            }
        });

    }

/*

    //savePackingListToFile
    // - Saves the Current Packing List to a File
    // - Based on the saveListToFile method Brandon used
    public void savePackingListToFile(ArrayList<PackingItem> PackingItems){
        try {
            file = new File(getFilesDir(), userTrip+File.separator+filename);
            FileOutputStream fos = new FileOutputStream (file);
            ObjectOutputStream os = new ObjectOutputStream (fos);
            os.writeObject ( PackingItems );
            fos.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/


/*
    public ArrayList<PackingItem> readPackingListFromFile(){
        ArrayList<PackingItem> packingItems = new ArrayList<PackingItem>();

        try {
            FileInputStream fis = new FileInputStream(PackingListActivity.this.getFilesDir() + File.separator+userTrip+File.separator + filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            packingItems = (ArrayList<PackingItem>) ois.readObject();
            fis.close();
            ois.close();

        } catch (FileNotFoundException e) {
            savePackingListToFile(packingItems);//will create the file if it doesn't exist, saving an empty array of friends
            e.printStackTrace();
        }
        catch(ClassNotFoundException ce){
            ce.printStackTrace();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        return packingItems;
    }
*/

    //Called to add new packing list item to the list
    //See: PackingListAdapter
    public void populateList() {
        final ArrayAdapter<PackingItem> adapter = new PackingListAdapter();
        packingListView.setAdapter(adapter);
    }

/*    private void populateListInitial() {
        ArrayAdapter<PackingItem> adapter = new InitialPackingListAdapter();
        packingListView.setAdapter(adapter);
    }*/

    //Constructs a new Packing List Item Object When Called
    private void addPackingItem(String name, String status) {
        PackingItems.add(new PackingItem(name, status));
    }

    //Method that handles fetching a new image from phone storage.
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK){
            if (resCode == 1)
            imgViewPackingImage.setImageURI(data.getData());
        }
    }


    /**
     * PackingListAdapter
     * Custom Adapter That Implements the Unique List View Element for the Packing List
     */
    public class PackingListAdapter extends ArrayAdapter<PackingItem> {

        public PackingListAdapter() {
            super (PackingListActivity.this, R.layout.listview_packingitem, PackingItems);
        }

        //This handles the actual item being added
        @Override
        public View getView(int position, View view, ViewGroup parent){
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_packingitem, parent, false);

            final PackingItem currentItem = PackingItems.get(position);

            final TextView name = (TextView) view.findViewById(R.id.packingItemName);
            name.setText(currentItem.getName());

            final TextView status = (TextView) view.findViewById(R.id.packingItemStatus);
            status.setText(currentItem.getStatus());

            return view;
        }
    }

/*    *
     * InitialPackingListAdapter
     * Custom Adapter That Implements the Unique List View Element for the Packing List

    private class InitialPackingListAdapter extends ArrayAdapter<PackingItem> {

        public InitialPackingListAdapter() {
            super (PackingListActivity.this, R.layout.listview_packingitem, PackingItems);
        }

        //This handles the actual item being added
        @Override
        public View getView(int position, View view, ViewGroup parent){
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_packingitem, parent, false);

            PackingItem currentItem = PackingItems.get(position);

            TextView name = (TextView) view.findViewById(R.id.packingItemName);
            name.setText(currentItem.getName());

            TextView status = (TextView) view.findViewById(R.id.packingItemStatus);
            status.setText(currentItem.getStatus());

            return view;
        }
    }*/


}