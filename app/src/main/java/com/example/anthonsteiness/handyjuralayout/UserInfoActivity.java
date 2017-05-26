package com.example.anthonsteiness.handyjuralayout;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
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

    private String title = "User Info";

    TextView textView;
    ListView listView;

    // Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRootRef;
    private DatabaseReference myChildRef;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        textView = (TextView) findViewById(R.id.tvUserInfo);
        listView = (ListView) findViewById(R.id.listView);

        // Firebase declaration stuff
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRootRef = mFirebaseDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        fbUser = user;
        userID = user.getUid();
        myChildRef = mFirebaseDatabase.getReference(userID);

        // Check if Firebase is already logged in to
        if (firebaseAuth.getCurrentUser() != null)
        {
            // The Firebase is logged in to

        }

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

        toastMessage("hello " + fbUser.getEmail());

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
            }
        }
    };

    private void showData(DataSnapshot dataSnapshot)
    {
        // For loop to iterate through all the snapshots of the database
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            BossUser bossUser = new BossUser();

            bossUser = ds.getValue(BossUser.class);

            //nameView.setText(bossUser.getFullName());
            //emailView.setText(bossUser.getEmail());
            //branchView.setText(bossUser.getBranch());
            //cvrView.setText(bossUser.getCVR());


            ArrayList<String> array = new ArrayList<>();

            array.add("Name:       " + bossUser.getFullName());
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
        }
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
                finish();
                startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                firebaseAuth.signOut();
                toastMessage("Successfully signed out");
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
