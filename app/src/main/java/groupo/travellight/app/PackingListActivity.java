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
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph Bandola on 2/25/14.
 * This Activity handles the Basic Packing List Screen, showing the viewer their list
 * TODO: Remove, Save List
 *          -Currently Working on This on a Seprate Project
 */
public class PackingListActivity extends ActionBarActivity {

    EditText itemText;
    String statusText;
    List<PackingItem> PackingItems = new ArrayList<PackingItem>();
    ListView packingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_list);

        itemText = (EditText) findViewById(R.id.itemText);
        statusText = "Unpacked";
        packingListView = (ListView) findViewById(R.id.packingListView);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();
        //Set up Add Item Tab
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Add Item");
        tabSpec.setContent(R.id.tabAdd);
        tabSpec.setIndicator("Add Item");
        tabHost.addTab(tabSpec);

        //Set Up Pack List Tab
        tabSpec = tabHost.newTabSpec("packList");
        tabSpec.setContent(R.id.tabPackList);
        tabSpec.setIndicator("Packing List");
        tabHost.addTab(tabSpec);

        //Loading default items
        addPackingItem("Passport", "Unpacked");
        addPackingItem("Clothes", "Unpacked");
        populateList();

        //Set up the removal methods
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
    //BUG: Activity Crashes When You Remove An Item Added In The Wizard
    private void callRemove(){
        ListView list = (ListView) findViewById(R.id.packingListView);
        final PackingListAdapter adapter = new PackingListAdapter();
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l)
            {
                AlertDialog.Builder adb = new AlertDialog.Builder(PackingListActivity.this);
                adb.setTitle("Are you sure you want to delete this item?");

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