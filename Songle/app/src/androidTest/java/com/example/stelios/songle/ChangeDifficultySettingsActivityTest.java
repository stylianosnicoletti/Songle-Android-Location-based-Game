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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChangeDifficultySettingsActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void changeDifficultySettingsActivityTest() {

        // NEED TO HAVE WIFI ENABLED !!!!!!!!!!!!

        // Register a new user with default Normal
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
        textInputEditText4.perform(replaceText("5lakis94@gmail.com"), closeSoftKeyboard());

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.textInputEditTextEmail), withText("5lakis94@gmail.com"), isDisplayed()));
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
        textInputEditText11.perform(replaceText("5lakis94@gmail.com "), closeSoftKeyboard());

        ViewInteraction textInputEditText12 = onView(
                allOf(withId(R.id.textInputEditTextEmail), withText("5lakis94@gmail.com "), isDisplayed()));
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

        // Click on settings image icon
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.imageButtonForSettings), isDisplayed()));
        appCompatImageButton.perform(click());

        // Click on change difficulty
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.appCompatButton_changedifficulty), withText("Change difficulty"), isDisplayed()));
        appCompatButton3.perform(click());

        // Choose Very Hard
        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.appCompatButton_veryhard), withText("very hard"), isDisplayed()));
        appCompatButton4.perform(click());

        // Accept to change difficulty
        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("YES")));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.text_valueStatistics),
                        isDisplayed()));
        try { Thread.sleep(5000); }
        catch (InterruptedException e) {  }

      // Check to see if difficulty has changed from Normal to Very Hard
        textView7.check(matches(withText("\n"+"0"+
                "\n"+"\n"+"0"+
                "\n"+"\n"+"0" +
                "\n"+"\n"+"0.0"+
                "\n"+"\n" + "Very Hard")));

    }
}
