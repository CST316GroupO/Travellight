package groupo.travellight.app;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Brandon on 3/13/14.
 */

public class FriendsList extends ListFragment implements ChooseAddMethodDialog.ChooseAddMethodDialogListener, AddFriendDialog.AddFriendDialogListener {
    private ListView lv;
    private ArrayList<Friend> listOfFriends;
    private FriendAdapter adapter;
    private MenuInflater inflateer;
    private String filename;
    private File file;
    private String userEmail;
    private String shareFileName;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        filename="ListOfFriends.txt";
        shareFileName="";
        Intent in = getActivity().getIntent();
        Bundle b = in.getExtras();
        userEmail = b.getString("LOGIN_EMAIL");
        setHasOptionsMenu(true);
    }

    @Override
    public void onStop(){
        super.onStop();
        saveListToFile(listOfFriends);
    }

    @Override
    public void onPause(){
        super.onPause();
        saveListToFile(listOfFriends);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.friends_options, menu);
        inflateer=inflater;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        inflateer.inflate(R.menu.friends_list_context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info;
        switch(item.getItemId()){
            case R.id.context_remove:
                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                removeFriend(info.position);
                return true;
            case R.id.context_edit:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                getFriendInfo(info.position);
                return true;
            case R.id.context_send_email:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                sendFriendEmail(info.position);
            default:
                return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.actionAddfriend:
                popupDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instancedState){
        listOfFriends=readListFromFile();

        View rootView = inflater.inflate(R.layout.friends_fragment,container,false);
        lv = (ListView) rootView.findViewById(android.R.id.list);
        adapter = new FriendAdapter(this.getActivity(),R.layout.individual_list_item_friends, listOfFriends);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        l.showContextMenuForChild(v);
    }

    public void addFriend(String name, String email){
        Friend newFriend = new Friend(name, email);
        listOfFriends.add(newFriend);
        saveListToFile(listOfFriends);
        adapter.notifyDataSetChanged();
    }

    public void removeFriend(int position){
        listOfFriends.remove(position);
        saveListToFile(listOfFriends);
        adapter.notifyDataSetChanged();
    }
    public void getFriendInfo(int position){
        String currentEmail =listOfFriends.get(position).getEmail();
        String currentName = listOfFriends.get(position).getName();
        popupEditDialog(currentName, currentEmail,position);
    }
    public void sendFriendEmail(int position){

        String currentEmail =listOfFriends.get(position).getEmail();
        //this file is temporary, it only contains the current selected trip's name
        shareFileName="My trip to "+ getActivity().getActionBar().getTitle().toString()+".txt";
        File shareFile = new File(getActivity().getFilesDir(), userEmail+File.separator+shareFileName);

        try{
            FileOutputStream fos = new FileOutputStream (shareFile);
            fos.write("Hello Friend, i'm going on a trip!!".getBytes());
            fos.close();
            }
        catch(FileNotFoundException e){e.printStackTrace();}
        catch(IOException ioe){ioe.printStackTrace();}

        //send intent to email apps
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{currentEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT,"TRAVELLIGHT -Sharing a Trip");
        intent.putExtra(Intent.EXTRA_TEXT, "File is attached");
        intent.putExtra(Intent.EXTRA_STREAM,Uri.parse("content://groupo.travellight.app.FileContentProvider"+File.separator+userEmail+File.separator+shareFileName));

        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this.getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }
    public void editFriend(String name, String email, int position){
        listOfFriends.get(position).setName(name);
        listOfFriends.get(position).setEmail(email);
        adapter.notifyDataSetChanged();
    }

    public void popupDialog(){
        ChooseAddMethodDialog dialog = new ChooseAddMethodDialog();
        dialog.setTargetFragment(this,0);
        dialog.show(this.getFragmentManager(),"choose add method");
    }

    public void popupEditDialog(String name, String email, int position){
        AddFriendDialog dialog = new AddFriendDialog(name, email,position, true);
        dialog.setTargetFragment(this,0);
        dialog.show(this.getFragmentManager(), "edit popup");
    }

    private void saveListToFile(ArrayList<Friend> listOfFriends) {
        try {
            file = new File(getActivity().getFilesDir(), userEmail+File.separator+filename);
            FileOutputStream fos = new FileOutputStream (file);
            ObjectOutputStream os = new ObjectOutputStream ( fos );
            os.writeObject ( listOfFriends );
            fos.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Friend> readListFromFile(){
        ArrayList<Friend> listOfFriendObjs = new ArrayList<Friend>();

        try {
                FileInputStream fis = new FileInputStream(getActivity().getFilesDir() + File.separator+userEmail+File.separator + filename);
                ObjectInputStream ois = new ObjectInputStream(fis);
                listOfFriendObjs = (ArrayList<Friend>) ois.readObject();
                fis.close();
                ois.close();

        } catch (FileNotFoundException e) {
            saveListToFile(listOfFriendObjs);//will create the file if it doesn't exist, saving an empty array of friends
            e.printStackTrace();
        }
        catch(ClassNotFoundException ce){
            ce.printStackTrace();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        return listOfFriendObjs;
    }

    @Override
    public void clickedPositive(String name, String email,int position, boolean editing){
        if (editing) editFriend(name,email,position);
        else addFriend(name, email);
    }

    @Override
    public void choseContactFriends(ArrayList<Friend> contactFriends){}

    @Override
    public void choseManually(String name, String email, int position, boolean editing){
        if (editing) editFriend(name,email,position);
        else addFriend(name, email);
    }

    @Override
    public void choseImportContacts(ArrayList<Friend> contactFriends){
        listOfFriends.addAll(contactFriends);
        adapter.notifyDataSetChanged();
        saveListToFile(listOfFriends);
    }
}