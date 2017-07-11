package com.example.anthonsteiness.handyjuralayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anthonsteiness.handyjuralayout.objects.BossUser;
import com.example.anthonsteiness.handyjuralayout.objects.RegularUser;
import com.example.anthonsteiness.handyjuralayout.objects.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity
{
    // Commit test
    private static final String TAG = "UserInfoActivity";

    private String title = "Min Info";

    TextView textView;
    ListView listView;
    Button changePassBtn;

    // Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRootRef;
    private DatabaseReference myChildRef;
    private String userID;
    private FirebaseUser fbUser;
    private String bossID;
    private boolean userType;

    final Context context = this;
    //private TextView nameView, emailView, branchView, cvrView;

    // Buttons and stuff from app_bar class
    ImageButton searchBtn;
    Spinner helpDropDown;
    ArrayAdapter<CharSequence> adapter2;
    TextView titleBar;
    EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent intent = getIntent();
        userType = intent.getExtras().getBoolean("userType");
        bossID = intent.getExtras().getString("bossID");
        userID = intent.getExtras().getString("userID");
        //toastMessage(userType + " " + userID);

        textView = (TextView) findViewById(R.id.tvUserInfo);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(itemClickListener);
        changePassBtn = (Button) findViewById(R.id.changePassBtn);
        changePassBtn.setOnClickListener(buttonClickListener);

        // Firebase declaration stuff
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRootRef = mFirebaseDatabase.getReference();

        myChildRef = mFirebaseDatabase.getReference(userID + "/UserInfo");

        // Check if Firebase is already logged in to
        if (firebaseAuth.getCurrentUser() != null)
        {
            // The Firebase is logged in to

        }


        myChildRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //toastMessage("hello " + fbUser.getEmail());

        // Everything here is from app_bar class -----------------
        searchBtn = (ImageButton) findViewById(R.id.searchbtn);
        searchBtn.setOnClickListener(buttonClickListener);

        searchBar = (EditText) findViewById(R.id.searchBar);
        titleBar = (TextView) findViewById(R.id.titleBar);

        helpDropDown = (Spinner) findViewById(R.id.helpDropDown);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.settingSelection, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        helpDropDown.setAdapter(adapter2);
        helpDropDown.setOnItemSelectedListener(dropDownListener);
        // Setting the title of this specific page.
        titleBar.setText(title);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            switch(view.getId())
            {
                case R.id.searchbtn:
                    toastMessage("Nothing to search for here...");
                    break;
                case R.id.changePassBtn:
                    changePassword();
                    break;
            }
        }
    };

    ArrayList<String> array;
    BossUser bossUser;
    private void showData(DataSnapshot dataSnapshot)
    {
        // For loop to iterate through all the snapshots of the database
        //for (DataSnapshot ds : dataSnapshot.getChildren())
        //{
            bossUser = new BossUser();

            bossUser = dataSnapshot.getValue(BossUser.class);

            array = new ArrayList<>();

            array.add("Navn:         " + bossUser.getFullName());
            array.add("Email:        " + bossUser.getEmail());
            String branchStr = bossUser.getBranch();
            String cvrStr = bossUser.getCVR();
            if(branchStr != null
                    && cvrStr != null)
            {
                array.add("Branch:     " + bossUser.getBranch());
                array.add("CVR:          " + bossUser.getCVR());
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
            listView.setAdapter(adapter);
        //}
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            String str = array.get(position);
            //toastMessage(str);
            if (str.contains("Navn: "))
            {
                //toastMessage(bossUser.getFullName());
                customDialogEvent(view, "Vil du ændrer det?", bossUser.getFullName());
            }
            else if (str.contains("Email: "))
            {
                //toastMessage(bossUser.getEmail());
                customDialogEvent(view, "Vil du ændrer det?", bossUser.getEmail());
            }
            else if (str.contains("Branch: "))
            {
                //toastMessage(bossUser.getBranch());
                customDialogEvent(view, "Vil du ændrer det?", bossUser.getBranch());
            }
            else if (str.contains("CVR: "))
            {
                //toastMessage(bossUser.getCVR());
                customDialogEvent(view, "Vil du ændrer det?", bossUser.getCVR());
            }
        }
    };

    private void customDialogEvent(View view, String message, String title)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_layout_two);
        dialog.setTitle(title);

        // Set custom dialog components - Text, image and button
        TextView titleView = (TextView) dialog.findViewById(R.id.dialogTxtTitle2);
        titleView.setText(title);

        final TextView text = (TextView) dialog.findViewById(R.id.dialogTxt2);
        text.setText(message);
        final View view1 = view;
        final String title1 = title;

        Button dialogBtnChange = (Button) dialog.findViewById(R.id.dialogBtnChange);
        dialogBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                changeDataDialog(view1, title1, 1);
            }
        });
        Button dialogBtnCancel = (Button) dialog.findViewById(R.id.dialogBtnCancel);
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void changeDataDialog(View view, String title, int number)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_change_data);
        dialog.setTitle(title);

        // Custom dialog components
        TextView titleView = (TextView) dialog.findViewById(R.id.dialogTxtTitle3);
        titleView.setText(title);

        final EditText editNameDialog = (EditText) dialog.findViewById(R.id.editNameDialog);
        final EditText editMailDialog = (EditText) dialog.findViewById(R.id.editMailDialog);
        final EditText editBranchDialog = (EditText) dialog.findViewById(R.id.editBranchDialog);
        final EditText editCvrDialog = (EditText) dialog.findViewById(R.id.editCvrDialog);

        editNameDialog.setText(bossUser.getFullName());
        editMailDialog.setText(bossUser.getEmail());
        editBranchDialog.setText(bossUser.getBranch());
        editCvrDialog.setText(bossUser.getCVR());


        Button dialogBtnChange = (Button) dialog.findViewById(R.id.dialogBtnChange1);
        dialogBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method for updating data in Firebase
                dialog.dismiss();
                updateData(editNameDialog.getText().toString().trim(),
                        editMailDialog.getText().toString().trim(),
                        editBranchDialog.getText().toString().trim(),
                        editCvrDialog.getText().toString().trim());
            }
        });
        Button dialogBtnCancel = (Button) dialog.findViewById(R.id.dialogBtnCancel1);
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateData(String dialogName, String dialogMail, String dialogBranch, String dialogCvr)
    {
        if (!userType)
        {
            BossUser tempUser = new BossUser();
            tempUser.setFullName(dialogName);
            tempUser.setEmail(dialogMail);
            tempUser.setBranch(dialogBranch);
            tempUser.setCVR(dialogCvr);
            tempUser.setRegUser(bossUser.isRegUser());
            tempUser.setUserID(bossUser.getUserID());

            myChildRef.setValue(tempUser, new DatabaseReference.CompletionListener() {

                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null)
                    {
                        // Data could not be updated
                        toastMessage("Ændring ikke successful");
                    }
                    else
                    {
                        // Data updated
                        toastMessage("Ændring successful");
                    }
                }
            });
        }
        else
        {
            RegularUser tempUser = new RegularUser();
            tempUser.setFullName(dialogName);
            tempUser.setEmail(dialogMail);
            tempUser.setRegUser(bossUser.isRegUser());
            tempUser.setUserID(bossUser.getUserID());

            myChildRef.setValue(tempUser, new DatabaseReference.CompletionListener() {

                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null)
                    {
                        // Data could not be updated
                        toastMessage("Ændring ikke successful");
                    }
                    else
                    {
                        // Data updated
                        toastMessage("Ændring successful");
                    }
                }
            });
        }


        if (dialogMail.matches(bossUser.getEmail()))
        {
            // Same Email
            //toastMessage("Same email");
        }
        else
        {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.getCurrentUser().updateEmail(dialogMail);
            //toastMessage("Different email");
        }
    }

    private void changePassword()
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_change_pass);
        dialog.setTitle("Skift Kode");

        // Custom dialog components
        TextView titleView = (TextView) dialog.findViewById(R.id.dialogTxtTitle4);
        titleView.setText("Skift Kode");

        final EditText editPassDialog = (EditText) dialog.findViewById(R.id.editPassDialog);
        final EditText editPassDialogRepeat = (EditText) dialog.findViewById(R.id.editPassDialogRepeat);

        Button dialogBtnChange = (Button) dialog.findViewById(R.id.dialogBtnChange2);
        dialogBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method for updating data in Firebase
                if ((editPassDialog.getText().toString().trim())
                        .equals(editPassDialogRepeat.getText().toString().trim()))
                {
                    // update password
                    dialog.dismiss();
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.getCurrentUser().updatePassword(editPassDialog.getText().toString().trim());
                    toastMessage("Kode ændret");
                }
                else
                {
                    // passwords not the same
                    toastMessage("Koderne er ikke de samme");
                }
            }
        });
        Button dialogBtnCancel = (Button) dialog.findViewById(R.id.dialogBtnCancel2);
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // This is the drop down menu with Help, Settings and About page buttons ----------------------------------
    private AdapterView.OnItemSelectedListener dropDownListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            String help = "Hjælp";
            String settings = "Indstillinger";
            String about = "Om";
            String signOut = "Log ud";
            String defaultItem = "Vælg en";
            if (parent.getItemAtPosition(position).equals(help))
            {
                toastMessage(help + " valgt");
            }
            else if (parent.getItemAtPosition(position).equals(settings))
            {
                toastMessage(settings + " valgt");
            }
            else if (parent.getItemAtPosition(position).equals(about))
            {
                toastMessage(about + " valgt");
            }
            else if (parent.getItemAtPosition(position).equals(defaultItem))
            {
                // This is the default "Select One"
                //toastMessage("Du valgte en");
            }
            else if (parent.getItemAtPosition(position).equals(signOut))
            {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
                    toastMessage("Du er nu logget ud");
                }
                else
                {
                    toastMessage("Du er ikke logget ind");
                    finish();
                    startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
                }
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
