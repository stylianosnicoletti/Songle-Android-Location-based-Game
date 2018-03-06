package com.example.stelios.songle;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class WrongUserLoginActivityTest {

    // NEED TO HAVE WIFI ENABLED !!!!!!!!!!!!

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void wrongUserLoginActivity() {

        // Login with details of a user that is not yet registered
        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.textInputEditTextEmail), isDisplayed()));
        textInputEditText.perform(click());

        // Enter unregistered email address
        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.textInputEditTextEmail), isDisplayed()));
        textInputEditText2.perform(replaceText("lakis94@gmail.com "), closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.textInputEditTextEmail), withText("lakis94@gmail.com "), isDisplayed()));
        textInputEditText3.perform(pressImeActionButton());

        // Enter unregistered password
        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.textInputEditTextPassword), isDisplayed()));
        textInputEditText4.perform(replaceText("123"), closeSoftKeyboard());

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.textInputEditTextPassword), withText("123"), isDisplayed()));
        textInputEditText5.perform(pressImeActionButton());

        // Press on login button
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.appCompatButtonLogin), withText("Login"), isDisplayed()));
        appCompatButton.perform(click());


        // Check if message "Wrong Email or Password" is displayed
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Wrong Email or Password")))
                .check(matches(isDisplayed()));
    }

}
