package groupo.travellight.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by Brandon on 3/22/14.
 */
public class ImportContactsDialog extends DialogFragment {
    private AddFriendDialog.AddFriendDialogListener callback;
    private ArrayList<Friend> selectedContacts;
    private ContentResolver contentResolver;
    private Uri contactsUri;
    private Cursor cursor;
    private ListView contactsList;
    private ArrayList<String> tempList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactsUri= ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        contentResolver = getActivity().getContentResolver();
        selectedContacts = new ArrayList<>();
        tempList=new ArrayList<>();

        try {
            callback = (AddFriendDialog.AddFriendDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement AddFriendDialogListener interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View rootView = inflater.inflate(R.layout.activity_my_own_contacts, null);
        contactsList = (ListView) rootView.findViewById(R.id.contacts_list);
        contactsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final ListAdapter adapter = retrieveContacts();
        contactsList.setAdapter(adapter);

        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position,long id){

                TextView nameView = (TextView)viewClicked.findViewById(R.id.contact_display_name);
                TextView emailView = (TextView)viewClicked.findViewById(R.id.contact_email_address);
                CheckBox checkBox = (CheckBox) viewClicked.findViewById(R.id.contact_checkbox);

                checkBox.toggle();
                String name= nameView.getText().toString();
                String email=emailView.getText().toString();
                if(checkBox.isChecked()){
                    tempList.add(name+"::"+email);
                }
                else tempList.remove(name+"::"+email);//item is reselected
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView);
        builder.setTitle("Your contacts with emails");
        builder.setPositiveButton("Add Selected", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    for (int index = 0; index < tempList.size(); index++) {
                        String [] strings= tempList.get(index).split("::");
                        String name = strings[0];
                        String email = strings[1];
                        selectedContacts.add(new Friend(name, email));
                    }

                callback.choseContactFriends(selectedContacts);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing, dialog closes
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private SimpleCursorAdapter retrieveContacts(){
        //selecting the columns in contacts table
        String [] projection = new String[]{
                ContactsContract.RawContacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.DATA
        };
        cursor = contentResolver.query(contactsUri, projection, null,null,null);

        String [] columnsToAdapt=new String[] {
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.DATA
        };

        int[] listItemViews= new int[]{
                R.id.contact_display_name,
                R.id.contact_email_address
        };
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_item_contacts,
                cursor,
                columnsToAdapt,
                listItemViews,
                0
        );
        return cursorAdapter;
    }
}
