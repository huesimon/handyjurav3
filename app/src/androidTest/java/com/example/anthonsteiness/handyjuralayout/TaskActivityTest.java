package com.example.anthonsteiness.handyjuralayout;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Simon_ on 01-06-2017.
 */

/**
 * Remember to log out of the app (HandyJura) before running the test
 * Auto login will cause some test to fail
 * Log out before running the test
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
        // set ActivityMonitor
        
    }



    @Test
    public void checkTextInput() throws InterruptedException {
      // Test af Anthon
        // Type text
        onView(withId(R.id.editEmail))
                .perform(typeText(mEmailString), closeSoftKeyboard());
        onView(withId(R.id.editPassword))
                .perform(typeText(mPasswordString), closeSoftKeyboard());
        Thread.sleep(2000);

        // Check that the text was changed.
        onView(withId(R.id.editEmail))
                .check(matches(withText(mEmailString)));
        // Check that the text was changed.
        onView(withId(R.id.editPassword))
                .check(matches(withText(mPasswordString)));

        Thread.sleep(5000);
    }
    @Test
    //Test af Jonas
    public void clickLogin() throws InterruptedException {
        // Type text and then press the button.
        onView(withId(R.id.editEmail))
                .perform(typeText(mEmailString), closeSoftKeyboard());
        onView(withId(R.id.editPassword))
                .perform(typeText(mPasswordString), closeSoftKeyboard());
        onView(withId(R.id.loginBtn2)).perform(click());
        Thread.sleep(5000);
    }



    @Ignore
    public void testActivityIsOpen() {
//Test af Simon
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation()
                .addMonitor(MyMenuActivity.class.getName(), null, false);

        MyMenuActivity MyMenuActivity = (MyMenuActivity) activityMonitor.waitForActivity(); // By using ActivityMonitor
        assertNotNull("Target Activity is not launched", MyMenuActivity);
    }


}