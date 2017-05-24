package com.example.anthonsteiness.handyjuralayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.anthonsteiness.handyjuralayout.R.layout.activity_create_task;

/**
 * Created by jibba_000 on 22-05-2017.
 */

public class CreateTaskActivity extends AppCompatActivity {

    private String title = "Opret opgave";

    private TextView customerView;
    private EditText editName, editAdress, editCity, editZip, editPhone;
    private TextView taskView;
    private EditText editTopic, editDescription, editPrice;
    private Button addTaskBtn;
    private RelativeLayout relativeLayout;
    private int height;
    private int width;

    ViewGroup.MarginLayoutParams marginParams;

    // Buttons and stuff from app_bar class
    private ImageButton searchBtn;
    private Spinner helpDropDown;
    private ArrayAdapter<CharSequence> adapter;

    private TextView titleBar;
    private EditText searchBar;
    private String saveSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        height = getWindowManager().getDefaultDisplay().getHeight();
        width = getWindowManager().getDefaultDisplay().getWidth();

        customerView = (TextView) findViewById(R.id.customerView);
        editName = (EditText) findViewById(R.id.editName);
        editAdress = (EditText) findViewById(R.id.editAdress);
        editCity = (EditText) findViewById(R.id.editCity);
        editZip = (EditText) findViewById(R.id.editZip);
        editPhone = (EditText) findViewById(R.id.editPhone);
        taskView = (TextView) findViewById(R.id.taskView);
        editTopic = (EditText) findViewById(R.id.editTopic);
        editDescription = (EditText) findViewById(R.id.editDescription);
        editPrice = (EditText) findViewById(R.id.editPrice);
        addTaskBtn = (Button) findViewById(R.id.addTaskBtn);

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

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

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
    private AdapterView.OnItemSelectedListener dropDownListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String help = "Help";
            String settings = "Settings";
            String about = "About";
            //String userInfo = "User Info";
            String signOut = "Sign Out";
            String defaultItem = "Select one";
            if (parent.getItemAtPosition(position).equals(help)) {
                toastMessage(help + " selected", true);
            } else if (parent.getItemAtPosition(position).equals(settings)) {
                toastMessage(settings + " selected", true);
            } else if (parent.getItemAtPosition(position).equals(about)) {
                toastMessage(about + " selected", true);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
