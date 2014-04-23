package groupo.travellight.app;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.StringTokenizer;


public class OpenCalendarFile extends ActionBarActivity {
    private Button yesButton, noButton;
    private TextView tripNameDisplay;
    private String userEmail,tripName;
    private Intent incomingIntent;
    private Uri incomingUri;
    private Friend receivedObject;//this object type will be changed later to some calendar related stuff

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Travellight");
        incomingIntent = getIntent();
        incomingUri = incomingIntent.getData();
        userEmail=getUserEmail();

        setContentView(R.layout.activity_open_calendar_file);

        //fileMessage= (TextView) findViewById(R.id.fileContentMessageDisplay);
        tripNameDisplay= (TextView) findViewById(R.id.tripNameDisplay);

        try {
            //using contentResolver to get file contents and making a local cache file out of it
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream= contentResolver.openInputStream(incomingUri);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);//****

            //*********
            //READ IN THE OBJECT:
            Friend receivedFriendObject= new Friend("notTheCorrectName","notcorrectEmail");
            try{
                receivedFriendObject = (Friend) objectInputStream.readObject();
                String friendName=receivedFriendObject.getName();
            }
            catch(ClassNotFoundException e){}
            catch(Exception e){}
            //***********

            //OLD WAY    OLD WAY     OLD WAY
//            File newFile = new File(getCacheDir()+File.separator+"ReceivedFile.txt");
//            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
//            while (inputStream.available()>0) {
//                fileOutputStream.write(inputStream.read());
//            }
//            fileOutputStream.close();
            //=================================


            //NEW WAY NEW WAY
            File newFile2 = new File(getCacheDir()+File.separator+"ReceivedAttachment.trvl");
            FileOutputStream fos = new FileOutputStream(newFile2);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
            try{objectOutputStream.writeObject(receivedFriendObject);}
            catch(Exception ce){}
            objectOutputStream.close();
            fos.close();
            //=================================


            //TODO: Change how this file reading is done when calendar info is actually written into attachment file
            //for displaying file contents, this is TEMPORARY: contents will actually be Calendar information
//            FileReader fileInputStream = new FileReader(newFile);
//            BufferedReader bufferedReader = new BufferedReader(fileInputStream);
//            String input= bufferedReader.readLine();
//
//            //get name of the trip:
//            StringTokenizer stringTokenizer =new StringTokenizer(input, "$");
//            String fileTripName = stringTokenizer.nextToken().toString();
//
//            //remove file name extension)
//            StringTokenizer stringTokenizer1 = new StringTokenizer(fileTripName,".");
//            tripName=stringTokenizer1.nextToken();
//            String fileContent = stringTokenizer.nextToken().toString();

            //TODO END TODO

            //TODO fix
            FileInputStream fis = new FileInputStream(getCacheDir()+File.separator+ "ReceivedAttachment.trvl");
            ObjectInputStream objectInputStream1 = new ObjectInputStream(fis);
            Friend receivedFriendRepeat=new Friend();
            try{receivedObject = (Friend) objectInputStream1.readObject();}
            catch(ClassNotFoundException c){}

            String friendName= receivedObject.getName();

            tripName=friendName;
            tripNameDisplay.setText(tripName);
            //tripNameDisplay.setText(friendName);//**
            //fileMessage.setText(fileContent);

           // newFile.delete();//delete temporary file
            newFile2.delete();

        }catch (FileNotFoundException fe){
            fe.printStackTrace();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        //Buttons and their click listeners:
        yesButton = (Button) findViewById(R.id.openFileYes);
        noButton= (Button) findViewById(R.id.openFileNo);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add the trip to the navigation drawer of the TripActivity by adding a new permanent file to the user's directory
                //also TEMPORARY, this will actually copy the contents of the attached file into this local permanent folder
                //make the folder:
                File newTripFolder = new File(getApplicationContext().getFilesDir().getPath().toString()
                        + File.separator + userEmail + File.separator + tripName);//this file will be read bythe TripActivity and added when it next restarts
                newTripFolder.mkdir();

                //Copy file into that folder:
                File newTripFile = new File(getApplicationContext().getFilesDir().getPath().toString()
                        + File.separator + userEmail + File.separator + tripName + File.separator + tripName + ".trvl");
                try{
                    FileOutputStream fileOutputStream = new FileOutputStream(newTripFile);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(receivedObject);
                    objectOutputStream.close();
                    fileOutputStream.close();
                }
                catch(FileNotFoundException e){}
                catch(IOException ioe){}


                Toast.makeText(getApplicationContext(), "Trip has been added", Toast.LENGTH_SHORT).show();
                //finishAffinity();
                Intent intent = new Intent(getApplicationContext(), TripActivity.class);
                intent.putExtra("LOGIN_EMAIL", userEmail);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Trip not added.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.open_calendar_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getUserEmail(){
        String prefFileName="MySavedStuff",emailName;
        SharedPreferences preferences = getSharedPreferences(prefFileName, 0);
        emailName= preferences.getString("userEmail","Not Found");
        return emailName;

    }

}
