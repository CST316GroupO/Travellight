package groupo.travellight.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Joseph Bandola on 4/2/2014.
 * PackingListEdit
 * Handles the 'edit' page for packing list. Which gives the user an interface for adding new items.
 * TODO: Implement an Actual Add that Works With the Default P-List Loader (WIP)
 */

public class PackingListEdit extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_edit);

        //Handles 'Confirm add Item' Button
        final Button button_ConfirmAdd = (Button) findViewById(R.id.button_confirmAddItem);
        button_ConfirmAdd.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //TODO: Add new to the packing list from the text field
                Toast.makeText(getApplicationContext(), "Item Would've Been Added!", Toast.LENGTH_SHORT).show();
            }
        });

        //Handles 'cancel' on the PackingListEdit Page
        final Button button_CancelAdd = (Button) findViewById(R.id.button_cancelAddItem);
        button_CancelAdd.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(PackingListEdit.this, PackingListActivity.class);
                startActivity(i);
            }
        });
    }

}