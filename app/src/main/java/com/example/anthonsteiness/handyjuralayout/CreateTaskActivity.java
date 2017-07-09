package com.example.anthonsteiness.handyjuralayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.anthonsteiness.handyjuralayout.objects.Customer;
import com.example.anthonsteiness.handyjuralayout.objects.RegularUser;
import com.example.anthonsteiness.handyjuralayout.objects.Task;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by jibba_000 on 22-05-2017.
 */

public class CreateTaskActivity extends AppCompatActivity {

    private String title = "Opret opgave";

    private TextView customerView;
    private EditText editName, editAddress, editCity, editZip, editPhone, editEmail;
    private TextView taskView;
    private EditText editTopic, editDescription, editPrice;
    private Button addTaskBtn;
    private RelativeLayout relativeLayout;
    private int height;
    private int width;
    private Button camerabtn;
    private ImageButton loadCustomerBtn;
    private ImageView picture1;
    //camera code
    private static int camReqCode =1;
    //camera Storage reference
    private StorageReference picStorage;
    //picture1 progressDialog
    private ProgressDialog picDialog;
    //private TextView downloadUrl;
    private String pictureURL;



    ViewGroup.MarginLayoutParams marginParams;


    // Buttons and stuff from app_bar class
    private ImageButton searchBtn;
    private Spinner helpDropDown;
    private ArrayAdapter<CharSequence> adapter;

    private TextView titleBar;
    private EditText searchBar;
    private String saveSearch = "";


    //Firebase related
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRootRef;
    private DatabaseReference myChildRef;
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private String userID;
    private FirebaseUser fbUser;
    private boolean userType;
    private String bossID;

    private DatabaseReference myCustomerRef;
    private DatabaseReference myTestRef;
    List<Customer> userList; // Maybe rename to customerList as this list contain all the customers.
    private String taskID;
    private boolean checkCustomer;
    private Customer testCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        height = getWindowManager().getDefaultDisplay().getHeight();
        width = getWindowManager().getDefaultDisplay().getWidth();

        // this is to check userType, and if it is true the bossID will not be null, but have an ID stored.
        Intent intent = getIntent();
        userType = intent.getExtras().getBoolean("userType");
        bossID = intent.getExtras().getString("bossID");
        //toastMessage(userType + " " + bossID, false);

        userList = new ArrayList<>();

        // Firebase declaration
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        fbUser = user;
        userID = user.getUid();

        myRootRef = mFirebaseDatabase.getReference();
        myChildRef = mFirebaseDatabase.getReference(userID);

        // Checks usertype, and makes reference to Customer data under bosses userID.
        if (userType == true)
        {
            // Regular User
            myCustomerRef = mFirebaseDatabase.getReference(bossID + "/Customers");
        }
        else
        {
            // Boss User
            myCustomerRef = mFirebaseDatabase.getReference(userID + "/Customers");
        }

        myCustomerRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    //Customer customer = ds.getValue(Customer.class);
                    //toastMessage(customer.getFullName(),false);

