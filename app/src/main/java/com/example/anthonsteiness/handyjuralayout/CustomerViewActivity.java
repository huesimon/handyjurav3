package com.example.anthonsteiness.handyjuralayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anthonsteiness.handyjuralayout.objects.Customer;
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
import java.util.List;

public class CustomerViewActivity extends AppCompatActivity
{
    private String title = "Kunder";

    TextView textView, textView2, textView3;
    ListView customerView;
    ImageButton addWorkerBtn;
    ViewGroup.MarginLayoutParams marginParams;
    private int height, width;

    String body, header;

    List<Customer> customerList;
    List<String> stringArray;

    // Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myUserIDRef;
    private DatabaseReference myBossIDRef;
    private DatabaseReference customerTaskRef;
    private String bossID;
    private String userID;
    private FirebaseUser fbUser;
    private boolean userType;

    // Buttons and stuff from app_bar class
    ImageButton searchBtn;
    Spinner helpDropDown;
    ArrayAdapter<CharSequence> adapter2;
    TextView titleBar;
    EditText searchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        Intent intent = getIntent();
        userType = intent.getExtras().getBoolean("userType");
        bossID = intent.getExtras().getString("bossID");
        userID = intent.getExtras().getString("userID");
        //toastMessage(userType + userID);

        height = getWindowManager().getDefaultDisplay().getHeight();
        width = getWindowManager().getDefaultDisplay().getWidth();

        textView = (TextView) findViewById(R.id.textViewCoworkers);
        textView.setText("Kunder");
        customerView = (ListView) findViewById(R.id.listViewCoworkers);
        customerView.setOnItemClickListener(itemClickListener);
        addWorkerBtn = (ImageButton) findViewById(R.id.addWorkerBtn);
        addWorkerBtn.setOnClickListener(buttonClickListener);
        marginParams = (ViewGroup.MarginLayoutParams) addWorkerBtn.getLayoutParams();
        marginParams.height = 0;
        marginParams.width = 0;
        addWorkerBtn.setLayoutParams(marginParams);

        textView2 = (TextView) findViewById(R.id.myTasksGreyArea);
        textView3 = (TextView) findViewById(R.id.myUsersTasksGreyArea);
        //textView2.setBackgroundColor(0);
        //textView3.setBackgroundColor(0);
        textView2.setHeight(0);
        textView3.setHeight(0);

        customerList = new ArrayList<>();
        stringArray = new ArrayList<>();

        // Firebase declaration stuff
        firebaseAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        fbUser = user;
        //userID = user.getUid();
        //toastMessage(userID);

        myUserIDRef = mFirebaseDatabase.getReference(userID + "/Customers");
        myBossIDRef = mFirebaseDatabase.getReference(bossID + "/Customers");

        checkUserType();

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
                    toastMessage("Search function not yet implemented..");
                    break;
            }
        }
    };

    private void checkUserType()
    {
        if (!userType)
        {
            // This is boss user, userType = false
            myUserIDRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    showData(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
        else
        {
            // This is Regular user, userType = true
            myBossIDRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) { showData(dataSnapshot); }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    private void showData(DataSnapshot dataSnapshot)
    {
        customerList.clear();
        stringArray.clear();
        // For loop to iterate through all the snapshots of the database
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            //toastMessage("test");

            for (DataSnapshot ds2 : ds.getChildren())
            {
                Customer customer = ds2.getValue(Customer.class);

                String str = customer.getFullName();
                if (str != null)
                {
                    customerList.add(customer);
                    stringArray.add(str);
                    //toastMessage(str + " is not empty");
                }
                else
                {
                    //toastMessage(str + " is empty");
                }
            }

        }

        ArrayAdapter adapter = new ArrayAdapter(CustomerViewActivity.this, android.R.layout.simple_list_item_1, stringArray);
        customerView.setAdapter(adapter);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Customer customer = customerList.get(position);
            header = customer.getFullName();
            body = "tlf.: " + customer.getPhoneNumber() + " Mail: " + customer.getEmail();
            body += "\nAdresse: " + customer.getAddress() + ", " + customer.getZipCode() + " " + customer.getCity();
            body += "\nOpgaver: ";

            final View view1 = view;

            customerTaskRef = mFirebaseDatabase.getReference(userID + "/Customers/" + customer.getCustomerID() + "/Tasks");
            customerTaskRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        Task task = ds.getValue(Task.class);
                        body += "\n- " + task.getTopic();
                    }
                    dialogEvent(view1, body, header);
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });

            //dialogEvent(view, body, header);
        }
    };

    private void dialogEvent(View view, String body, String header)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerViewActivity.this);
        alertDialog.setMessage(body).setCancelable(true);

        AlertDialog alert = alertDialog.create();
        alert.setTitle(header);
        alert.show();
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
                    startActivity(new Intent(CustomerViewActivity.this, MainActivity.class));
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
