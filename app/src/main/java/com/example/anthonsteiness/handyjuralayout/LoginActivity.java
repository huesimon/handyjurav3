package com.example.anthonsteiness.handyjuralayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anthonsteiness.handyjuralayout.objects.RegularUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn2;
    EditText mailText;
    EditText passText;
    TextView forgotText;
    int height;
    int width;

    ViewGroup.MarginLayoutParams marginParams;

    // Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myUserIDRef;
    private String userID;
    private FirebaseUser fbUser;
    private ProgressDialog progressDialog;

    // Buttons and stuff from app_bar class
    ImageButton searchBtn;
    Spinner helpDropDown;
    ArrayAdapter<CharSequence> adapter2;
    TextView titleBar;
    EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Fierbase declaration stuff
        firebaseAuth = FirebaseAuth.getInstance();
        // Check if Firebase is already logged in to
        if (firebaseAuth.getCurrentUser() != null)
        {
            // The Firebase is already logged in to

        }


        progressDialog = new ProgressDialog(this);

        height = getWindowManager().getDefaultDisplay().getHeight();
        width = getWindowManager().getDefaultDisplay().getWidth();

        loginBtn2 = (Button) findViewById(R.id.loginBtn2);
        loginBtn2.setOnClickListener(buttonClickListener);
        marginParams = (ViewGroup.MarginLayoutParams) loginBtn2.getLayoutParams();

        mailText = (EditText) findViewById(R.id.editEmail1);
        passText = (EditText) findViewById(R.id.editPassword);

        mailText.setOnFocusChangeListener(focusOnMail);
        passText.setOnFocusChangeListener(focusOnPass);

        forgotText = (TextView) findViewById(R.id.forgotPassText);
        forgotText.setOnClickListener(buttonClickListener);

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
        titleBar.setText("HandyJura");

        checkScreenReso();

        firebaseAuth.addAuthStateListener(mAuthListener);

    }

    // This listens for any change made in Authentication. The code is run every time the user logs out or in.
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener()
    {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
        {
            if (firebaseAuth.getCurrentUser() != null)
            {
                //toastMessage("Signed in with " + fbUser.getEmail());

                // This is to store the info off the user that is logged in
                fbUser = firebaseAuth.getCurrentUser();
                userID = fbUser.getUid();
                // This is to instanziate the Database reference. This is done here because, it's only now it is possible.
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myUserIDRef = mFirebaseDatabase.getReference(userID);

                // This is the Listener for the Database reference to the UserID
                myUserIDRef.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        // This runs through every snapshot of the data under this reference.
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            RegularUser regUser = new RegularUser();

                            // Capturing the stored Data, and saves it in a new RegularUser
                            regUser = ds.getValue(RegularUser.class);

                            // To check if the RegularUser = true or false.
                            boolean check = regUser.isRegUser();
                            if (!check)
                            {
                                // this is the BossUser
                                //toastMessage("check = false");

                                finish();
                                startActivity(new Intent(LoginActivity.this, MyMenuActivity.class));
                            }
                            else
                            {
                                // This is the RegularUser
                                finish();
                                startActivity(new Intent(LoginActivity.this, MyMenuActivity.class));
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });

            } else
            {

                //toastMessage("Not signed in");
            }
        }
    };

    private View.OnClickListener buttonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            switch(view.getId())
            {
                case R.id.loginBtn2:
                    userLogin();
                    //Toast.makeText(LoginActivity.this, "You are trying to Login...", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.forgotPassText:
                    Toast.makeText(LoginActivity.this, "You have forgot your password...", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.searchbtn:
                    toastMessage("Nothing to search for here...");
                    break;
            }
        }
    };

    private void userLogin()
    {
        String email = mailText.getText().toString().trim();
        String pass  = passText.getText().toString().trim();

        if((TextUtils.isEmpty(email)) || (TextUtils.isEmpty(pass)))
        {
            // Email or Password empty
            Toast.makeText(LoginActivity.this, "Please enter valid values in text fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // If EditText not empty it comes to here
        progressDialog.setMessage("Checking user information...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful())
                        {
                            // The login is successful
                            //toastMessage("User login successful");

                            // Finish the LoginActivity and start up the MyFrontPageActivity (Not made yet) instead of main
                            // This is done in the firebase auth listener. Because we need to check the logged in users UserType, stored in the Database.
                        }
                        else
                        {
                            // Not successful
                            Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private void checkScreenReso()
    {
        if (height>=1790)
        {
            // For 5.2" screen
            marginParams.topMargin = 400;
            loginBtn2.setLayoutParams(marginParams);

            if (height>=1800)
            {
                // For 7" Screen
                marginParams.topMargin = 740;
                loginBtn2.setLayoutParams(marginParams);

                if (height>=1950)
                {
                    // For 9" Screen
                    marginParams.topMargin = 870;
                    loginBtn2.setLayoutParams(marginParams);

                    if (height>=2390)
                    {
                        // For 6" & 5.5" Screen
                        marginParams.topMargin = 535;
                        loginBtn2.setLayoutParams(marginParams);


                        if (height>=2460)
                        {
                            // For 10" Screen
                            marginParams.topMargin = 1380;
                            //marginParams.height = 400; -- This works
                            loginBtn2.setLayoutParams(marginParams);
                        }
                    }
                }
            }
        }

        if (height>=1920 && height<=1920)
        {
            // For 1920x1080 screens
            marginParams.topMargin = 380;
            loginBtn2.setLayoutParams(marginParams);
        }
    }


    private View.OnFocusChangeListener focusOnMail = new View.OnFocusChangeListener()
    {
        @Override
        public void onFocusChange(View view, boolean hasFocus)
        {
            if (hasFocus)
            {
                mailText.setHint("");
            }
            else
            {
                mailText.setHint("Email Address");
                //test simon commit
            }
        }
    };

    private View.OnFocusChangeListener focusOnPass = new View.OnFocusChangeListener()
    {
        @Override
        public void onFocusChange(View view, boolean hasFocus)
        {
            if (hasFocus)
            {
                passText.setHint("");
            }
            else
            {
                passText.setHint("Password");
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

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
