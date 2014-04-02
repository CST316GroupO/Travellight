package groupo.travellight.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
/**
 * Created by Joseph Bandola on 2/25/14.
 * This Activity handles the Basic Packing List Screen, showing the viewer their list
 * TODO: Implement a New XML Parser to Handle the New 'Packing Item' Objects, and load them to this list (WIP)
 *          -Currently Working on This on a Seprate Project
 */
public class PackingListActivity extends Activity {

    ListView listPackingItems;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_packing_list);

            //Static Packing XML Loader, to Be Replaced by an XML Parse and Load
            //TODO: Implement a new XML Parser to load a fancier looking list.
            listPackingItems = (ListView) findViewById(R.id.packingListView);
            listPackingItems.setAdapter(new ArrayAdapter<>(PackingListActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.packingList)));

            //Handles 'add a new item' button
            final Button button_Add = (Button) findViewById(R.id.button_addPacking);
            button_Add.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v)
                {
                    Intent i = new Intent(PackingListActivity.this, PackingListEdit.class);
                    startActivity(i);
                }
            });

            //Handles 'return to hub' button.
            final Button button_HubReturn = (Button) findViewById(R.id.button_HubReturn);
            button_HubReturn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v)
                {
                    Intent i = new Intent(PackingListActivity.this, MainScreen.class);
                    startActivity(i);
                }
            });

        }

}