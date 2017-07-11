package com.example.anthonsteiness.handyjuralayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.anthonsteiness.handyjuralayout.objects.Customer;
import com.example.anthonsteiness.handyjuralayout.objects.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by jibba_000 on 22-05-2017.
 */

public class CreateTaskActivity extends AppCompatActivity {

    private String title = "Opret opgave";

    private TextView customerView;
    private EditText editName, editAddress, editCity, editZip, editPhone, editEmail, editStartDate, editDueDate;
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
    //private TextView downloadUrl;
    private String pictureURL;

    ViewGroup.MarginLayoutParams marginParams;
    ListView listViewCustomer;

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

    List<Customer> customerList;
    List<String> cNameList;
    private String taskID;
    private boolean checkCustomer;
    private Customer testCustomer;

    private Animation animBounce;
    private Animation animLinear;

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
        userID = intent.getExtras().getString("userID");
        //toastMessage(userType + " " + bossID, false);

        animBounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        animLinear = AnimationUtils.loadAnimation(this, R.anim.linear);

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
        editStartDate = (EditText) findViewById(R.id.editStartDate1);
        editDueDate = (EditText) findViewById(R.id.editDueDate1);
        listViewCustomer = (ListView) findViewById(R.id.listViewCustomers1);
        marginParams = (ViewGroup.MarginLayoutParams) listViewCustomer.getLayoutParams();
        listViewCustomer.setOnItemClickListener(itemClickListener);

        customerList = new ArrayList<>();
        cNameList = new ArrayList<>();

        // Firebase declaration
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

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
                customerList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Customer cust = ds.child("CustomerInfo").getValue(Customer.class);
                    //toastMessage(cust.getFullName() , false);
                    customerList.add(cust);
                    cNameList.add(cust.getFullName() + ", " + cust.getAddress());
                }

                ArrayAdapter adapter = new ArrayAdapter(CreateTaskActivity.this, R.layout.simple_list_layout, R.id.label, cNameList);
                listViewCustomer.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });


        //open camera
        /*camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camIntent,camReqCode);
            }
        });*/
        camerabtn.setOnClickListener(buttonClickListener);


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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);

        byte[] data1 = baos.toByteArray();
        UUID imageID = UUID.randomUUID();
        String path = "Photos/"+imageID+".png";
        final StorageReference imageStorageRef = mStorage.getReference(path);
        UploadTask uploadTask = imageStorageRef.putBytes(data1);

        picture1.setImageBitmap(bitmap);



        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                // Handle on faliure
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            // This suppresses a warning this line of code shows. The code works fine, don't worry about it.
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pictureURL = taskSnapshot.getDownloadUrl().toString();
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
                    // Show List View of Customers
                    if (marginParams.topMargin > 360)
                    {
                        marginParams.topMargin = 300;
                        listViewCustomer.setLayoutParams(marginParams);
                        listViewCustomer.startAnimation(animBounce);
                    }
                    else
                    {
                        startLinearAnim();
                    }
                    break;
                case R.id.billedeBtn:
                    Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camIntent,camReqCode);
                    break;
            }

        }
    };


    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Customer customer = customerList.get(position);
            editName.setText(customer.getFullName());
            editAddress.setText(customer.getAddress());
            editCity.setText(customer.getCity());
            editZip.setText(customer.getZipCode());
            editPhone.setText(customer.getPhoneNumber());
            editEmail.setText(customer.getEmail());
            startLinearAnim();
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
        for (Customer cu : customerList)
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

    private void createTask()
    {
        if (!checkEmptyText())
        {
            // false - meaning no fields empty
            String cName = editName.getText().toString().trim();
            String cAddress = editAddress.getText().toString().trim();
            String cCity = editCity.getText().toString().trim();
            String cZip = editZip.getText().toString().trim();
            String cPhone = editPhone.getText().toString().trim();
            String cEmail = editEmail.getText().toString().trim();
            String taskTopic = editTopic.getText().toString().trim();
            String taskDescription = editDescription.getText().toString().trim();
            String startDate = editStartDate.getText().toString().trim();
            String dueDate = editDueDate.getText().toString().trim();
            double taskPrice = 0.0;

            try
            {
                taskPrice = Double.parseDouble(editPrice.getText().toString());
            } catch (final NumberFormatException e)
            {
                taskPrice = 0.0;
            }

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
            task.setDueDate(dueDate);
            task.setStartDate(startDate);

            myChildRef.child("Tasks").child(taskID).setValue(task);

            createCustomer(task);
        }
        else
        {
            // true - meaning at least one field empty
        }

    }

    private boolean checkEmptyText()
    {
        // This method is going to check every EditText field, if it is empty or not
        // If just one field is empty, (only picture field is allowed to be empty)
        // then it must say error and say where the error accured.
        double taskPrice = 0.0;

        try
        {
            taskPrice = Double.parseDouble(editPrice.getText().toString());
        } catch (final NumberFormatException e)
        {
            taskPrice = 0.0;
        }

        boolean bool = true;

        if ((!(editName.length() > 0)) || (!(editAddress.length() > 0))
                || (!(editCity.length() > 0)) || (!(editZip.length() > 0))
                || (!(editPhone.length() > 0)) || (!(editEmail.length() > 0)))
        {
            toastMessage("Mangler information om kunden", false);
            bool = true;
        }
        else
        {
            bool = false;
        }

        if (!(editTopic.length() > 0) || (!(editDescription.length() > 0))
                || (!(editStartDate.length() > 0)) || (!(editDueDate.length() > 0)))
        {
            toastMessage("Mangler information om opgaven", false);
            bool = true;
        }
        else
        {
            bool = false;
        }

        return bool;
    }

    private void startLinearAnim()
    {
        listViewCustomer.startAnimation(animLinear);

        animLinear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                marginParams.topMargin = 20000;
                listViewCustomer.setLayoutParams(marginParams);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
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
