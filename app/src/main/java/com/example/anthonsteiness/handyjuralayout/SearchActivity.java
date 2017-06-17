package com.example.anthonsteiness.handyjuralayout;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private String title = "Søg";

    private TextView textViewSearchText;
    private ListView listViewSearchResults;
    private String saveSearch;
    private ListView listViewTaskResults;
    private ListView listViewTaskResults2;
    private TextView textViewWorkersTasks;

    List<RegularUser> userList;
    List<String> stringArray;
    // Correct search results go in these
    List<String> searchArray;
    List<RegularUser> userResults;

    // These Lists are for the showTasks method
    List<Task> taskList;
    List<String> stringArray2;
    // Correct search results go in these
    List<Task> taskResults;
    List<String> searchArray2;

    // These Lists are for the showAllTasks method
    List<Task> taskList2;
    List<String> stringArray3;
    // The search results go in here
    List<Task> taskResults2;
    List<String> searchArray3;


    List<String> workerIDList;
    boolean check;
    int i;

    // Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRootRef;
    private DatabaseReference myUserIDRef;
    private DatabaseReference myRegUserRef;
    private DatabaseReference myBossIDRef;
    private DatabaseReference myTaskRef;
    private DatabaseReference myWorkerRef;
    private String bossID;
    private String userID;
    private String regUserID;
    private FirebaseUser fbUser;
    private boolean regUserType;

    // Buttons and stuff from app_bar class
    ImageButton searchBtn;
    Spinner helpDropDown;
    ArrayAdapter<CharSequence> adapter2;
    TextView titleBar;
    EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        saveSearch = intent.getExtras().getString("searchText");
        regUserType = intent.getExtras().getBoolean("userType");

        textViewSearchText = (TextView) findViewById(R.id.textViewSearchText);
        textViewSearchText.setText("Søgning: " + saveSearch);
        listViewSearchResults = (ListView) findViewById(R.id.listViewSearchResults);
        listViewSearchResults.setOnItemClickListener(itemClickListener);
        listViewTaskResults = (ListView) findViewById(R.id.listViewTaskResults);
        listViewTaskResults.setOnItemClickListener(itemClickListener2);
        listViewTaskResults2 = (ListView) findViewById(R.id.listViewTaskResults2);
        listViewTaskResults2.setOnItemClickListener(itemClickListener3);
        textViewWorkersTasks = (TextView) findViewById(R.id.othersTasksGreyArea);

        // Hides the "Medarbejderes opgaver" textView.
        if (regUserType == true)
        {
            textViewWorkersTasks.setBackgroundColor(0);
        }

        userList = new ArrayList<>();
        stringArray = new ArrayList<>();
        searchArray = new ArrayList<>();
        userResults = new ArrayList<>();

        taskList = new ArrayList<>();
        stringArray2 = new ArrayList<>();
        searchArray2 = new ArrayList<>();
        taskResults = new ArrayList<>();

        taskList2 = new ArrayList<>();
        stringArray3 = new ArrayList<>();
        searchArray3 = new ArrayList<>();
        taskResults2 = new ArrayList<>();

        workerIDList = new ArrayList<>();

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
        myUserIDRef = mFirebaseDatabase.getReference(userID + "/UserInfo");
        myRegUserRef = mFirebaseDatabase.getReference(userID + "/RegularUsers");
        myTaskRef = mFirebaseDatabase.getReference(userID + "/Tasks");

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
        titleBar.setClickable(true);
        titleBar.setOnClickListener(buttonClickListener);

    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            switch(view.getId())
            {
                case R.id.searchbtn:
                    search(view);
                    break;
                case R.id.titleBar:
                    search(view);
                    break;
            }
        }
    };

    private void checkUserType()
    {

        myUserIDRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                    RegularUser regUser = new RegularUser();

                    regUser = dataSnapshot.getValue(RegularUser.class);

                    regUserType = regUser.isRegUser();

                    if (!regUserType)
                    {

                        // this is the BossUser
                        myRegUserRef.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                showUsers(dataSnapshot);

                                showAllTasks(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });

                        myTaskRef.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                showTasks(dataSnapshot);
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
                        bossID = regUser.getBossUserID();
                        toastMessage("This is test, this is RegUser \n" + bossID);

                        myBossIDRef = mFirebaseDatabase.getReference(bossID + "/RegularUsers");

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


                        myTaskRef.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                showTasks(dataSnapshot);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {

                            }
                        });
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }

    private void showAllTasks(DataSnapshot dataSnapshot)
    {
        //toastMessage(workerIDList.get(i));
        myWorkerRef = mFirebaseDatabase.getReference(workerIDList.get(i) + "/Tasks");
        myWorkerRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Task task = new Task();
                    task = ds.getValue(Task.class);
                    taskList2.add(task);
                }
                for (Task task : taskList2)
                {
                    String str = task.getName();
                    if (str.matches("(?i).*" + saveSearch +".*"))
                    {
                        //toastMessage("Keep: " + str);
                        searchArray3.add("Opgave: " + str);
                        //toastMessage("add " + regUser.getFullName() + " too resultList");
                        taskResults2.add(task);
                    }
                }
                    ArrayAdapter adapter = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1, searchArray3);
                    listViewTaskResults2.setAdapter(adapter);
                }
                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                }
        });
        i++;
    }

    // This shall both get the name of the user working on this task, but also the name of the customor.
    // This shall also get email like the showUsers method needs to get email. We need method to check inputtype in onCreate.
    private void showTasks(DataSnapshot datasnapshot)
    {
        taskList.clear();
        stringArray2.clear();
        // For loop to iterate through all the snapshots of the database
        for (DataSnapshot ds : datasnapshot.getChildren()) {
            Task task = ds.getValue(Task.class);
            taskList.add(task);
        }
        for (Task task : taskList)
        {
            String str = task.getName();
            if (str.matches("(?i).*" + saveSearch +".*"))
            {
                searchArray2.add("Opgave: " + str);
                taskResults.add(task);
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1, searchArray2);
        listViewTaskResults.setAdapter(adapter);
    }

    // This needs to have implicated so that it checks if the text is an email, instead of name.
    // We might wanna make a method in the onCreate to check the SearchText, if it's a name, email, phonenumber etc.
    private void showUsers(DataSnapshot dataSnapshot)
    {
        userList.clear();
        stringArray.clear();
        // For loop to iterate through all the snapshots of the database
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            RegularUser regUser = ds.getValue(RegularUser.class);
            userList.add(regUser);
            String str = regUser.getFullName();
            stringArray.add(str);
        }
        // This for loop removes anything after a space. So in this case, it removes last name.
        // Probably not the best, but for now. If the user only typed in first name.
        // Now I can compare the searchText with the names, and see if anything matches.
        for (int i = 0; i < stringArray.size(); i++)
        {
            String str = stringArray.get(i);
            RegularUser regUser = userList.get(i);
            if (str.matches("(?i).*" + saveSearch +".*"))
            {
                searchArray.add("Medarbejder: " + str);
                userResults.add(regUser);
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1, searchArray);
        listViewSearchResults.setAdapter(adapter);

        workerIDList.clear();
        for (RegularUser user : userList)
        {
            workerIDList.add(user.getUserID());
            //toastMessage(user.getUserID());
        }
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            RegularUser testUser = userResults.get(position);
            dialogEvent(view, testUser.getEmail(), testUser.getFullName());
        }
    };

    private AdapterView.OnItemClickListener itemClickListener2 = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Task testTask = taskResults.get(position);
            String title123 = testTask.getTopic();
            String text = "Opgave Beskrivelse:\n" + testTask.getDescription()
                    + "\nAdresse: " + testTask.getAddress() + ", " + testTask.getZipCode() + ", " + testTask.getCity()
                    + "\nPris: " + testTask.getPrice()
                    + "\nKunde: " + testTask.getName() + ", " + testTask.getPhone() + ", " + testTask.getEmail();
            dialogEvent(view, text, title123);
        }
    };

    private AdapterView.OnItemClickListener itemClickListener3 = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Task testTask = taskResults2.get(position);
            String title123 = testTask.getTopic();
            String text = "Opgave Beskrivelse:\n" + testTask.getDescription()
                    + "\nAdresse: " + testTask.getAddress() + ", " + testTask.getZipCode() + ", " + testTask.getCity()
                    + "\nPris: " + testTask.getPrice()
                    + "\nKunde: " + testTask.getName() + ", " + testTask.getPhone() + ", " + testTask.getEmail();
            dialogEvent(view, text, title123);
        }
    };

    // Method for searchBar (NOT NEEDED HERE, SAVED FOR LATER USAGE)
    private void setTitle(View view)
    {
        if (!titleBar.getText().equals(title))
        {
            saveSearch = searchBar.getText().toString().trim();
            searchBar.setHint("");
            searchBar.setText("");
            titleBar.setText(title);
            searchBar.setInputType(InputType.TYPE_NULL);

            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void search(View view) {
        // This is where the search functionality is gonna be.
        if (!titleBar.getText().equals(title))
        {
            if (!searchBar.getText().toString().trim().equals(""))
            {
                finish();
                Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                intent.putExtra("searchText", searchBar.getText().toString().trim());
                intent.putExtra("userType", regUserType);
                startActivity(intent);
            }
            else
            {
                setTitle(view);
            }

        }
        // This is where it shows the search bar at first.
        else
        {
            searchBar.setHint("Søg...");
            titleBar.setText("");
            searchBar.setInputType(InputType.TYPE_CLASS_TEXT);

            if (!saveSearch.equals(""))
            {
                searchBar.setText(saveSearch);
            }

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void dialogEvent(View view, String mail, String name)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchActivity.this);
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
            String help = "Hjælp";
            String settings = "Indstillinger";
            String about = "Om";
            String signOut = "Log ud";
            String defaultItem = "Vælg en";
            if (parent.getItemAtPosition(position).equals(help))
            {
                //toastMessage(help + " valgt");
            }
            else if (parent.getItemAtPosition(position).equals(settings))
            {
                //toastMessage(settings + " valgt");
            }
            else if (parent.getItemAtPosition(position).equals(about))
            {
                //toastMessage(about + " valgt");
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
                    startActivity(new Intent(SearchActivity.this, MainActivity.class));
                    //toastMessage("Du er nu logget ud");
                }
                else
                {
                    //toastMessage("Du er ikke logget ind");
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
