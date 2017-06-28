package com.example.anthonsteiness.handyjuralayout;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity {
    // Anthon's Commit test 18-05-2017 10:28
    // Anthon's Commit test 20-05-2017 13:10
    // Anthon's Commit test 26-05-2017 00:17

    String title = "HammerNemt";

    Button loginBtn;
    Button registerBtn;
    ImageView plumber;
    String str;

    int height;
    int width;

    // Buttons and stuff from app_bar class
    ImageButton searchBtn;
    Spinner helpDropDown;
    ArrayAdapter<CharSequence> adapter;

    TextView titleBar;
    EditText searchBar;

    // Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myUserIDRef;
    private String userID;
    private FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase declaration stuff
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        //myRootRef = mFirebaseDatabase.getReference();
        //myUserIDRef = mFirebaseDatabase.getReference(userID);


        // Check if Firebase is already logged in to
        if (firebaseAuth.getCurrentUser() != null)
        {
            // The Firebase is already logged in to



            fbUser = firebaseAuth.getCurrentUser();
            userID = fbUser.getUid();
            myUserIDRef = mFirebaseDatabase.getReference(userID);

            myUserIDRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    //showData(dataSnapshot);
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        RegularUser regUser = new RegularUser();

                        regUser = ds.getValue(RegularUser.class);

                        boolean check = regUser.isRegUser();
                        if (!check)
                        {
                            // this is the BossUser
                            //toastMessage("check = false");

                            finish();
                            Intent intent = new Intent(MainActivity.this, MyMenuActivity.class);
                            intent.putExtra("userType", check);
                            intent.putExtra("userID", regUser.getUserID());
                            startActivity(intent);
                        }
                        else
                        {
                            // This is the RegularUser
                            finish();
                            Intent intent = new Intent(MainActivity.this, MyMenuActivity.class);
                            intent.putExtra("userType", check);
                            intent.putExtra("bossID", regUser.getBossUserID());
                            intent.putExtra("userID", regUser.getUserID());
                            startActivity(intent);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });


        }

        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser fbUser = firebaseAuth.getCurrentUser();
                if (fbUser != null)
                {
                    toastMessage("Successfully signed in with " + fbUser.getEmail());
                }
                else
                {
                    // Could display not signed in. But might cause toasting issues...
                }
            }
        };



        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        plumber = (ImageView) findViewById(R.id.plumberIcon);

        plumber.setOnClickListener(buttonClickListener);
        loginBtn.setOnClickListener(buttonClickListener);
        registerBtn.setOnClickListener(buttonClickListener);

        height = getWindowManager().getDefaultDisplay().getHeight();
        width = getWindowManager().getDefaultDisplay().getWidth();

        // Everything here is from app_bar class -----------------
        searchBtn = (ImageButton) findViewById(R.id.searchbtn);
        searchBtn.setOnClickListener(buttonClickListener);

        searchBar = (EditText) findViewById(R.id.searchBar);
        titleBar = (TextView) findViewById(R.id.titleBar);

        helpDropDown = (Spinner) findViewById(R.id.helpDropDown);
        adapter = ArrayAdapter.createFromResource(this, R.array.settingSelection, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        helpDropDown.setAdapter(adapter);

        //helpDropDown.setOnItemClickListener(dropDownListener);
        helpDropDown.setOnItemSelectedListener(dropDownListener);
        titleBar.setText(title);

        checkScreenReso();

        // End of onCreate method
    }

    // This is the ButtonClickListener, it listens for multiple buttons. just give the case, the button id from xml.
    private View.OnClickListener buttonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            height = getWindowManager().getDefaultDisplay().getHeight();
            width = getWindowManager().getDefaultDisplay().getWidth();
            str = "Height: " + height + ", Width: " + width;

            switch(view.getId())
            {
                case R.id.loginBtn:
                    //Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                    //finish();
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    //toastMessage("Height: " +height + "\nWidht: " + width);

                    break;
                case R.id.registerBtn:
                    //Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                    //finish();
                    Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);

                    break;
                case R.id.searchbtn:
                    //Toast.makeText(MainActivity.this, "This is the search button", Toast.LENGTH_SHORT).show();

                    if (!titleBar.getText().equals(title))
                    {
                        searchBar.setHint("");
                        searchBar.setText("");
                        titleBar.setText(title);
                        searchBar.setInputType(InputType.TYPE_NULL);

                        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    }
                    else
                    {
                        searchBar.setHint("Search");
                        titleBar.setText("");
                        searchBar.setInputType(InputType.TYPE_CLASS_TEXT);

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
                    }


                    break;
                case R.id.plumberIcon:
                    Toast toast = Toast.makeText(MainActivity.this, "Hello, I'm the plumber!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 200);
                    toast.show();
                    if (!titleBar.getText().equals(title))
                    {
                        searchBar.setHint("");
                        searchBar.setText("");
                        titleBar.setText(title);
                        searchBar.setInputType(InputType.TYPE_NULL);

                        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    }
                    break;

            }
        }
    };

    private void checkScreenReso()
    {
        if (height>=1790)
        {
            // For 5.2" screen
            plumber.setImageResource(R.drawable.plumber);
            loginBtn.setHeight(305);
            registerBtn.setHeight(305);

            if (height>=1800)
            {
                // For 7" Screen
                plumber.setImageResource(R.drawable.plumberr_vtwo);
                loginBtn.setHeight(285);
                registerBtn.setHeight(285);

                if (height>=1950)
                {
                    // For 9" Screen
                    plumber.setImageResource(R.drawable.plumberrr);
                    loginBtn.setHeight(245);
                    registerBtn.setHeight(245);

                    if (height>=2390)
                    {
                        // For 6" Screen
                        plumber.setImageResource(R.drawable.plumber);
                        loginBtn.setHeight(385);
                        registerBtn.setHeight(385);

                        if (height>=2460)
                        {
                            // For 10" Screen
                            plumber.setImageResource(R.drawable.plumberrrr);
                            loginBtn.setHeight(315);
                            registerBtn.setHeight(315);
                        }
                    }
                }
            }
        }

        if (height>=1920 && height<=1920)
        {
            plumber.setImageResource(R.drawable.plumber);
            loginBtn.setHeight(295);
            registerBtn.setHeight(315);
        }
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
                    toastMessage("Du er nu logget ud");
                }
                else
                {
                    toastMessage("Du er ikke logget ind");
                }
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthListener != null)
        {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
