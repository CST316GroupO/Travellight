package groupo.travellight.app;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;


public class OpenCalendarFile extends ActionBarActivity {

    //private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Travellightt");
        Intent incomingIntent = getIntent();
        Uri incomingUri = incomingIntent.getData();
        //File file = new File(incomingUri.getPath());

        //View contentView = getLayoutInflater().inflate(R.layout.activity_open_calendar_file,,false);
        //String tripName= incomingUri.getLastPathSegment();
        String tripName= incomingUri.getPath();
        setContentView(R.layout.activity_open_calendar_file);
        TextView fileMessage= (TextView) findViewById(R.id.fileContentMessageDisplay);
        TextView tripNameDisplay= (TextView) findViewById(R.id.tripNameDisplay);
        tripNameDisplay.setText(tripName);

        try {

            //using contentResolver to get filecontents, and making a local file out of it
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream= contentResolver.openInputStream(incomingUri);
            File newFile = new File(getFilesDir()+File.separator+"ReceivedFile.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            while (inputStream.available()>0) {
                fileOutputStream.write(inputStream.read());
            }
            fileOutputStream.close();

            //for displaying file contents, this is TEMPORARY: contents will actually be Calendar information
            FileReader fileInputStream = new FileReader(newFile);
            BufferedReader bufferedReader = new BufferedReader(fileInputStream);
            String input= bufferedReader.readLine();
            fileMessage.setText(input);



        }catch (FileNotFoundException fe){
            fe.printStackTrace();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.open_calendar_file, menu);
        return true;
    }

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

}