                    Customer cust = ds.child("CustomerInfo").getValue(Customer.class);
                    //toastMessage(cust.getFullName() , false);
                    userList.add(cust);
                }

                //for (Customer c : userList)
               // {
                    //toastMessage(c.getFullName(), true);
                //}

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        loadCustomerBtn = (ImageButton) findViewById(R.id.loadCustomerBtn);
        loadCustomerBtn.setOnClickListener(buttonClickListener);

        customerView = (TextView) findViewById(R.id.customerView1);
        editName = (EditText) findViewById(R.id.editName1);
        editAddress = (EditText) findViewById(R.id.editAddress1);
        editCity = (EditText) findViewById(R.id.editCity1);
        editZip = (EditText) findViewById(R.id.editZip1);
        editPhone = (EditText) findViewById(R.id.editPhone1);
        editEmail = (EditText) findViewById(R.id.editEmail1);
        taskView = (TextView) findViewById(R.id.taskView1);
        editTopic = (EditText) findViewById(R.id.editTopic1);
        editDescription = (EditText) findViewById(R.id.editDescription1);
        editPrice = (EditText) findViewById(R.id.editPrice1);
        addTaskBtn = (Button) findViewById(R.id.addTaskBtn1);
        addTaskBtn.setOnClickListener(buttonClickListener);
        camerabtn =(Button) findViewById(R.id.billedeBtn);
        picture1= (ImageView)findViewById(R.id.billedIV);
        //downloadUrl= (TextView) findViewById(R.id.urlTextview);

        //progress dialog uploading image
        picDialog = new ProgressDialog(this);


        //open camera
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                startActivityForResult(camIntent,camReqCode);

            }
        });


        // Fierbase declaration stuff
        firebaseAuth = FirebaseAuth.getInstance();
        // Check if Firebase is already logged in to
        if (firebaseAuth.getCurrentUser() != null)
        {
            // The Firebase is logged in to
        }
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    // The Firebase is logged in to
                }
                else
                {
                    // Could display not signed in. But might cause toasting issues...
                }
            }
        };



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
        titleBar.setText("Opret Opgave");

        checkScreenReso();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] data1 = baos.toByteArray();
        String path = "Photos/"+UUID.randomUUID()+".png";
        final StorageReference picstorage = mStorage.getReference(path);
        UploadTask uploadTask = picstorage.putBytes(data1);
        picture1.setImageBitmap(bitmap);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //downloadUrl.setText(picstorage.toString());
                pictureURL = picstorage.toString();
            }

        });


        }




    private View.OnClickListener buttonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            switch(view.getId()){
                case R.id.addTaskBtn1:
                    createTask();
                    break;
                case R.id.loadCustomerBtn:
                    //createCustomer();
                    break;
            }

        }
    };



    private void createCustomer(Task task)
    {
        String cName = editName.getText().toString().trim();
        String cAddress = editAddress.getText().toString().trim();
        String cCity = editCity.getText().toString().trim();
        String cZip = editZip.getText().toString().trim();
        String cPhone = editPhone.getText().toString().trim();
        String cEmail = editEmail.getText().toString().trim();

        Customer customer = new Customer(cName, cPhone, cEmail, cAddress, cCity, cZip);
        final String customerID = myCustomerRef.push().getKey();
        customer.setCustomerID(customerID);

        // This checks if the Customer already is in the Database.
        for (Customer cu : userList)
        {
            if (customer.equals(cu) == true)
            {
                //toastMessage("Customer is the same", true);
                checkCustomer = true;
                testCustomer = cu;
                break;
            }
            else
            {
                //toastMessage("Customer not the same", true);
                checkCustomer = false;
            }
        }

        if (!checkCustomer) // false therefor not found in Database already.
        {
            // And the Customer is created.
            myCustomerRef.child(customerID).child("CustomerInfo").setValue(customer);

            // Checks if boss or regular user. And creates the right database reference acordingly.
            if (userType == true)
            {
                // Regular user, needs to go under his bosses data
                //toastMessage("regular user", true);
                myTestRef = mFirebaseDatabase.getReference(bossID + "/Customers/" + customerID);
            }
            else
            {
                // Boss user, needs to go under his own data
                //toastMessage("boss user", true);
                myTestRef = mFirebaseDatabase.getReference(userID + "/Customers/" + customerID);
            }

            // This saves a link to the task under the Customer
            myTestRef.child("Tasks").child(task.getTaskID()).setValue(task);
        }
        else // true therefor the Customer is already an old Customer, thus not needed to be created.
        {
            // Checks if boss or regular user. And creates the right database reference acordingly.
            if (userType == true)
            {
                // Regular user, needs to go under his bosses data
                //toastMessage("regular user", true);
                myTestRef = mFirebaseDatabase.getReference(bossID + "/Customers/" + testCustomer.getCustomerID());
            }
            else
            {
                // Boss user, needs to go under his own data
                //toastMessage("boss user", true);
                myTestRef = mFirebaseDatabase.getReference(userID + "/Customers/" + testCustomer.getCustomerID());
            }

            // This saves a link to the task under the Customer
            myTestRef.child("Tasks").child(task.getTaskID()).setValue(task);
        }
    }

    private void createTask(){

        //lav metode der tjekker at felter ikke er tomme, undgå crash

        String cName = editName.getText().toString().trim();
        String cAddress = editAddress.getText().toString().trim();
        String cCity = editCity.getText().toString().trim();
        String cZip = editZip.getText().toString().trim();
        String cPhone = editPhone.getText().toString().trim();
        String cEmail = editEmail.getText().toString().trim();
        String taskTopic = editTopic.getText().toString().trim();
        String taskDescription = editDescription.getText().toString().trim();
        double taskPrice = Double.parseDouble(editPrice.getText().toString().trim());
        //String url = downloadUrl.getText().toString().trim();

        taskID = myChildRef.push().getKey();

        Task task = new Task();
        task.setName(cName);
        task.setAddress(cAddress);
        task.setCity(cCity);
        task.setZipCode(cZip);
        task.setPhone(cPhone);
        task.setEmail(cEmail);
        task.setTopic(taskTopic);
        task.setDescription(taskDescription);
        task.setPrice(taskPrice);
        task.setDownloadUrl(pictureURL);
        task.setWorkerID(userID);
        task.setTaskID(taskID);

        myChildRef.child("Tasks").child(taskID).setValue(task);

        //Customer customer = new Customer(cName, cPhone, cEmail, cAddress, cCity, cZip);

        createCustomer(task);


        //finish();
        //startActivity(new Intent(CreateTaskActivity.this, MyMenuActivity.class));

        //toastMessage(cName, true);

    }

    private void checkScreenReso() {
        if (height >= 1790) {
            // For 5.2" screen


            if (height >= 1800) {
                // For 7" Screen


                if (height >= 1950) {
                    // For 9" Screen


                    if (height >= 2390) {
                        // For 6" & 5.5" Screen


                        if (height >= 2460) {
                            // For 10" Screen

                        }
                    }
                }
            }
        }

        if (height >= 1920 && height <= 1920) {
            // For 1920x1080 screens
        }
    }
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
                firebaseAuth.signOut();
                toastMessage("Du er nu logget ud", true);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }


    };


}
