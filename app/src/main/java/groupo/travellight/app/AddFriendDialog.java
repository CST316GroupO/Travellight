package groupo.travellight.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Brandon on 3/14/14.
 */
public class AddFriendDialog extends DialogFragment{
    //For sending info to the FriendsList fragment
    private AddFriendDialogListener callback;
     EditText em;
     EditText et;
    int position;
    boolean editing=false;//to use this dialog for editing a friend not adding a new one
    String name="", email="", dialogTitle="", posButtLabel="";
    public AddFriendDialog(){

    }
    public AddFriendDialog(String name, String email,int position,boolean editing){//}, boolean editable){
        super();
        this.editing=editing;
        this.name = name;
        this.email=email;
        this.position=position;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (AddFriendDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement AddFriendDialogListener interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view =inflater.inflate(R.layout.add_friend_dialog, null);
        builder.setView(view);

        em=(EditText) view.findViewById(R.id.input_friend_email);
        et=(EditText) view.findViewById(R.id.input_friend_name);

        if (!(this.email).isEmpty() && !(this.name).isEmpty() ){
            dialogTitle="Edit your Friend";
            posButtLabel="Finish";
            et.setText(name);
            em.setText(email);

        }
        else {
           dialogTitle="Add your Friend";
           posButtLabel="Add";
        }//end else
        builder.setTitle(dialogTitle);
        builder.setPositiveButton(posButtLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                String inName = et.getText().toString();
                String inEmail = em.getText().toString();
                StringBuilder errorMessage = new StringBuilder("");
                if (!inEmail.contains("@") || !inEmail.contains(".")) {
                    errorMessage.append(" Invaid Email");

                }
                if (inName.isEmpty()) {
                    errorMessage.append("\n Invalid Friend Name");
                }
                if (!(errorMessage.toString()).equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            errorMessage.toString(), Toast.LENGTH_LONG)
                            .show();
                } else {
                    callback.clickedPositive(inName,inEmail,position, editing);
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        InputMethodManager imm =(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(),0);
    }

    public interface AddFriendDialogListener {
        public void clickedPositive(String name,String email,int position, boolean editing);
        public void choseContactFriends(ArrayList<Friend> contactFriends);

    }
}
