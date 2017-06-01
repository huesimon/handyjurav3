package com.example.anthonsteiness.handyjuralayout;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Simon_ on 01-06-2017.
 */
public class LoginActivityTest  extends ActivityInstrumentationTestCase2<LoginActivity>{
    LoginActivity activity;


    public LoginActivityTest(){
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        activity = getActivity();
    }
    @Test
    public void testTextViewNotNull(){
        TextView textView = (TextView) activity.findViewById(R.id.forgotPassText);
        assertNotNull(textView);

    }

    @Test
    public void testLoginButtonNotNull(){
        Button button = (Button) activity.findViewById(R.id.loginBtn2) ;
        assertNotNull(button);
    }
}