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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ResettingUsersDataSettingsActivityTest {

    User user = new User();
    DatabaseHelper databaseHelper;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class,true,true);
    public ActivityTestRule<UsersActivity> mActivityTestRule2 = new ActivityTestRule<UsersActivity>(UsersActivity.class,true,false);

    @Test
    public void ResettingUsersDataSettingsActivityTest() throws Throwable {

        // NEED TO HAVE WIFI ENABLED !!!!!!!!!!!!

        // NEED TO ACCEPT LOCATION REQUEST MANUALLY WHEN REQUEST POPS UP !!!!!!!!!!!!!!!!!!!!

        /*****************  ADD A USER IN DATABASE WITH SOME GAME PROGRESS ************************/

        databaseHelper = new DatabaseHelper( mActivityTestRule.getActivity().getBaseContext());

        // Add user's Name, Email and Password to database
        user.setName("Stelios");
        user.setEmail("9lakis94@gmail.com");
        user.setPassword("1234");

        // Set user's current playing difficulty to default 3 -> "Normal"
        user.setDifficulty(3);

        // Set user's current playing song to the first one
        user.setSong(5);

        // Set user score to 0
        user.setScore(15000);

        // Set user's collected words to empty list
        List<String> removedMarkers = new ArrayList<>();
        removedMarkers.add(" ");
        Gson gson = new Gson();
        String inputString = gson.toJson(removedMarkers);
        user.setJson(inputString);

        // Set user distance walked to 0
        user.setWalked(4650);

        // Set user coins walked to 0
        user.setCoins(1325);

        // Set music on background to enabled
        user.setMusic(1);

        // Set is hint used to 'No hint used'
        user.setHint(0);

        // Add user to database
        databaseHelper.addUser(user);

        // Take user to Users Activity (Main Screen)
        Intent accountIntent = new Intent();

        // Take user's email, name and current playing difficulty for future use
        accountIntent.putExtra("EMAIL", "9lakis94@gmail.com");
        accountIntent.putExtra("NAME", databaseHelper.getNameByEmail("9lakis94@gmail.com"));
        accountIntent.putExtra("DIFFICULTY", databaseHelper.getDifficultyByEmail("9lakis94@gmail.com"));

        /*******************************************************************************************/

        // Launch main screen
        mActivityTestRule2.launchActivity(accountIntent);

        try { Thread.sleep(3000); }
        catch (InterruptedException e) {  }

        // Click on settings image icon
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.imageButtonForSettings), isDisplayed()));
        appCompatImageButton.perform(click());


        // Click on reset game button
        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.appCompatButton_reset_game), isDisplayed()));
        appCompatButton4.perform(click());

        // Accept to reset game
        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("YES")));
        appCompatButton5.perform(scrollTo(), click());


        ViewInteraction textView7 = onView(
                allOf(withId(R.id.text_valueStatistics),
                        isDisplayed()));
        try { Thread.sleep(3000); }
        catch (InterruptedException e) {  }

        // Check if all user progress was reset
        textView7.check(matches(withText("\n"+"0"+
                "\n"+"\n"+"0"+
                "\n"+"\n"+"0" +
                "\n"+"\n"+"0.0"+
                "\n"+"\n" + "Normal")));

    }

}
