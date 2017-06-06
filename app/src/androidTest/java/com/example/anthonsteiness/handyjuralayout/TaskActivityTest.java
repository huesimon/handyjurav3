package com.example.anthonsteiness.handyjuralayout;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Simon_ on 01-06-2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TaskActivityTest {

    private String mEmailString;
    private String mPasswordString;
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mEmailString = "jens@jens.dk";
        mPasswordString = "123456";
        
        
    }

    @Test
    public void checkTextInput() {
      
        // Type text and then press the button.
        onView(withId(R.id.editEmail))
                .perform(typeText(mEmailString), closeSoftKeyboard());
        onView(withId(R.id.editPassword))
                .perform(typeText(mPasswordString), closeSoftKeyboard());


       // onView(withId(R.id.loginBtn)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.editEmail))
                .check(matches(withText(mEmailString)));
        // Check that the text was changed.
        onView(withId(R.id.editPassword))
                .check(matches(withText(mPasswordString)));
    }
}