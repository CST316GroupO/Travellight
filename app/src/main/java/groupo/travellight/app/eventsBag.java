package groupo.travellight.app;

import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class eventsBag extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_bag);

        findViewById(R.id.textSocks).setOnLongClickListener(longListen);
        findViewById(R.id.textShoes).setOnLongClickListener(longListen);
        findViewById(R.id.textShirts).setOnLongClickListener(longListen);
        findViewById(R.id.textPants).setOnLongClickListener(longListen);
        findViewById(R.id.textUnderwear).setOnLongClickListener(longListen);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.events_bag, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_events_bag, container, false);
            return rootView;
        }
    }

    /*
        Drag objects
     */
    View.OnLongClickListener longListen = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view)
        {
            DragShadow dragShadow = new DragShadow(view);

            ClipData data = ClipData.newPlainText("","");
            view.startDrag(data, dragShadow, view, 0);
            return false;
        }
    };

    /*
        The shadow you see when you start dragging
     */
    private class DragShadow extends View.DragShadowBuilder
    {
        ColorDrawable greyBox;

        public DragShadow(View view)
        {
            super(view);
            greyBox = new ColorDrawable(Color.LTGRAY);
        }

        public void onDrawShadow(Canvas canvas)
        {
            greyBox.draw(canvas);
        }

        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint)
        {
            View v = getView();

            int height = (int)v.getHeight() / 2;
            int width = (int)v.getWidth() / 2;

            greyBox.setBounds(0, 0, width, height);

            shadowSize.set(width, height);

            shadowTouchPoint.set((int)width / 2, (int)height / 2);
        }
    }

}
