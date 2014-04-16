package groupo.travellight.app.ActivitiesToTestFriends;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import groupo.travellight.app.FriendsList;
import groupo.travellight.app.R;

/**
 * Created by Brandon on 4/14/14. This activity starts with the FriendsList Fragment, so that I can
 * test its methods and stuff without having to deal with TripActivity yet
 */
public class ActivityToTestFriends extends ActionBarActivity{
     @Override
    protected void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_trip);
         FragmentTransaction ft = getFragmentManager().beginTransaction();
         ft.replace(R.id.content_frame, new FriendsList());//change add to replace?
         ft.addToBackStack(null);
         ft.commit();
     }

    @Override
    protected void onPause(){
        super.onPause();
    }
}
