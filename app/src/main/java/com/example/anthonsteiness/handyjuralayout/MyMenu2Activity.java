package com.example.anthonsteiness.handyjuralayout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyMenu2Activity extends AppCompatActivity
{

    private String title = "Min Menu";

    private Button tasksBtn, makeTaskBtn, coworkersBtn, contractsBtn, userInfoBtn, signOutBtn;
    private int height;
    private int width;
    private String str;
    private RelativeLayout relativeLayout;
    ViewGroup.MarginLayoutParams btn1Params, btn2Params, btn3Params, btn4Params, btn5Params, btn6Params;

    // Buttons and stuff from app_bar class
    private ImageButton searchBtn;
    private Spinner helpDropDown;
    private ArrayAdapter<CharSequence> adapter;

    private TextView titleBar;
    private EditText searchBar;
    private String saveSearch = "";

    // Firebase stuff
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu2);

        height = getWindowManager().getDefaultDisplay().getHeight();
        width = getWindowManager().getDefaultDisplay().getWidth();

        // Fierbase declaration stuff
        firebaseAuth = FirebaseAuth.getInstance();
        // Check if Firebase is already logged in to
        if (firebaseAuth.getCurrentUser() != null)
        {
            // The Firebase is already logged in to
        }
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser fbUser = firebaseAuth.getCurrentUser();
                if (fbUser != null)
                {
                    //toastMessage("Successfully signed in with " + fbUser.getEmail());
                }
                else
                {
                    // Could display not signed in. But might cause toasting issues...
                }
            }
        };


        tasksBtn = (Button) findViewById(R.id.btn11);
        makeTaskBtn = (Button) findViewById(R.id.btn22);
        coworkersBtn = (Button) findViewById(R.id.btn33);
        contractsBtn = (Button) findViewById(R.id.btn44);
        userInfoBtn = (Button) findViewById(R.id.btn55);
        signOutBtn = (Button) findViewById(R.id.btn66);

        tasksBtn.setOnClickListener(buttonClickListener);
        makeTaskBtn.setOnClickListener(buttonClickListener);
        coworkersBtn.setOnClickListener(buttonClickListener);
        contractsBtn.setOnClickListener(buttonClickListener);
        userInfoBtn.setOnClickListener(buttonClickListener);
        signOutBtn.setOnClickListener(buttonClickListener);

        btn1Params = (ViewGroup.MarginLayoutParams) tasksBtn.getLayoutParams();
        btn2Params = (ViewGroup.MarginLayoutParams) makeTaskBtn.getLayoutParams();
        btn3Params = (ViewGroup.MarginLayoutParams) coworkersBtn.getLayoutParams();
        btn4Params = (ViewGroup.MarginLayoutParams) contractsBtn.getLayoutParams();
        btn5Params = (ViewGroup.MarginLayoutParams) userInfoBtn.getLayoutParams();
        btn6Params = (ViewGroup.MarginLayoutParams) signOutBtn.getLayoutParams();

        // Everything here is from app_bar class -----------------
        searchBtn = (ImageButton) findViewById(R.id.searchbtn);
        searchBtn.setOnClickListener(buttonClickListener);

        searchBar = (EditText) findViewById(R.id.searchBar);
        titleBar = (TextView) findViewById(R.id.titleBar);

        helpDropDown = (Spinner) findViewById(R.id.helpDropDown);
        adapter = ArrayAdapter.createFromResource(this, R.array.settingSelection, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        helpDropDown.setAdapter(adapter);
        helpDropDown.setOnItemSelectedListener(dropDownListener);
        titleBar.setText("Min Menu");

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
                case R.id.btn11: // Tasks button
                    break;
                case R.id.btn22: // Add task Button
                    startActivity(new Intent(MyMenu2Activity.this, CreateTaskActivity.class));
                    break;
                case R.id.btn33: // Workers button
                    // I will make a new method in WorkersActivty that get's called if RegularUser = true.
                    // But for now it doesn't do anything, so the button is disabled.
                    startActivity(new Intent(MyMenu2Activity.this, WorkersActivity.class));
                    break;
                case R.id.btn44: // Contracts Button
                    // This button opens up a page that should display all the different Contract templates
                    startActivity(new Intent(MyMenu2Activity.this, ContractActivity.class));
                    break;
                case R.id.btn55: // User Info Button
                    // Open user information activity.
                    if (firebaseAuth.getCurrentUser() != null)
                    {
                        // The Firebase is already logged in to
                        startActivity(new Intent(MyMenu2Activity.this, UserInfoActivity.class));
                    }
                    else
                    {
                        toastMessage("Please login to access this");
                    }
                    break;
                case R.id.btn66: // Sign Out Button
                    if (firebaseAuth.getCurrentUser() != null)
                    {
                        // The Firebase is logged in to
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(MyMenu2Activity.this, MainActivity.class));
                        toastMessage("Successfully signed out");
                    }
                    break;
            }
        }
    };

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
                    // The Firebase is logged in to
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(MyMenu2Activity.this, MainActivity.class));
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

    private void checkScreenReso()
    {

        if (height>=1790)
        {
            // For 5.2" screen
            // Do nothing, it is designed for 5.2" - 6"

            if (height>=1800)
            {
                // For 7" Screen
                setBtnParams(105, 400,450);

                if (height>=1950)
                {
                    // For 9" Screen
                    setBtnParams(105, 480, 640);

                    if (height>=2390)
                    {
                        // For 6" Screen
                        setBtnParams(105, 500, 600);

                        if (height>=2460)
                        {
                            // For 10" Screen
                            setBtnParams(105, 580, 750);
                        }
                    }
                }
            }
        }
        if (height>=1920 && height<=1920)
        {
            // for 1920x1080 screens
            setBtnParams(45, 400, 485);
        }


    }

    private void setBtnParams(int sides, int btnHeight, int btnWidth)
    {
        btn1Params.height = btnHeight;
        btn1Params.width = btnWidth;
        tasksBtn.setLayoutParams(btn1Params);

        btn2Params.rightMargin = sides;
        btn2Params.height = btnHeight;
        btn2Params.width = btnWidth;
        makeTaskBtn.setLayoutParams(btn2Params);

        btn3Params.leftMargin = sides;
        btn3Params.height = btnHeight;
        btn3Params.width = btnWidth;
        coworkersBtn.setLayoutParams(btn3Params);

        btn4Params.rightMargin = sides;
        btn4Params.height = btnHeight;
        btn4Params.width = btnWidth;
        contractsBtn.setLayoutParams(btn4Params);

        btn5Params.height = btnHeight;
        btn5Params.width = btnWidth;
        userInfoBtn.setLayoutParams(btn5Params);

        btn6Params.rightMargin = sides;
        btn6Params.height = btnHeight;
        btn6Params.width = btnWidth;
        signOutBtn.setLayoutParams(btn6Params);
    }
}
