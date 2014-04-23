package groupo.travellight.test;

import android.app.Instrumentation;
import android.support.v7.app.ActionBar;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import groupo.travellight.app.PackingItem;
import groupo.travellight.app.PackingListActivity;
import groupo.travellight.app.R;

/**
 * Packing Test
 * A test for the PackingListActivity
 */
public class PackingTest extends ActivityInstrumentationTestCase2<PackingListActivity> {

    PackingListActivity activity;
    Button btnAdd;
    ActionBar.Tab tabadd;
    EditText itemText;
    Instrumentation mInstrumentation;
    ListView packingListView;

    public PackingTest(){
        super(PackingListActivity.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        activity = getActivity();

        mInstrumentation = getInstrumentation();

    }

    //Test if views loaded correctly
    @SmallTest
    public void testViewPacking(){
        TextView textViewUserTrip = (TextView) activity.findViewById(R.id.textViewUserTrip);
        assertNotNull("Packing TextView String 'textViewUserTrip' Failed to Load!", textViewUserTrip);

        ImageView imgViewPackingImage = (ImageView) activity.findViewById(R.id.imgViewPackingImage);
        assertNotNull( "Packing Image 'imgViewPackingImage' Failed to Load!" , imgViewPackingImage);
    }

    //Test if these elements are present
    public void testOnScreen(){
        ViewAsserts.assertOnScreen(btnAdd.getRootView(), btnAdd);
    }

    /**
     * This tests that the add functions work as they're supposed
      */
    public void testPopulateList() {

        List<PackingItem> PackingItems = new ArrayList<>();

        PackingItems.add(new PackingItem("Item1", "Unpacked"));
        PackingItems.add(new PackingItem("Item2", "Unpacked"));
        PackingItems.add(new PackingItem("Item3", "Unpacked"));


        assertNotNull("PackingItem.Add Method Failed!", PackingItems);
    }

    /**
     * testAddInterface
     * tests for:
     * Add interface functionality
     * Tap an item in the packing list should produce a prompt
     */
    public void testAddInterface() {

        List<PackingItem> PackingItems = new ArrayList<>();
        TouchUtils.tapView(this, itemText);
        getInstrumentation().sendStringSync("An Item");

        int Position = PackingItems.size() + 1;

        //Tests to see that the add interface works
        TouchUtils.clickView(this, activity.findViewById(R.id.btnAdd));
        assertSame("Add Item Interface Appears To Have Failed!", "An Item", PackingItems.get(Position).getName());
        assertSame("Add Item Interface Failed to Set Default Status!", "Unpacked", PackingItems.get(Position).getStatus());

        //Tests for Appearance of 'Set to Packed' Prompt after Tapping an Indidivdual Item
        TouchUtils.tapView(this, packingListView.findViewById(R.id.packingListView));
        assertSame("Tap Prompt for Individual Items Failed to Appear!", "Are you sure you want to set the item: " + PackingItems.get(Position).getName() + " to Packed?", packingListView.getChildAt(Position));
    }
}


