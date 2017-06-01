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

import com.example.anthonsteiness.handyjuralayout.objects.BossUser;
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

public class RegisterActivity extends AppCompatActivity {
    // This is a commit test 16-05-2017

    String title = "HandyJura";

    Spinner branchDrop;
    TextView chosenBusiness;
    EditText mEmail, mEmailRepeat, mPass, mPassRepeat, mName, mCVR;
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
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    // This is needed to write or read data from database
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        height = getWindowManager().getDefaultDisplay().getHeight();
        width = getWindowManager().getDefaultDisplay().getWidth();

        // Fierbase declaration stuff
        firebaseAuth = FirebaseAuth.getInstance();
        // Check if Firebase is already logged in to
        if (firebaseAuth.getCurrentUser() != null)
        {
            // The Firebase is already logged in to

        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        // This listens for any updates made in the database it referer to.
        // It is called every time data is changed in the database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mEmail = (EditText) findViewById(R.id.editEmailReg);
        mEmailRepeat = (EditText) findViewById(R.id.editEmailRegRepeat);
        mPass = (EditText) findViewById(R.id.editPasswordReg);
        mPassRepeat = (EditText) findViewById(R.id.editPasswordRegRepeat);
        mName = (EditText) findViewById(R.id.editName1);
        mCVR = (EditText) findViewById(R.id.editCVR);

        regBtn = (Button) findViewById(R.id.registerBtn2);
        regBtn.setOnClickListener(buttonClickListener2);
        marginParams = (ViewGroup.MarginLayoutParams) regBtn.getLayoutParams();

        chosenBusiness = (TextView) findViewById(R.id.chosenBusiText);

        branchDrop = (Spinner) findViewById(R.id.branchSpinner);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.branches, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchDrop.setAdapter(adapter2);
        branchDrop.setOnItemSelectedListener(dropDownListener);

        // Everything here is from app_bar class -----------------
        searchBtn = (ImageButton) findViewById(R.id.searchbtn);
        searchBtn.setOnClickListener(buttonClickListener2);

        searchBar = (EditText) findViewById(R.id.searchBar);
        titleBar = (TextView) findViewById(R.id.titleBar);

        helpDropDown = (Spinner) findViewById(R.id.helpDropDown);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.settingSelection, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        helpDropDown.setAdapter(adapter2);
        helpDropDown.setOnItemSelectedListener(dropDownListener);
        titleBar.setText("HandyJura");

        checkScreenReso();
    }


