package groupo.travellight.test;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.TextView;

import java.util.ArrayList;
import groupo.travellight.app.ActivitiesToTestFriends.ActivityToTestFriends;
import groupo.travellight.app.Friend;
import groupo.travellight.app.FriendsList;

/**
 * Created by Brandon on 4/14/14. The Test starts the ActivityToTestFriends activity first to be able to test only
 * Friends fragment and not the TripActivity
 */
public class FriendsListTest extends ActivityInstrumentationTestCase2<ActivityToTestFriends> {

    ActivityToTestFriends activity;
    Instrumentation mInstrumentation;
    FriendsList friendsFrag;

    public FriendsListTest(){
        super(ActivityToTestFriends.class);

    }


    @Override
    protected void setUp() throws Exception{
        super.setUp();
        setActivityInitialTouchMode(true);
        Intent intent = new Intent ();
        intent.putExtra("LOGIN_EMAIL", "TestEmail");

        setActivityIntent(intent);
        activity=  getActivity();
        mInstrumentation= getInstrumentation();
        friendsFrag =(FriendsList) getActivity().getFragmentManager().findFragmentById(groupo.travellight.app.R.id.content_frame);
    }
    @SmallTest
    public void testThatFriendFragmentIsLoaded(){
        assertTrue(friendsFrag instanceof FriendsList);
    }

    @UiThreadTest
    public void testAddFriend(){
        String friendName="testBrandon";
        String friendEmail="testBrandonEmail";
        friendsFrag.addFriend(friendName,friendEmail);
        assertTrue(((friendsFrag.getFriend(0)).getName()).equals(friendName));
        assertTrue(((friendsFrag.getFriend(0)).getEmail()).equals(friendEmail));

        friendsFrag.addFriend(friendName+"!",friendEmail+"!");
        assertTrue(((friendsFrag.getFriend(1)).getName()).equals(friendName+"!"));
        assertTrue(((friendsFrag.getFriend(1)).getEmail()).equals(friendEmail+"!"));
    }

    @UiThreadTest
    public void testDeletingFriends(){
        String friendName2="test2Brandon";
        String friendEmail2="test2BrandonEmail";
        //add two friends
        friendsFrag.addFriend(friendName2,friendEmail2);
        friendsFrag.addFriend(friendName2+"!",friendEmail2+"!");
        //remove one friend
        friendsFrag.removeFriend(1);

        //check that size is 1
        assertTrue(friendsFrag.getListOfFriends().size()==1);
        //remove remaining friend
        friendsFrag.removeFriend(0);
        assertTrue(friendsFrag.getListOfFriends().size()==0);

    }

    @UiThreadTest
    public void testEditFriend(){

        String friendName3="test3Brandon";
        String friendEmail3="test3BrandonEsmail";
        friendsFrag.addFriend(friendName3,friendEmail3);
        friendsFrag.editFriend("changedBrandon", "changedEmail", 0);//change index 0, the friend that was previously added
        //check that the friend's name and email have changed
        assertTrue(friendsFrag.getFriend(0).getName().equals("changedBrandon"));
        assertTrue(friendsFrag.getFriend(0).getEmail().equals("changedEmail"));

        //change the same friend again
        friendsFrag.editFriend("changedBrandonAgain","changedEmailAgain",0);

        //check that name and email has changed again
        assertTrue(friendsFrag.getFriend(0).getName().equals("changedBrandonAgain"));
        assertTrue(friendsFrag.getFriend(0).getEmail().equals("changedEmailAgain"));
    }

    @UiThreadTest
    public void testChoseImportContacts(){
        //create the ArrayList of Friends that the ContactsDialog would return
        ArrayList<Friend> contactFriends=new ArrayList<>();
        Friend friend1=new Friend("ContactFriend1","ContactEmail1");
        Friend friend2=new Friend("ContactFriend2","ContactEmail2");
        Friend friend3=new Friend("ContactFriend3","ContactEmail3");
        contactFriends.add(friend1);contactFriends.add(friend2);contactFriends.add(friend3);

        //check that FriendsList adds this ArrayList to the list of friends
        friendsFrag.choseImportContacts(contactFriends);
        assertTrue(friendsFrag.getFriend(0).getName().equals("ContactFriend1"));
        assertTrue(friendsFrag.getFriend(0).getEmail().equals("ContactEmail1"));

        assertTrue(friendsFrag.getFriend(1).getName().equals("ContactFriend2"));
        assertTrue(friendsFrag.getFriend(1).getEmail().equals("ContactEmail2"));

        assertTrue(friendsFrag.getFriend(2).getName().equals("ContactFriend3"));
        assertTrue(friendsFrag.getFriend(2).getEmail().equals("ContactEmail3"));

    }

}
