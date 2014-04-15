package groupo.travellight.test;

import android.app.Fragment;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import groupo.travellight.app.ActivityToTestFriends;
import groupo.travellight.app.FriendsList;
import groupo.travellight.app.LoginActivity;

import groupo.travellight.app.R;
import groupo.travellight.app.TripActivity;

/**
 * Created by Brandon on 4/14/14. The Test starts the ActivityToTestFriends activity first to be able to test only
 * Friends fragment and not the TripActivity
 */
//TODO: Test for actual stuff,
public class FriendsListTest extends ActivityInstrumentationTestCase2<ActivityToTestFriends> {

    ActivityToTestFriends activity;
    TextView textHeader, friendListView;
    Instrumentation mInstrumentation;

    public FriendsListTest(){
        super(ActivityToTestFriends.class);


    }


    @Override
    protected void setUp() throws Exception{
        super.setUp();
        Intent intent = new Intent ();
        intent.putExtra("LOGIN_EMAIL", "TestEmail");
//
        setActivityIntent(intent);
        activity=  getActivity();
        mInstrumentation= getInstrumentation();
        //so TripActivity has trips to add to
//        File folder = new File(activity.getApplicationContext().getFilesDir().getPath().toString() + "/" + "TestEmail"+"testTripFolder");
//        folder.mkdir();
    }

    public void testThatFriendsListOpens() {

        //mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);//open overflow menu
        //mInstrumentation.invokeMenuActionSync(activity,groupo.travellight.app.R.id.action_friends, 0);//click on Friends/Share


//        textHeader = (TextView)activity.findViewById(groupo.travellight.app.R.id.txtHeader);
//        friendListView = (TextView)activity.findViewById(android.R.id.list);
//
//        ViewAsserts.assertOnScreen(textHeader.getRootView(),textHeader);
//        ViewAsserts.assertOnScreen(friendListView.getRootView(),friendListView);
        Fragment frag = waitForFragment(groupo.travellight.app.R.id.content_frame, 5000);
//        Fragment frag = activity.getFragmentManager()
//                .findFragmentById(groupo.travellight.app.R.id.content_frame);

        assertTrue(frag instanceof FriendsList);
        //get the fragment
        //friendsFrag= activity.friendsFragment;
        //assertNotNull(activity);
       //assertNotNull(friendsFrag);

    }

    public void testThatFriendIsAdded(){

    }

    public void testCorrectFriendNameEmailInputs(){

    }

    public void testImportingContacts(){

    }

    public void testDeletingFriends(){
        
    }

    //waits for fragment to load...
    protected Fragment waitForFragment(int id, int timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() <= endTime) {

            Fragment fragment = getActivity().getFragmentManager().findFragmentById(id);
            if (fragment != null) {
                return fragment;
            }
        }
        return null;
    }
}
