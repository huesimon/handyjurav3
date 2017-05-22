package com.example.anthonsteiness.handyjuralayout;

import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jibba_000 on 22-05-2017.
 */

public class CreateTaskActivity extends AppCompatActivity{
    private String title = "Opret opgave";
    private TextView customerView;
    private EditText name, address, city, zip, phone;
    private TextView task;
    private EditText topic, description, price;
    private Button addTask;
    private RelativeLayout relativeLayout;
    private int height;
    private int width;

    ViewGroup.MarginLayoutParams marginParams;
}
