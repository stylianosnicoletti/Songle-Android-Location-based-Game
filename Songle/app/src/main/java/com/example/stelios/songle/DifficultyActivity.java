package com.example.stelios.songle;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DifficultyActivity  extends AppCompatActivity implements View.OnClickListener{
    private final AppCompatActivity activitydif = DifficultyActivity.this;

    // FOR TEXT ON TOP
    private TextView textViewChooseDifficulty;

    // FOR BUTTONS
    private AppCompatButton appCompatButtonVeryEasy;
    private AppCompatButton appCompatButtonEasy;
    private AppCompatButton appCompatButtonNormal;
    private AppCompatButton appCompatButtonHard;
    private AppCompatButton appCompatButtonVeryHard;

    // CURRENT DIFFICULTY OF USER NUMBER
    private Integer difficulty_int ;

    // CURRENT DIFFICULTY OF USER TEXT
    private String difficulty_text ;

    // FOR DATABASE USE
    private DatabaseHelper databaseHelper;

    // CURRENT USER EMAIL
    private String email;

    /******************************* ON CREATE *****************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        // Initialise objects, views and listeners
        initObjects();
        initViews();
        initListeners();

        // Get current user email
        email = getIntent().getStringExtra("EMAIL");

        // Get current user difficulty playing
        difficulty_int = databaseHelper.getDifficultyByEmail(email);

        // Much difficulty number with actual difficulty (eg. 1 for Very Hard)
        if(difficulty_int==5){difficulty_text = "Very Easy";}
        else if(difficulty_int==4){difficulty_text = "Easy";}
        else if(difficulty_int==3){difficulty_text = "Normal";}
        else if(difficulty_int==2){difficulty_text = "Hard";}
        else if(difficulty_int==1){difficulty_text = "Very Hard";}

        // Display text on top informing user what is his current difficulty selected
        textViewChooseDifficulty.setText("Your current difficulty is set to: \n" + difficulty_text + "!");

    }

    /********************** INITIALISE OBJECTS *****************************************************/
    private void initObjects(){
        databaseHelper = new DatabaseHelper(activitydif);

    }
    /********************** INITIALISE VIEWS *******************************************************/
    private void initViews() {

        // Text on top
        textViewChooseDifficulty = (TextView) findViewById(R.id.text_choosedifficulty);

        // Easy button
        appCompatButtonEasy = (AppCompatButton) findViewById(R.id.appCompatButton_easy);

        // Very Easy button
        appCompatButtonVeryEasy = (AppCompatButton) findViewById(R.id.appCompatButton_veryeasy);

        // Normal button
        appCompatButtonNormal = (AppCompatButton) findViewById(R.id.appCompatButton_normal);

        // Hard button
        appCompatButtonHard = (AppCompatButton) findViewById(R.id.appCompatButton_hard);

        // Very Hard button
        appCompatButtonVeryHard = (AppCompatButton) findViewById(R.id.appCompatButton_veryhard);
    }

    /********************** INITIALISE LISTENERS ***************************************************/
    private void initListeners() {

        // Easy button listener
        appCompatButtonEasy.setOnClickListener(this);

        // Very Easy button listener
        appCompatButtonVeryEasy.setOnClickListener(this);

        // Normal button listener
        appCompatButtonNormal.setOnClickListener(this);

        // Hard button listener
        appCompatButtonHard.setOnClickListener(this);

        // Very Hard button listener
        appCompatButtonVeryHard.setOnClickListener(this);

    }

    /********************* ON CLICK ****************************************************************/
    public void onClick(final View v){

        // Prepare an empty list for words collected in case the user changes difficulty
        List<String> removedMarkers = new ArrayList<>();
        removedMarkers.add(" ");
        Gson gson = new Gson();
        final String inputString = gson.toJson(removedMarkers);

        // Initialise and Alert Dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DifficultyActivity.this);

        // Setting Alert Dialog Title
        alertDialog.setTitle("Confirm Change...");

        // Setting Alert Dialog Message
        alertDialog.setMessage("Are you sure you want to change difficulty?\nAll words collected for current song will be erased..");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                switch (v.getId()){

                    // Case where user clicked on Very Easy button
                    case R.id.appCompatButton_veryeasy:

                        // If the current difficulty is not the same as the one chosen
                        if(difficulty_int!=5){

                            // Update current playing difficulty, reset current song words collected
                            databaseHelper.changeDifficulty(email,5);
                            databaseHelper.changeWordsJson(email,inputString);}

                        // Take user back to Main Activity
                        Intent intentUsersActivity = new Intent(getApplicationContext(),UsersActivity.class);
                        intentUsersActivity.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
                        startActivity(intentUsersActivity);
                        finish();
                        break;

                    // Case where user clicked on Easy button
                    case R.id.appCompatButton_easy:

                        // If the current difficulty is not the same as the one chosen
                        if(difficulty_int!=4){

                            // Update current playing difficulty, reset current song words collected
                            databaseHelper.changeDifficulty(email,4);
                            databaseHelper.changeWordsJson(email,inputString);}

                        // Take user back to Main Activity
                        intentUsersActivity = new Intent(getApplicationContext(), UsersActivity.class);
                        intentUsersActivity.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
                        startActivity(intentUsersActivity);
                        finish();
                        break;

                    // Case where user clicked on Normal button
                    case R.id.appCompatButton_normal:

                        // If the current difficulty is not the same as the one chosen
                        if(difficulty_int!=3){

                            // Update current playing difficulty, reset current song words collected
                            databaseHelper.changeDifficulty(email,3);
                            databaseHelper.changeWordsJson(email,inputString);}

                        // Take user back to Main Activity
                        intentUsersActivity = new Intent(getApplicationContext(), UsersActivity.class);
                        intentUsersActivity.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
                        startActivity(intentUsersActivity);
                        finish();
                        break;

                    // Case where user clicked on Hard button
                    case R.id.appCompatButton_hard:

                        // If the current difficulty is not the same as the one chosen
                        if(difficulty_int!=2){

                            // Update current playing difficulty, reset current song words collected
                            databaseHelper.changeDifficulty(email,2);
                            databaseHelper.changeWordsJson(email,inputString);}

                        // Take user back to Main Activity
                        intentUsersActivity = new Intent(getApplicationContext(), UsersActivity.class);
                        intentUsersActivity.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
                        startActivity(intentUsersActivity);
                        finish();
                        break;

                    // Case where user clicked on Very Hard button
                    case R.id.appCompatButton_veryhard:

                        // If the current difficulty is not the same as the one chosen
                        if(difficulty_int!=1){

                            // Update current playing difficulty, reset current song words collected
                            databaseHelper.changeDifficulty(email,1);
                            databaseHelper.changeWordsJson(email,inputString);}

                        // Take user back to Main Activity
                        intentUsersActivity = new Intent(getApplicationContext(), UsersActivity.class);
                        intentUsersActivity.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
                        startActivity(intentUsersActivity);
                        finish();
                        break;
                }

                // On YES event, Display a toast informing the user
                Toast.makeText(getApplicationContext(), "Difficulty changed!", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // On NO event, Display a toast informing the user
                Toast.makeText(getApplicationContext(), "Difficulty did not change!", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });


        switch (v.getId()){

            // Case where user clicked on Very Easy button
            case R.id.appCompatButton_veryeasy:

                // If the current difficulty is the same as the one chosen
                if(difficulty_int==5) {

                    // Display a toast informing the user, that difficulty selected is the same as the current one
                    Toast.makeText(getApplicationContext(), "You have already selected this difficulty!", Toast.LENGTH_SHORT).show();

                }else{        alertDialog.show();}

                break;

            // Case where user clicked on Easy button
            case R.id.appCompatButton_easy:

                // If the current difficulty is the same as the one chosen
                if(difficulty_int==4) {

                    // Display a toast informing the user, that difficulty selected is the same as the current one
                    Toast.makeText(getApplicationContext(), "You have already selected this difficulty!", Toast.LENGTH_SHORT).show();


                }else{        alertDialog.show();}
                break;

            // Case where user clicked on Normal button
            case R.id.appCompatButton_normal:

                // If the current difficulty is the same as the one chosen
                if(difficulty_int==3) {

                    // Display a toast informing the user, that difficulty selected is the same as the current one
                    Toast.makeText(getApplicationContext(), "You have already selected this difficulty!", Toast.LENGTH_SHORT).show();

                }else{        alertDialog.show();}
                break;

            // Case where user clicked on Hard button
            case R.id.appCompatButton_hard:

                // If the current difficulty is the same as the one chosen
                if(difficulty_int==2) {

                    // Display a toast informing the user, that difficulty selected is the same as the current one
                    Toast.makeText(getApplicationContext(), "You have already selected this difficulty!", Toast.LENGTH_SHORT).show();

                }else{        alertDialog.show();}
                break;

            // Case where user clicked on Very Hard button
            case R.id.appCompatButton_veryhard:

                // If the current difficulty is the same as the one chosen
                if(difficulty_int==1) {

                    // Display a toast informing the user, that difficulty selected is the same as the current one
                    Toast.makeText(getApplicationContext(), "You have already selected this difficulty!", Toast.LENGTH_SHORT).show();

                }else{        alertDialog.show();}
                break;
        }

    }

    /*********************** ON PHONE BACK BUTTON PRESS ********************************************/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {

            // Take user back to Settings Activity
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            settingsIntent.putExtra("EMAIL", email);
            startActivity(settingsIntent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
