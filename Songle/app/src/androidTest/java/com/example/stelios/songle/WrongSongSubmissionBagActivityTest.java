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
import static android.support.test.espresso.Espresso.pressBack;
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
public class WrongSongSubmissionBagActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void wrongSongSubmissionBagActivityTest() {

        // NEED TO HAVE WIFI ENABLED !!!!!!!!!!!!
        // NEED TO ACCEPT LOCATION REQUEST MANUALLY WHEN REQUEST POPS UP  !!!!!!!!!!!!!!!!!!!1!!!!

        // Register a new user
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
        textInputEditText4.perform(replaceText("3lakis94@gmail.com"), closeSoftKeyboard());

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.textInputEditTextEmail), withText("3lakis94@gmail.com"), isDisplayed()));
        textInputEditText5.perform(pressImeActionButton());

        ViewInteraction textInputEditText6 = onView(
                allOf(withId(R.id.textInputEditTextPassword), isDisplayed()));
        textInputEditText6.perform(replaceText("11"), closeSoftKeyboard());

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.textInputEditTextPassword), withText("11"), isDisplayed()));
        textInputEditText7.perform(pressImeActionButton());

        ViewInteraction textInputEditText8 = onView(
                allOf(withId(R.id.textInputEditTextConfirmPassword), isDisplayed()));
        textInputEditText8.perform(replaceText("11"), closeSoftKeyboard());

        ViewInteraction textInputEditText9 = onView(
                allOf(withId(R.id.textInputEditTextConfirmPassword), withText("11"), isDisplayed()));
        textInputEditText9.perform(pressImeActionButton());

        Espresso.onView(ViewMatchers.withId(R.id.nestedScrollView)).perform(ViewActions.swipeUp());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.appCompatButtonRegister), withText("Register"), isDisplayed()));
        appCompatButton.perform(click());

        pressBack();

        // Login
        ViewInteraction textInputEditText10 = onView(
                allOf(withId(R.id.textInputEditTextEmail), isDisplayed()));
        textInputEditText10.perform(click());

        ViewInteraction textInputEditText11 = onView(
                allOf(withId(R.id.textInputEditTextEmail), isDisplayed()));
        textInputEditText11.perform(replaceText("3lakis94@gmail.com "), closeSoftKeyboard());

        ViewInteraction textInputEditText12 = onView(
                allOf(withId(R.id.textInputEditTextEmail), withText("3lakis94@gmail.com "), isDisplayed()));
        textInputEditText12.perform(pressImeActionButton());

        ViewInteraction textInputEditText13 = onView(
                allOf(withId(R.id.textInputEditTextPassword), isDisplayed()));
        textInputEditText13.perform(replaceText("11"), closeSoftKeyboard());

        ViewInteraction textInputEditText14 = onView(
                allOf(withId(R.id.textInputEditTextPassword), withText("11"), isDisplayed()));
        textInputEditText14.perform(pressImeActionButton());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.appCompatButtonLogin), withText("Login"), isDisplayed()));
        appCompatButton2.perform(click());

        // Click on play button
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.appCompatButton_play), withText("Play"), isDisplayed()));
        appCompatButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // HERE SHOULD ACCEPT LOCATION PERMISSION REQUEST

        // Click on Words Button
        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.buttonMapToBag), withText("MY WORDS"), isDisplayed()));
        appCompatButton5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2232);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Enter a wrong song
        ViewInteraction textInputEditText18 = onView(
                allOf(withId(R.id.textInputEditTextSong), isDisplayed()));
        textInputEditText18.perform(replaceText("wrong submission "), closeSoftKeyboard());

        // Submit the wrong song
        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.appCompatButton_guessTheSong), withText("Submit Song"), isDisplayed()));
        appCompatButton6.perform(click());


        ViewInteraction textView = onView(
                allOf(withId(R.id.text_dialog),
                        isDisplayed()));

        // Check if message displayed informing the user that song was wrong, and try again
        textView.check(matches(withText("Try Again!")));

    }


}
