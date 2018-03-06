package com.example.stelios.songle;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.google.gson.Gson;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BuyHintWithCoinsWordsActivityTest {

    User user = new User();
    DatabaseHelper databaseHelper;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class,true,true);
    public ActivityTestRule<UsersActivity> mActivityTestRule2 = new ActivityTestRule<UsersActivity>(UsersActivity.class,true,false);

    @Test
    public void BuyHintWithCoinsWordsActivityTest() throws Throwable {

        // NEED TO HAVE WIFI ENABLED !!!!!!!!!!!!

        // NEED TO ACCEPT LOCATION REQUEST MANUALLY WHEN REQUEST POPS UP !!!!!!!!!!!!!!!!!!!!

        /*****************  ADD A USER IN DATABASE WITH 25 COINS ***********************************/

        databaseHelper = new DatabaseHelper( mActivityTestRule.getActivity().getBaseContext());

        // Add user's Name, Email and Password to database
        user.setName("Stelios");
        user.setEmail("8lakis94@gmail.com");
        user.setPassword("1234");

        // Set user's current playing difficulty to default 3 -> "Normal"
        user.setDifficulty(3);

        // Set user's current playing song to the first one
        user.setSong(1);

        // Set user score to 0
        user.setScore(0);

        // Set user's collected words to empty list
        List<String> removedMarkers = new ArrayList<>();
        removedMarkers.add(" ");
        Gson gson = new Gson();
        String inputString = gson.toJson(removedMarkers);
        user.setJson(inputString);

        // Set user distance walked to 0
        user.setWalked(0);

        // Set user coins walked to 0
        user.setCoins(25);

        // Set music on background to enabled
        user.setMusic(1);

        // Set is hint used to 'No hint used'
        user.setHint(0);

        // Add user to database
        databaseHelper.addUser(user);

        // Take user to Users Activity (Main Screen)
        Intent accountIntent = new Intent();


        // Take user's email, name and current playing difficulty for future use
        accountIntent.putExtra("EMAIL", "8lakis94@gmail.com");
        accountIntent.putExtra("NAME", databaseHelper.getNameByEmail("8lakis94@gmail.com"));
        accountIntent.putExtra("DIFFICULTY", databaseHelper.getDifficultyByEmail("8lakis94@gmail.com"));

        /*******************************************************************************************/

        // Launch main screen
        mActivityTestRule2.launchActivity(accountIntent);

        try { Thread.sleep(3000); }
        catch (InterruptedException e) {  }

        // Click on the play button
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


        // HERE YOU SHOULD ACCEPT LOCATION REQUEST PERMISSIONS


        // Click on words button
        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.buttonMapToBag), withText("MY WORDS"), isDisplayed()));
        appCompatButton4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1782);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click on buy hint image button
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.imageButtonHint), isDisplayed()));
        appCompatImageButton.perform(click());

        // Click on Buy button
        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.btn_dialog_ok), withText("BUY"), isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.text_dialog),
                        isDisplayed()));

        // Check if hint is diplayed
        String b = "Artist : ";
        textView.check(matches(withText(containsString(b))));

        // Click on Buy button
        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.btn_dialog), withText("OK"), isDisplayed()));
        appCompatButton6.perform(click());

        // Go back to login screen
        pressBack();
        try {
            Thread.sleep(1782);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        try {
            Thread.sleep(1782);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.text_valueStatistics),
                        isDisplayed()));
        try { Thread.sleep(3000); }
        catch (InterruptedException e) {  }

        // Check if coins wre decremented from 25 to 0
        textView7.check(matches(withText("\n"+"0"+
                "\n"+"\n"+"0"+
                "\n"+"\n"+"0" +
                "\n"+"\n"+"0.0"+
                "\n"+"\n" + "Normal")));

    }

}
