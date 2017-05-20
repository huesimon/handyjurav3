package com.example.anthonsteiness.handyjuralayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn2;
    EditText mailText;
    EditText passText;
    TextView forgotText;
    int height;
    int width;

    ViewGroup.MarginLayoutParams marginParams;

    // Firebase stuff
    private FirebaseAuth firebaseAuth;
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

        mailText = (EditText) findViewById(R.id.editEmail);
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
    }

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
                            finish();
                            //startActivity(new Intent(LoginActivity.this, MyMenuActivity.class));
                            startActivity(new Intent(LoginActivity.this, MyMenuActivity.class));
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
                            loginBtn2.setLayoutParams(marginParams);
                            //loginBtn2.setHeight(400); -- IT WONT CHANGE FOR SOME REASON!?!?
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
//                if (firebaseAuth.getCurrentUser() != null)
//                {
//                    // The Firebase is already logged in to
//                    startActivity(new Intent(LoginActivity.this, UserInfoActivity.class));
//                }
//                else
//                {
//                    toastMessage("Please login to access this");
//                }
//            }
            else if (parent.getItemAtPosition(position).equals(signOut))
            {
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
