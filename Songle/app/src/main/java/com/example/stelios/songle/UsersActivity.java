package com.example.stelios.songle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class UsersActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activityusers = UsersActivity.this;

    // FOR TOP TEXT WELCOMING USER
    private TextView textViewName;

    // FOR STATISTICS TITLES
    private TextView textViewYourStatisticsLabels;

    // FOR STATISTICS VALUES
    private TextView textViewYourStatisticsValues;

    // FOR BUTTONS
    private AppCompatButton appCompatButtonPlay;
    private ImageButton imageButtonSettings;
    private AppCompatButton appCompatButtonSongs;

    // FOR DATABASE USE
    private DatabaseHelper databaseHelper;

    // FOR USER CURRENT SCORE
    private int score;

    // FOR USER TOTAL DISTANCE WALKED
    private float distance;
    private double distanceInKm;

    // FOR TESTING
    private String userName;

    /********************* ON CREATE ***************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view from activity_users.xml
        setContentView(R.layout.activity_users);

        // Initialise objects, listeners and views
        initViews();
        initListeners();
        initObjects();

        // Get current user email
        String emailFromIntent = getIntent().getStringExtra("EMAIL");

        // Get current user score
        score = databaseHelper.getScoreByEmail(emailFromIntent);

        // Get current user distance walked in meters
        distance = databaseHelper.getDistanceByEmail(emailFromIntent);

        // Convert distance walked in kilometers and round up to 2 decimal places
        distanceInKm = ((int)((distance/1000)*100+.5)/100.0);

        // Get current user name
        String nameFromIntent = databaseHelper.getNameByEmail(emailFromIntent.trim());
        userName = nameFromIntent;

        // Get the number of songs found for the user playing
        Integer songsFound = Integer.valueOf(databaseHelper.getSongByEmail(emailFromIntent))-1;

        // Get the difficulty that user is playing
        int difficulty_int = databaseHelper.getDifficultyByEmail(emailFromIntent);

        // Display a text view on top, welcoming the user
        textViewName.setText("Welcome " + nameFromIntent + "!");

        // Display statistics titles (eg. Coins, Kms Walked, etc.)
        textViewYourStatisticsLabels.setText("" +"\n"+"Coins:"+
                        "\n"+"\n"+"Score:"+
                        "\n"+"\n"+"Songs found:"+
                        "\n"+"\n"+"Kms Walked:"+
                        "\n"+"\n"+"Difficulty:");

        // Convert difficulty number to actual difficulty in text (eg. 1 to Very Hard)
        String difficulty_text = "";
        if(difficulty_int==5){difficulty_text = "Very Easy";}
        else if(difficulty_int==4){difficulty_text = "Easy";}
        else if(difficulty_int==3){difficulty_text = "Normal";}
        else if(difficulty_int==2){difficulty_text = "Hard";}
        else if(difficulty_int==1){difficulty_text = "Very Hard";}

        // Display statistical values (eg. "100" for Coins, "1.35" for Kms walked, etc.)
        textViewYourStatisticsValues.setText("\n"+databaseHelper.getCoinsByEmail(emailFromIntent)+
                        "\n"+"\n"+databaseHelper.getScoreByEmail(emailFromIntent)+
                        "\n"+"\n"+songsFound +
                        "\n"+"\n"+distanceInKm+
                        "\n"+"\n"+ difficulty_text);
    }

    /******************** INITIALISE VIEWS *********************************************************/
    private void initViews() {

        // Welcome Text
        textViewName = (TextView) findViewById(R.id.text_welcome);

        // Statistics Text
        textViewYourStatisticsLabels = (TextView) findViewById(R.id.text_labelsStatistics);
        textViewYourStatisticsValues = (TextView) findViewById(R.id.text_valueStatistics);

        // Play button
        appCompatButtonPlay = (AppCompatButton) findViewById(R.id.appCompatButton_play);

        // Settings button
        imageButtonSettings = (ImageButton) findViewById(R.id.imageButtonForSettings);

        // Songs button
        appCompatButtonSongs = (AppCompatButton) findViewById(R.id.appCompatButton_songs);

    }
    /******************** INITIALISE LISTENERS *****************************************************/
    private void initListeners() {

        // Play button listener
        appCompatButtonPlay.setOnClickListener(this);

        // Settings button listener
        imageButtonSettings.setOnClickListener(this);

        // Songs button listener
        appCompatButtonSongs.setOnClickListener(this);

    }

    /******************** INITIALISE OBJECTS *******************************************************/
    private void initObjects(){
        databaseHelper = new DatabaseHelper(activityusers);
    }

    /******************* ON CLICK ******************************************************************/
    public void onClick(View v){
        switch (v.getId()){

            // Case of clicking on Play button
            case R.id.appCompatButton_play:

                // Take user to Maps Activity
                Intent intentMap = new Intent(getApplicationContext(),MapsActivity.class);
                intentMap.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
                startActivity(intentMap);
                finish();
                break;

            // Case of clicking on Settings button
            case R.id.imageButtonForSettings:

                // Take user to Settings Activity
                Intent intentSet = new Intent(getApplicationContext(),SettingsActivity.class);
                intentSet.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
                startActivity(intentSet);
                finish();
                break;

            // Case of clicking on Songs button
            case R.id.appCompatButton_songs:

                // Take user to Songs Activity
                Intent intentSongs = new Intent(getApplicationContext(),SongsActivity.class);
                intentSongs.putExtra("EMAIL",getIntent().getStringExtra("EMAIL"));
                startActivity(intentSongs);
                finish();
                break;
        }
    }


    /*********************** ON PHONE BACK BUTTON PRESS ********************************************/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {

            // Take user back to Login Activity
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /******************************* TESTING *******************************************************/
    @VisibleForTesting

    public String testUser() {

        //String emailFromIntent = getIntent().getStringExtra("EMAIL");


        // Get current user name
        //String nameFromIntent = databaseHelper.getNameByEmail(emailFromIntent.trim());
       return userName ;


    }

}
