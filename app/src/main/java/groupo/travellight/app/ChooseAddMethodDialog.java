package groupo.travellight.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import java.util.ArrayList;

/**
 * Created by Brandon on 3/22/14.
 */
public class ChooseAddMethodDialog extends DialogFragment implements AddFriendDialog.AddFriendDialogListener{
    private ChooseAddMethodDialogListener callback;
    private DialogFragment currentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentFragment=this;

        try {
            callback = (ChooseAddMethodDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement ChooseAddMethodDialogListener interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("How would you like to add your friend? ");
        builder.setPositiveButton("From Contacts", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ImportContactsDialog contactsDialog = new ImportContactsDialog();
                contactsDialog.setTargetFragment(currentFragment, 0);
                contactsDialog.show(currentFragment.getFragmentManager(),"contacts dialog");
            }
        });
        builder.setNegativeButton("Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AddFriendDialog addFriendDialog = new AddFriendDialog();
                addFriendDialog.setTargetFragment(currentFragment, 1);
                addFriendDialog.show(currentFragment.getFragmentManager(),"contacts dialog");
            }
        });
        AlertDialog dialog = builder.create();
        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    @Override //for when AddFriendDialog was chosen as the add method
    public void clickedPositive(String name,String email,int position, boolean editing){
        callback.choseManually(name,email,position,editing);
    }
    @Override//for when ImportContactsDialog was chosen as the add method
    public void choseContactFriends(ArrayList<Friend> contactFriends){
        callback.choseImportContacts(contactFriends);
    }

    public interface ChooseAddMethodDialogListener{
        public void choseManually(String name, String email,int position, boolean editing);
        public void choseImportContacts(ArrayList<Friend> contactFriends);
    }
}