    private View.OnClickListener buttonClickListener2 = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            switch(view.getId())
            {
                case R.id.registerBtn2:
                    //Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                    userRegister();
                    break;
                case R.id.searchbtn:
                    toastMessage("Nothing to search for here...", true);
                    break;
            }
        }
    };

    private void userRegister()
    {
        String email = mEmail.getText().toString().trim();
        String pass  = mPass.getText().toString().trim();
        String emailRepeat = mEmailRepeat.getText().toString().trim();
        String passRepeat  = mPassRepeat.getText().toString().trim();
        String branch = chosenBusiness.getText().toString().trim();

        //Toast.makeText(RegisterActivity.this, "Email: " + email + "\nPass: " + pass, Toast.LENGTH_SHORT).show();

        if((TextUtils.isEmpty(email)) || (TextUtils.isEmpty(pass)))
        {
            // Email or Password empty
            Toast.makeText(RegisterActivity.this, "No blank fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.equals(emailRepeat))
        {
            toastMessage("Email is not the same", true);
            return;
        }
        if (!pass.equals(passRepeat))
        {
            toastMessage("Password is not the same", true);
            return;
        }
        if(branch.equals("Choose Business"))
        {
            toastMessage("Please choose the line of business\nyour company primarily provide.", false);
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
                            toastMessage("User Registration successful\nPlease Login", true);
                            // Create boss user and save it's User Information in his database section.
                            createBossUser();
                            // Finish the RegisterActivity and start up the MyFrontPageActivity (Not made yet) instead of main
                            // It automatically logs you in when you register.
                            finish();
                            //startActivity(new Intent(LoginActivity.this, MyFronPageActivity.class));
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                        else
                        {
                            // Not successful
                            toastMessage("User Registration not successful", true);
                        }
                    }
                });


    }

    private void createBossUser()
    {
        // This saves all the user information under his userID and again under UserInfo.
        // A user shall only be able to store information under his own UserID from Firebase.
        // So when a BossUser makes a user, the information from that user shall be stored
        // under both BossUser UserID, but also under RegularUser's UserID.
        String email = mEmail.getText().toString().trim();
        String fullName = mName.getText().toString().trim();
        String CVR = mCVR.getText().toString().trim();
        String branch = chosenBusiness.getText().toString().trim();

        // No need to check text fields conditions
        // this method is only called if the user registration is successful
        // So if userRegister() method is unsuccessful. this will never be called
        BossUser bossUser = new BossUser(CVR, fullName, email, branch, false);
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        String userID = fbUser.getUid();
        bossUser.setUserID(userID);
        String childInfo = "UserInfo";
        databaseReference.child(userID).child(childInfo).setValue(bossUser);
        // Sign the user out because he has to login from the LoginActivity.
        // Dont know if this featere is necessary, but it's easy to remove if not.
        firebaseAuth.signOut();
    }

    private void checkScreenReso()
    {
        if (height>=1790)
        {
            // For 5.2" screen
            marginParams.topMargin = 205;
            regBtn.setLayoutParams(marginParams);

            if (height>=1800)
            {
                // For 7" Screen
                marginParams.topMargin = 585;
                regBtn.setLayoutParams(marginParams);

                if (height>=1950)
                {
                    // For 9" Screen
                    marginParams.topMargin = 715;
                    regBtn.setLayoutParams(marginParams);

                    if (height>=2390)
                    {
                        // For 6" & 5.5" Screen
                        marginParams.topMargin = 260;
                        regBtn.setLayoutParams(marginParams);


                        if (height>=2460)
                        {
                            // For 10" Screen
                            marginParams.topMargin = 1225;
                            regBtn.setLayoutParams(marginParams);
                            //loginBtn2.setHeight(400); -- IT WONT CHANGE FOR SOME REASON!?!?
                        }
                    }
                }
            }
        }

        if (height>=1920 && height<=1920)
        {
            // For 1920x1080 screens
            marginParams.topMargin = 135;
            regBtn.setLayoutParams(marginParams);
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
            String defaultItem2 = "Choose Business";
            String tømrer = "Tømrer";
            String murer = "Murer";
            String glar = "Glarmester";
            String skilt = "Skiltemager";
            String elek = "Elektriker";
            String vvs = "VVS";
            String maler = "Maler";
            String smedie = "Smedie";
            String snedker = "Snedker";
            String gulv = "Gulvlægger";

            String help = "Hjælp";
            String settings = "Indstillinger";
            String about = "Om";
            String signOut = "Log ud";
            String defaultItem = "Vælg en";
            if (parent.getItemAtPosition(position).equals(help))
            {
                toastMessage(help + " valgt", true);
            }
            else if (parent.getItemAtPosition(position).equals(settings))
            {
                toastMessage(settings + " valgt", true);
            }
            else if (parent.getItemAtPosition(position).equals(about))
            {
                toastMessage(about + " valgt", true);
            }
            else if (parent.getItemAtPosition(position).equals(defaultItem))
            {
                // This is the default "Select One"
                //toastMessage("Du valgte en", true);
            }
            else if (parent.getItemAtPosition(position).equals(signOut))
            {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    firebaseAuth.signOut();
                    toastMessage("Du er nu logget ud", true);
                }
                else
                {
                    toastMessage("Du er ikke logget ind", true);
                }
            }
            else if (parent.getItemAtPosition(position).equals(tømrer))
            {
                //Toast.makeText(RegisterActivity.this, tømrer + " selected", Toast.LENGTH_SHORT).show();
                chosenBusiness.setText(tømrer);
            }
            else if (parent.getItemAtPosition(position).equals(murer))
            {
                //Toast.makeText(RegisterActivity.this, murer + " selected", Toast.LENGTH_SHORT).show();
                chosenBusiness.setText(murer);
            }
            else if (parent.getItemAtPosition(position).equals(glar))
            {
                //Toast.makeText(RegisterActivity.this, glar + " selected", Toast.LENGTH_SHORT).show();
                chosenBusiness.setText(glar);
            }
            else if (parent.getItemAtPosition(position).equals(skilt))
            {
                //Toast.makeText(RegisterActivity.this, skilt + " selected", Toast.LENGTH_SHORT).show();
                chosenBusiness.setText(skilt);
            }
            else if (parent.getItemAtPosition(position).equals(elek))
            {
                //Toast.makeText(RegisterActivity.this, elek + " selected", Toast.LENGTH_SHORT).show();
                chosenBusiness.setText(elek);
            }
            else if (parent.getItemAtPosition(position).equals(vvs))
            {
                //Toast.makeText(RegisterActivity.this, vvs + " selected", Toast.LENGTH_SHORT).show();
                chosenBusiness.setText(vvs);
            }
            else if (parent.getItemAtPosition(position).equals(maler))
            {
                //Toast.makeText(RegisterActivity.this, maler + " selected", Toast.LENGTH_SHORT).show();
                chosenBusiness.setText(maler);
            }
            else if (parent.getItemAtPosition(position).equals(smedie))
            {
                //Toast.makeText(RegisterActivity.this, smedie + " selected", Toast.LENGTH_SHORT).show();
                chosenBusiness.setText(smedie);
            }
            else if (parent.getItemAtPosition(position).equals(snedker))
            {
                chosenBusiness.setText(snedker);
                //Toast.makeText(RegisterActivity.this, snedker + " selected", Toast.LENGTH_SHORT).show();
            }
            else if (parent.getItemAtPosition(position).equals(gulv))
            {
                chosenBusiness.setText(gulv);
                //Toast.makeText(RegisterActivity.this, gulv + " selected", Toast.LENGTH_SHORT).show();
            }
            else if (parent.getItemAtPosition(position).equals(defaultItem2))
            {
                chosenBusiness.setText(defaultItem2);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}
