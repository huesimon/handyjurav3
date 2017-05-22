package com.example.anthonsteiness.handyjuralayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import static com.example.anthonsteiness.handyjuralayout.R.layout.activity_create_task;

/**
 * Created by jibba_000 on 22-05-2017.
 */

public class CreateTaskActivity extends AppCompatActivity{
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
    protected void onCreate(Bundle savedInstanceState)
    {
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
};

}
