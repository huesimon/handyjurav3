package com.example.anthonsteiness.handyjuralayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class TaskViewActivity extends AppCompatActivity
{
    private static final String TAG = "WorkersActivity";

    private String title = "Opgaver";

    TextView textView, textView2, textView3;
    ListView listView;
    ListView listView2;
    ImageButton addTaskBtn;
    ViewGroup.MarginLayoutParams marginParams;
    private int height, width;

    // Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRootRef;
    private DatabaseReference myUserIDRef;
    private DatabaseReference myTaskRef;
    private DatabaseReference myBossIDRef;
    private DatabaseReference myRegUserRef;
    private DatabaseReference myWorkerRef;
    private String bossID;
    private String userID;
    private FirebaseUser fbUser;
    private boolean userType;

    private Activity context;
    //private TextView nameView, emailView, branchView, cvrView;

    // Buttons and stuff from app_bar class
    private ImageButton searchBtn;
    private Spinner helpDropDown;
    private ArrayAdapter<CharSequence> adapter2;
    private TextView titleBar;
    private EditText searchBar;


    private List<Task> taskList;
    private List<String> stringArray;

    private List<RegularUser> userList;
    private List<String> workerIDList;

    private int i;
    private List<Task> taskList2;
    private List<String> stringArray2;

    private ArrayAdapter<CharSequence> testAdapter;
    RelativeLayout.LayoutParams lp, lp2;
    private String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        Intent intent = getIntent();
        userType = intent.getExtras().getBoolean("userType");
        bossID = intent.getExtras().getString("bossID");
        //toastMessage(userType + " " + bossID);

        height = getWindowManager().getDefaultDisplay().getHeight();
        width = getWindowManager().getDefaultDisplay().getWidth();

        textView = (TextView) findViewById(R.id.textViewCoworkers);
        textView.setText("Opgaver");
        listView = (ListView) findViewById(R.id.listViewCoworkers);
        listView.setOnItemClickListener(itemClickListener);
        addTaskBtn = (ImageButton) findViewById(R.id.addWorkerBtn);
        addTaskBtn.setOnClickListener(buttonClickListener);
        marginParams = (ViewGroup.MarginLayoutParams) addTaskBtn.getLayoutParams();
        textView2 = (TextView) findViewById(R.id.myTasksGreyArea);
        textView3 = (TextView) findViewById(R.id.myUsersTasksGreyArea);
        textView2.setClickable(true);
        textView3.setClickable(true);
        textView2.setOnClickListener(buttonClickListener);
        textView3.setOnClickListener(buttonClickListener);

        listView2 = (ListView) findViewById(R.id.listViewCoworkersTasks);
        listView2.setOnItemClickListener(itemClickListener2);

        lp = (RelativeLayout.LayoutParams) listView.getLayoutParams();
        lp2 = (RelativeLayout.LayoutParams) listView2.getLayoutParams();

        taskList = new ArrayList<>();
        stringArray = new ArrayList<>();
        userList = new ArrayList<>();
        workerIDList = new ArrayList<>();
        taskList2 = new ArrayList<>();
        stringArray2 = new ArrayList<>();

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
        myUserIDRef = mFirebaseDatabase.getReference(userID);
        myTaskRef = mFirebaseDatabase.getReference(userID + "/Tasks");
        myRegUserRef = mFirebaseDatabase.getReference(userID + "/RegularUsers");
        myBossIDRef = mFirebaseDatabase.getReference(bossID + "/RegularUsers");

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

    // This shows the workers no matter if the userType is regular or boss.
    // this get the datasnapshot when called. So it depends on the Datasnapshot from the ValueEventListener.
    private void showData(DataSnapshot dataSnapshot)
    {
        taskList.clear();
        stringArray.clear();
        // For loop to iterate through all the snapshots of the database
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            Task task = ds.getValue(Task.class);

            // This arrayList is made for later usage
            // At some point we want the users to be clickable. And we might need all the users
            // information to show when clicked.
            // And the users are added the same time as the String name. (This is thoughts. don't know if possible)
            taskList.add(task);

            String description = task.getDescription();

            if (task.getDescription().length() > 65)
            {
                desc = description.substring(0, 65);
                desc += "...";
            }
            else
            {
                desc = description;
            }

            String str = "Opgave: " + task.getTopic();
            str += "\nOpgave Beskrivelse: " + desc;
            str += "\nAdresse: " + task.getAddress() + ", " + task.getZipCode() + " " + task.getCity();
            stringArray.add(str);

        }

        testAdapter = new ArrayAdapter(TaskViewActivity.this, android.R.layout.simple_list_item_1, stringArray);
        listView.setAdapter(testAdapter);
    }


    private void showUsers(DataSnapshot dataSnapshot)
    {
            userList.clear();
            // For loop to iterate through all the snapshots of the database
            for (DataSnapshot ds : dataSnapshot.getChildren())
            {
                RegularUser regUser = ds.getValue(RegularUser.class);
                userList.add(regUser);
                //toastMessage("If I made it this far.. " + regUser.getFullName());
            }

            workerIDList.clear();
            for (RegularUser user : userList)
            {
                workerIDList.add(user.getUserID());
                //toastMessage("If I made it this far.. " + user.getUserID());
            }
    }

    private void showAllTasks(DataSnapshot dataSnapshot)
    {
        //toastMessage(workerIDList.get(i));
        for (String str : workerIDList)
        {
            myWorkerRef = mFirebaseDatabase.getReference(str + "/Tasks");
            myWorkerRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        Task task = ds.getValue(Task.class);
                        taskList2.add(task);

                        String description = task.getDescription();

                        if (task.getDescription().length() > 65)
                        {
                            desc = description.substring(0, 65);
                            desc += "...";
                        }
                        else
                        {
                            desc = description;
                        }

                        String str = "Opgave: " + task.getTopic();
                        str += "\nOpgave Beskrivelse: " + desc;
                        str += "\nAdresse: " + task.getAddress() + ", " + task.getZipCode() + " " + task.getCity();
                        stringArray2.add(str);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(TaskViewActivity.this, android.R.layout.simple_list_item_1, stringArray2);
                    listView2.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }

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
                case R.id.addWorkerBtn:
                    //Gotta finish this activity for now. As the user gets logged out.
                    Intent intent2 = new Intent(TaskViewActivity.this, CreateTaskActivity.class);
                    intent2.putExtra("bossID", bossID);
                    intent2.putExtra("userType", userType);
                    startActivity(intent2);
                    break;
                case R.id.myTasksGreyArea:
                    hideOrShowItems(1);
                    break;
                case R.id.myUsersTasksGreyArea:
                    hideOrShowItems(2);
                    break;
            }
        }
    };

    private void hideOrShowItems(int number)
    {
        int test = 0;
        if (number == 1)
        {
            // This is for ListView
            if (lp.height > 275 || listView.getHeight() > 275)
            {
                lp.height = 272;
            }
            else
            {
                int j = taskList.size();
                j = j * 272;
                lp.height = j;
            }
            listView.setLayoutParams(lp);
        }
        else if (number == 2)
        {
            // This is for ListView2
            if (lp2.height > 275 || listView2.getHeight() > 275)
            {
                lp2.height = 272;
            }
            else
            {
                int j = taskList2.size();
                j = j * 272;
                lp2.height = j;
            }
            listView2.setLayoutParams(lp2);
        }
    }

    private void checkUserType()
    {
        if (!userType)
        {
            // this is the BossUser

            myTaskRef.addValueEventListener(new ValueEventListener()
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

            myRegUserRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    showUsers(dataSnapshot);
                    //toastMessage("if i made it this far...");
                    showAllTasks(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });



        }
        else
        {
            // This is the RegularUser

            myTaskRef.addValueEventListener(new ValueEventListener()
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

            myBossIDRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    showUsers(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });



        }
    }


    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //for (RegularUser testUser : userList)
            //{
                //toastMessage(testUser.getFullName());
            //}
            Task task = taskList.get(position);
            String title = task.getTopic();
            String message = "Opgave Beskrivelse: " + task.getDescription()
                    + "\nPris: " + task.getPrice()
                    + "\nAdresse: " + task.getAddress()
                    + "\nBy: " + task.getZipCode() + ", " + task.getCity()
                    + "\nKunde: " + task.getName() + ", " + task.getPhone() + ", " + task.getEmail();
            dialogEvent(view, message, title);
           // RegularUser testUser = userList.get(position);
          //  dialogEvent(view, testUser.getEmail(), testUser.getFullName());
        }
    };
    private AdapterView.OnItemClickListener itemClickListener2 = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

            Task task = taskList2.get(position);
            String title = task.getTopic();
            String message = "Opgave Beskrivelse: " + task.getDescription()
                    + "\nPris: " + task.getPrice()
                    + "\nAdresse: " + task.getAddress()
                    + "\nBy: " + task.getZipCode() + ", " + task.getCity()
                    + "\nKunde: " + task.getName() + ", " + task.getPhone() + ", " + task.getEmail();
            dialogEvent(view, message, title);
        }
    };

    private void dialogEvent(View view, String message, String title)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskViewActivity.this);
        alertDialog.setMessage(message).setCancelable(true);

        AlertDialog alert = alertDialog.create();
        alert.setTitle(title);
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
                    startActivity(new Intent(TaskViewActivity.this, MainActivity.class));
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
