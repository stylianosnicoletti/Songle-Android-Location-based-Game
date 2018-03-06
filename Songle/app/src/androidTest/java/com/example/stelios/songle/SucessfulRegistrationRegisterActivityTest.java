package com.example.stelios.songle;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
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
public class SucessfulRegistrationRegisterActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void sucessfulRegistrationRegisterActivityTest() {

        // NEED TO HAVE WIFI ENABLED !!!!!!!!!!!!

        // Register a new user with an email address that was not used before
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.textViewLinkRegister), withText("No account yet? Create one"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.textInputEditTextName), isDisplayed()));
        textInputEditText.perform(click());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.textInputEditTextName), isDisplayed()));
        textInputEditText2.perform(replaceText("Stelios"), closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.textInputEditTextName), withText("Stelios"), isDisplayed()));
        textInputEditText3.perform(pressImeActionButton());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.textInputEditTextEmail), isDisplayed()));
        textInputEditText4.perform(replaceText("101lakis94@gmail.com "), closeSoftKeyboard());

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.textInputEditTextEmail), withText("101lakis94@gmail.com "), isDisplayed()));
        textInputEditText5.perform(pressImeActionButton());

        ViewInteraction textInputEditText6 = onView(
                allOf(withId(R.id.textInputEditTextPassword), isDisplayed()));
        textInputEditText6.perform(replaceText("1234"), closeSoftKeyboard());

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.textInputEditTextPassword), withText("1234"), isDisplayed()));
        textInputEditText7.perform(pressImeActionButton());

        ViewInteraction textInputEditText8 = onView(
                allOf(withId(R.id.textInputEditTextConfirmPassword), isDisplayed()));
        textInputEditText8.perform(replaceText("1234"), closeSoftKeyboard());

        ViewInteraction textInputEditText9 = onView(
                allOf(withId(R.id.textInputEditTextConfirmPassword), withText("1234"), isDisplayed()));
        textInputEditText9.perform(pressImeActionButton());

        Espresso.onView(ViewMatchers.withId(R.id.nestedScrollView)).perform(ViewActions.swipeUp());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.appCompatButtonRegister), withText("Register"), isDisplayed()));
        appCompatButton.perform(click());

        Espresso.onView(ViewMatchers.withId(R.id.nestedScrollView)).perform(ViewActions.swipeDown());

        // Check if message "Email Already Exists" is displayed
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Registration Successful")))
                .check(matches(isDisplayed()));


    }

}
