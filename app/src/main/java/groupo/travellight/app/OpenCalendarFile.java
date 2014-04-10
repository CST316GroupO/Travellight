package groupo.travellight.app;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;


public class OpenCalendarFile extends ActionBarActivity {
    private Button yesButton, noButton;
    private TextView fileMessage, tripNameDisplay;
    private String userEmail;
    private Intent incomingIntent;
    private Uri incomingUri;
    //private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Travellightt");
        incomingIntent = getIntent();
        incomingUri = incomingIntent.getData();
        //Todo: find a way to save files to the user's email folder
       // Bundle bundle = incomingIntent.getExtras();
        //userEmail=bundle.getString("LOGIN_EMAIL");

        String tripName= incomingUri.toString();//used to be .getPath();
        setContentView(R.layout.activity_open_calendar_file);

        fileMessage= (TextView) findViewById(R.id.fileContentMessageDisplay);
        tripNameDisplay= (TextView) findViewById(R.id.tripNameDisplay);

        try {

            //using contentResolver to get file contents and making a local file out of it
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
            StringTokenizer stringTokenizer =new StringTokenizer(input, "$");
            String fileTripName = stringTokenizer.nextToken().toString();
            StringTokenizer stringTokenizer1 = new StringTokenizer(fileTripName,".");
            String fileTripName2=stringTokenizer1.nextToken();
            String fileContent = stringTokenizer.nextToken().toString();

            tripNameDisplay.setText(fileTripName2);
            fileMessage.setText(fileContent);



        }catch (FileNotFoundException fe){
            fe.printStackTrace();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }

        yesButton = (Button) findViewById(R.id.openFileYes);
        noButton= (Button) findViewById(R.id.openFileNo);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Good for you.", Toast.LENGTH_SHORT).show();
            //add the trip to the navigation drawer of the TripActivity by adding a new file to the user's directory
//                File newTripFile= new File(getApplicationContext().getFilesDir().getPath().toString()
//                                           + File.separator + userEmail+File.separator+)
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "whatever you say", Toast.LENGTH_LONG).show();
            }
        });


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
