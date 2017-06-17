package com.example.anthonsteiness.handyjuralayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anthonsteiness.handyjuralayout.objects.RegularUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private String title = "Søg";

    private TextView textViewSearchText;
    private ListView listViewSearchResults;
    private String saveSearch;

    List<RegularUser> userList;
    List<String> stringArray;
    List<String> searchArray;

    // Firebase stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRootRef;
    private DatabaseReference myUserIDRef;
    private DatabaseReference myRegUserRef;
    private DatabaseReference myBossIDRef;
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

        textViewSearchText = (TextView) findViewById(R.id.textViewSearchText);
        textViewSearchText.setText("Søgning: " + saveSearch);
        listViewSearchResults = (ListView) findViewById(R.id.listViewSearchResults);

        userList = new ArrayList<>();
        stringArray = new ArrayList<>();
        searchArray = new ArrayList<>();

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
        myRegUserRef = mFirebaseDatabase.getReference(userID + "/RegularUsers");

        //myRegUserRef = mFirebaseDatabase.getReference(userID + "/RegularUsers");

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
                    //toastMessage("Search function not yet implemented..");
                    break;

            }
        }
    };

    // Method to check the users information.
    // If RegularUser = true.
    //
    // if RegularUser = false
    //
    private void checkUserType()
    {
        myUserIDRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    RegularUser regUser = new RegularUser();

                    regUser = ds.getValue(RegularUser.class);

                    regUserType = regUser.isRegUser();

                    if (regUserType)
                    {
                        // This is the RegularUser

                        bossID = regUser.getBossUserID();

                        //toastMessage("This is test, this is RegUser \n" + bossID);

                        myBossIDRef = mFirebaseDatabase.getReference(bossID + "/RegularUsers");

                        myBossIDRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                showUsers(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        // this is the BossUser

                        //bossID = regUser.getUserID();
                        //toastMessage("This is test, this is bossUser \n");

                        myRegUserRef.addValueEventListener(new ValueEventListener()
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    // This shall both get the name of the user working on this task, but also thee name of the customor.
    // This shall also get email like the showUsers method needs to get email.
    private void showTasks(DataSnapshot datasnapshot)
    {

    }

    // This needs to have implicated so that it checks if the text is an email, instead of name.
    // We might wanna make a method in the onCreate to check the SearchText, if it's a name, email, phonenumber etc.
    private void showUsers(DataSnapshot dataSnapshot)
    {
        userList.clear();
        stringArray.clear();
        // For loop to iterate through all the snapshots of the database
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

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
            if (str.contains(" "))
            {

                String parts[] = str.split("\\ ");
                str = parts[0]; // Strips anything after a space

            }


            if (str.equalsIgnoreCase(saveSearch))
            {
                toastMessage("Keep: " + stringArray.get(i));
                searchArray.add(str);
            }
            else
            {
                toastMessage("remove: " + stringArray.get(i));
            }

            String nothingFound = "Der blev ikke fundet noget under søgningen...";
            if (searchArray.size() == 0)
            {
                searchArray.add(nothingFound);
            }
            else
            {
                searchArray.remove(nothingFound);
            }

        }

        ArrayAdapter adapter = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1, searchArray);
        listViewSearchResults.setAdapter(adapter);
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
