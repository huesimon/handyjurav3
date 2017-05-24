package com.example.anthonsteiness.handyjuralayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class AddWorkerActivity extends AppCompatActivity {

    private static final String TAG = "AddWorkerActivity";

    String title = "HandyJura";

    EditText mEmail, mEmailRepeat, mPass, mPassRepeat, mName;
    Button regBtn;
    int height, width;
    ViewGroup.MarginLayoutParams marginParams;

    // Buttons and stuff from app_bar class
    ImageButton searchBtn;
    Spinner helpDropDown;
    ArrayAdapter<CharSequence> adapter2;
    TextView titleBar;
    EditText searchBar;

    // Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRootRef;
    private DatabaseReference myChildRef;
    private String userID;
    private FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);

        height = getWindowManager().getDefaultDisplay().getHeight();
        width = getWindowManager().getDefaultDisplay().getWidth();

        // Firebase declaration stuff
        firebaseAuth = FirebaseAuth.getInstance();
        // Check if Firebase is already logged in to
        if (firebaseAuth.getCurrentUser() != null)
        {
            // The Firebase is logged in to

        }



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        fbUser = user;
        userID = user.getUid();

        myRootRef = mFirebaseDatabase.getReference();
        myChildRef = mFirebaseDatabase.getReference(userID);

        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //toastMessage("Successfully signed in using: " + user.getEmail());
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    //toastMessage("Successfully signed out from: " + user.getEmail());
                }
                // ...
            }
        };

        mEmail = (EditText) findViewById(R.id.editEmailReg2);
        mEmailRepeat = (EditText) findViewById(R.id.editEmailRegRepeat2);
        mPass = (EditText) findViewById(R.id.editPasswordReg2);
        mPassRepeat = (EditText) findViewById(R.id.editPasswordRegRepeat2);
        mName = (EditText) findViewById(R.id.editName2);

        regBtn = (Button) findViewById(R.id.registerBtn3);
        regBtn.setOnClickListener(buttonClickListener);
        marginParams = (ViewGroup.MarginLayoutParams) regBtn.getLayoutParams();

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
        titleBar.setText("Tilføj medarbejder");

        checkScreenReso();

        //toastMessage(userID + " = userID\n" + fbUser.getEmail() + " = email", false);
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            switch(view.getId())
            {
                case R.id.registerBtn3:
                    //Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                    regUserRegister();
                    break;
                case R.id.searchbtn:
                    toastMessage("Nothing to search for here...", true);
                    break;
            }
        }
    };

    private void regUserRegister()
    {
        final String email = mEmail.getText().toString().trim();
        final String pass  = mPass.getText().toString().trim();
        final String emailRepeat = mEmailRepeat.getText().toString().trim();
        final String passRepeat  = mPassRepeat.getText().toString().trim();

        //Toast.makeText(RegisterActivity.this, "Email: " + email + "\nPass: " + pass, Toast.LENGTH_SHORT).show();

        if((TextUtils.isEmpty(email)) || (TextUtils.isEmpty(pass)))
        {
            // Email or Password empty
            toastMessage("Ingen blanke feldter", true);
            return;
        }
        if (!email.equals(emailRepeat))
        {
            toastMessage("Email er ikke den samme", true);
            return;
        }
        if (!pass.equals(passRepeat))
        {
            toastMessage("Koden er ikke den samme", true);
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Registration Successful
                            //toastMessage("Bruger registreret", true);

                            // Create Regular user and save it's User Information in his database section.
                            createRegUser(email, emailRepeat, pass, passRepeat);
                            //createReguUser();

                            firebaseAuth.signOut();
                            // As it logs in after registration,right now you'll have to log back in on BossUser.
                            finish();
                            startActivity(new Intent(AddWorkerActivity.this, LoginActivity.class));
                            toastMessage("Vær sød at logge på igen\nBeklager ulejligheden...", false);
                        }
                        else
                        {
                            // Not successful
                            toastMessage("Noget gik galt under registreringen\nPrøv igen", true);
                        }
                    }
                });


    }

    private void createReguUser()
    {
        //toastMessage(userID + " = userID\n" + fbUser.getEmail() + " = email", false);
        // fbUser is apperently changed at this point, but userID is still the old. so saving is no problem.


    }

    private void createRegUser(String mail, String mailR, String pass, String passR)
    {
        String name = mName.getText().toString().trim();
        String regUserID = firebaseAuth.getCurrentUser().getUid();
        //String bossUserInfo = "BossID = " + userID;
        //String regUserInfo = "RegUserID = " + regUserID;
        //toastMessage(bossUserInfo + "\n" + regUserInfo, false);

        RegularUser regUser = new RegularUser(name, mail, regUserID, userID, true);
        String childInfo = "UserInfo";
        String userTable = "RegularUsers";
        //myChildRef.child(userTable).child(regUserID).child(childInfo).setValue(regUser);
        myChildRef.child(userTable).child(regUserID).setValue(regUser);
        myRootRef.child(regUserID).child(childInfo).setValue(regUser);
    }


    private void checkScreenReso()
    {
        if (height>=1790)
        {
            // For 5.2" screen


            if (height>=1800)
            {
                // For 7" Screen


                if (height>=1950)
                {
                    // For 9" Screen


                    if (height>=2390)
                    {
                        // For 6" & 5.5" Screen



                        if (height>=2460)
                        {
                            // For 10" Screen

                            //loginBtn2.setHeight(400); -- IT WONT CHANGE FOR SOME REASON!?!?
                        }
                    }
                }
            }
        }

        if (height>=1920 && height<=1920)
        {
            // For 1920x1080 screens
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message, boolean length)
    {
        // If true - short message. If false - Long message
        if (length)
        {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
                toastMessage(help + " selected", true);
            }
            else if (parent.getItemAtPosition(position).equals(settings))
            {
                toastMessage(settings + " selected", true);
            }
            else if (parent.getItemAtPosition(position).equals(about))
            {
                toastMessage(about + " selected", true);
            }
//            else if (parent.getItemAtPosition(position).equals(userInfo))
//            {
//                // Open user information activity.
//                if (firebaseAuth.getCurrentUser() != null)
//                {
//                    // The Firebase is already logged in to
//                    startActivity(new Intent(RegisterActivity.this, UserInfoActivity.class));
//                }
//                else
//                {
//                    toastMessage("Please login to access this", true);
//                }
//            }
            else if (parent.getItemAtPosition(position).equals(signOut))
            {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    // The Firebase is logged in to
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(AddWorkerActivity.this, MainActivity.class));
                    toastMessage("Du er nu logget ud.", true);
                }

            }
            else if (parent.getItemAtPosition(position).equals(defaultItem))
            {
                // This is the default "Select One"
                //Toast.makeText(MainActivity.this, "Default selected", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}
