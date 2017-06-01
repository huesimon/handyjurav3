package com.example.anthonsteiness.handyjuralayout;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.List;

public class ContractActivity extends AppCompatActivity {

    EditText cName, cPhone, cEmail, cZip, date;
    Button contract_createButton;
    ListView listView;

    List<Task> taskList;
    List<String> stringArray;

    //FIREBASE
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRootRef;
    private DatabaseReference myUserIDRef;
    private DatabaseReference myContractRef;
    private DatabaseReference myTasktRef;
    private DatabaseReference myBossIDRef;
    private String bossID;
    private String userID;
    private FirebaseUser fbUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);

        cName = (EditText) findViewById(R.id.cName);
        contract_createButton = (Button) findViewById(R.id.contract_createButton);
        contract_createButton.setOnClickListener(buttonClickListener);



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
        myContractRef = mFirebaseDatabase.getReference(userID + "/Contracts");
        myTasktRef = mFirebaseDatabase.getReference(userID + "/Tasks");

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
        myTasktRef.addValueEventListener(new ValueEventListener()
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





    }
    private void showData(DataSnapshot dataSnapshot)
    {
        // For loop to iterate through all the snapshots of the database
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            Task task = ds.getValue(Task.class);


        }

        ArrayAdapter adapter = new ArrayAdapter(ContractActivity.this, android.R.layout.simple_list_item_1, stringArray);
        listView.setAdapter(adapter);
    }



    

    private View.OnClickListener buttonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {

            switch(view.getId())
            {
                case R.id.contract_createButton:
                   Context context = getApplicationContext();
                    CharSequence text = "Hello toast!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    finish();
                    startActivity(new Intent(ContractActivity.this, AddWorkerActivity.class));
                    break;
            }
        }
    };
}


