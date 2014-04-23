package groupo.travellight.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Brandon on 3/15/14.
 */
public class FriendAdapter extends ArrayAdapter<Friend> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Friend> data= null;

    public FriendAdapter(Context context, int layoutResourceId, ArrayList<Friend> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public Friend getItem(int position){
        Friend friend= data.get(position);
        return friend;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FriendHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new FriendHolder();
            holder.icon=(ImageView) row.findViewById(R.id.friend_portrait);
            holder.name = (TextView)row.findViewById(R.id.friend_name);
            holder.email = (TextView)row.findViewById(R.id.friend_email);

            row.setTag(holder);
        }
        else
        { holder = (FriendHolder)row.getTag();
        }

        Friend friend = data.get(position);
        try {
            holder.icon.setImageResource(R.drawable.ic_action_person);
            holder.name.setText(friend.getName());
            holder.email.setText(friend.getEmail());
        }catch (Exception e){System.out.print("some error ocurred");}

        return row;
    }

    static class FriendHolder
    {
        ImageView icon;
        TextView name;
        TextView email;
    }
}