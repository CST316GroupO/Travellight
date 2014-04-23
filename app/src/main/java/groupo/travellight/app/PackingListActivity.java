package groupo.travellight.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph Bandola on 2/25/14.
 * Packing List Activity
 * This Activity handles the Packing List Screen, including
 * viewing the list as well as modifying the elements
 * contained in the list.
 * TODO: Save List, Add A Picture To Item
 *          -Currently Working on This on a Seprate Project
 */
public class PackingListActivity extends ActionBarActivity {

    TextView userEmail, userTrip;
    EditText itemText;
    ImageView imgViewPackingImage;
    String statusText;
    List<PackingItem> PackingItems = new ArrayList<PackingItem>();
    ListView packingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_list);

        //Fetches the Email Field From Previous Activity
        Intent in = getIntent();
        Bundle b = in.getExtras();
        String email;
        CharSequence title;
        if (b == null){
            email = "Test Email";
            title = "Test Name";
        }
        else{
            email = b.getString("LOGIN_EMAIL");
            title = b.getCharSequence("TRIP_NAME");
        }

        //Email Loaded
        userEmail = (TextView) findViewById(R.id.textViewUserEmail);
        userEmail.setText(email);
        userTrip = (TextView) findViewById(R.id.textViewUserTrip);
        userTrip.setText(title);

        itemText = (EditText) findViewById(R.id.itemText);
        imgViewPackingImage = (ImageView) findViewById(R.id.imgViewPackingImage);
        statusText = "Unpacked";
        packingListView = (ListView) findViewById(R.id.packingListView);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        //Set up the Add Item and Packing List Tabs Respectively
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Add Item");
        tabSpec.setContent(R.id.tabAdd);
        tabSpec.setIndicator("Add Item");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("packList");
        tabSpec.setContent(R.id.tabPackList);
        tabSpec.setIndicator("Packing List");
        tabHost.addTab(tabSpec);

        //Loading default items
        addPackingItem("Passport", "Unpacked");
        addPackingItem("Clothes", "Unpacked");
        populateList();

        //Prep up the removal methods
        callRemove();

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
        imgViewPackingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Item Image"), 1);
            }
        });


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
    }

    //Remove Item Derived From Tommy's checkForRemove Method
    private void callRemove(){
        ListView list = (ListView) findViewById(R.id.packingListView);
        final PackingListAdapter adapter = new PackingListAdapter();
        list.setAdapter(adapter);

        //Method for delete
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l)
            {
                AlertDialog.Builder adb = new AlertDialog.Builder(PackingListActivity.this);
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


        //Method for Change to Packed
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l)
            {
                AlertDialog.Builder adb = new AlertDialog.Builder(PackingListActivity.this);
                adb.setTitle("Are you sure you want to set the item: : " + PackingItems.get(position).getName() + " to Packed?");

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
                        PackingItems.get(position).setStatus("Packed");
                        populateList();
                    }
                });

                adb.show();

            }
        });


    }

    //Called to add new packing list item to the list
    //See: PackingListAdapter
    private void populateList() {
        ArrayAdapter<PackingItem> adapter = new PackingListAdapter();
        packingListView.setAdapter(adapter);
    }

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
    private class PackingListAdapter extends ArrayAdapter<PackingItem> {

        public PackingListAdapter() {
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
    }

    }