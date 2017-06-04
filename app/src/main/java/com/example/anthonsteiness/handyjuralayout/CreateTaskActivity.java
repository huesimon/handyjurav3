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


import com.example.anthonsteiness.handyjuralayout.objects.Task;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;


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
    private ImageView picture1;
    //camera code
    private static int camReqCode =1;
    //camera Storage reference
    private StorageReference picStorage;
    //picture1 progressDialog
    private ProgressDialog picDialog;


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
   
    private String userID;
    private FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        height = getWindowManager().getDefaultDisplay().getHeight();
        width = getWindowManager().getDefaultDisplay().getWidth();

        // Firebase declaration
        firebaseAuth = FirebaseAuth.getInstance();
        // Check if Firebase is already logged in
        if (firebaseAuth.getCurrentUser() != null)
        {
            // The Firebase is logged in
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        fbUser = user;
        userID = user.getUid();

        myRootRef = mFirebaseDatabase.getReference();
        myChildRef = mFirebaseDatabase.getReference(userID);



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
        //progress dialog uploading image
        picDialog = new ProgressDialog(this);


        //open camera
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file= getFile();
                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, getFile());
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
        picture1.setImageBitmap(bitmap);
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
            }

        }
    };

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
        ImageView picture= picture1;

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
        task.setImage(picture);

        myChildRef.child("Tasks").child(myChildRef.push().getKey()).setValue(task);

        finish();
        startActivity(new Intent(CreateTaskActivity.this, MyMenuActivity.class));

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

                            //loginBtn2.setHeight(400); -- IT WONT CHANGE FOR SOME REASON!?!?
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
    private File getFile(){
        File folder = new File("data/Hammernemt");
        if(!folder.exists()){
            folder.mkdir();
        }
        File image_file= new File(folder,"cam_image.jpg");
        return image_file;
    }

}
