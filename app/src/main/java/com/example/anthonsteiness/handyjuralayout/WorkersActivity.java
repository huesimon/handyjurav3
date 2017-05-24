package com.example.anthonsteiness.handyjuralayout;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anthonsteiness.handyjuralayout.objects.BossUser;
import com.example.anthonsteiness.handyjuralayout.objects.RegularUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WorkersActivity extends AppCompatActivity
{
    private static final String TAG = "WorkersActivity";

    private String title = "Medarbejdere";

    TextView textView;
    ListView listView;

    // Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRootRef;
    private DatabaseReference myUserIDRef;
    private DatabaseReference myRegUserRef;
    private String userID;
    private FirebaseUser fbUser;

    private Activity context;
    //private TextView nameView, emailView, branchView, cvrView;

    // Buttons and stuff from app_bar class
    ImageButton searchBtn;
    Spinner helpDropDown;
    ArrayAdapter<CharSequence> adapter2;
    TextView titleBar;
    EditText searchBar;


    List<RegularUser> userList;
    List<String> stringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        textView = (TextView) findViewById(R.id.textViewCoworkers);
        listView = (ListView) findViewById(R.id.listViewCoworkers);
        listView.setOnItemClickListener(itemClickListener);

        userList = new ArrayList<>();
        stringArray = new ArrayList<>();

        // Firebase declaration stuff
        firebaseAuth = FirebaseAuth.getInstance();
        // Check if Firebase is already logged in to
        if (firebaseAuth.getCurrentUser() != null)
        {
            // The Firebase is logged in to

        }



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRootRef = mFirebaseDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        fbUser = user;
        userID = user.getUid();

        myRootRef = mFirebaseDatabase.getReference();
        myUserIDRef = mFirebaseDatabase.getReference(userID);
        myRegUserRef = mFirebaseDatabase.getReference(userID + "/RegularUsers");

        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //toastMessage("Successfully signed in using: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    //toastMessage("Successfully signed out from: " + user.getEmail());
                }
                // ...
            }
        };

        myRegUserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                showData(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });



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

    private void showData(DataSnapshot dataSnapshot)
    {
        // For loop to iterate through all the snapshots of the database
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            RegularUser regUser = ds.getValue(RegularUser.class);

            // This arrayList is made for later usage
            // At some point we want the users to be clickable. And we might need all the users
            // information to show when clicked.
            // And the users are added the same time as the String name. (This is thoughts. don't know if possible)
            userList.add(regUser);

            String str = regUser.getFullName();
            stringArray.add(str);

        }

        ArrayAdapter adapter = new ArrayAdapter(WorkersActivity.this, android.R.layout.simple_list_item_1, stringArray);
        listView.setAdapter(adapter);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            switch(view.getId())
            {
                case R.id.searchbtn:
                    toastMessage("Search function not yet implemented..");
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //for (RegularUser testUser : userList)
            //{
                //toastMessage(testUser.getFullName());
            //}

            RegularUser testUser = userList.get(position);
            dialogEvent(view, testUser.getEmail(), testUser.getFullName());
        }
    };

    private void dialogEvent(View view, String mail, String name)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WorkersActivity.this);
        alertDialog.setMessage(mail).setCancelable(true);

        AlertDialog alert = alertDialog.create();
        alert.setTitle(name);
        alert.show();
    }

    // This is the drop down menu with Help, Settings and About page buttons ----------------------------------
    private AdapterView.OnItemSelectedListener dropDownListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            String help = "Help";
            String settings = "Settings";
            String about = "About";
            //String userInfo = "User Info";
            String signOut = "Sign Out";
            String defaultItem = "Select one";
            if (parent.getItemAtPosition(position).equals(help))
            {
                toastMessage(help + " selected");
            }
            else if (parent.getItemAtPosition(position).equals(settings))
            {
                toastMessage(settings + " selected");
            }
            else if (parent.getItemAtPosition(position).equals(about))
            {
                toastMessage(about + " selected");
            }
            else if (parent.getItemAtPosition(position).equals(defaultItem))
            {
                // This is the default "Select One"
                //Toast.makeText(MainActivity.this, "Default selected", Toast.LENGTH_SHORT).show();
            }
//            else if (parent.getItemAtPosition(position).equals(userInfo))
//            {
//                // Open user information activity.
//                toastMessage("You are already on this page.");
//            }
            else if (parent.getItemAtPosition(position).equals(signOut))
            {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    finish();
                    startActivity(new Intent(WorkersActivity.this, LoginActivity.class));
                    firebaseAuth.signOut();
                    toastMessage("Successfully signed out");
                }
                else
                {
                    toastMessage("You are not signed in");
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
