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

import static com.example.stelios.songle.R.id.appCompatButton_startover;
import static com.example.stelios.songle.R.id.appCompatButton_waitfornewsongs;


public class TheEndActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activityTheEnd = TheEndActivity.this;

    // FOR TEXT VIEW
    private TextView textViewTheEnd;

    // FOR BUTTONS
    private AppCompatButton appCompatButtonNewGame;
    private AppCompatButton appCompatButtonWaitForSongs;

    // FOR DATABASE USE
    private DatabaseHelper databaseHelper;

    // FOR STORING CURRENT USER'S EMAIL
    private String email;

    /********************** ON CREATE **************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Set content view of activity_thenend.xml
        setContentView(R.layout.activity_theend);

        // Initialise objects, views and listeners
        initObjects();
        initViews();
        initListeners();

        // Get current user email
        email = getIntent().getStringExtra("EMAIL");

        // Set text " The End "
        textViewTheEnd.setText("\nTHE END!");
    }

    /*************** INITIALISE OBJECTS, VIEWS AND LISTENERS ***************************************/
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activityTheEnd);
    }

    private void initViews() {

        // Text "The End" View
        textViewTheEnd = (TextView) findViewById(R.id.textViewTheEnd);

        // New Game Button View
        appCompatButtonNewGame = (AppCompatButton) findViewById(appCompatButton_startover);

        // Wait For New Songs Button View
        appCompatButtonWaitForSongs = (AppCompatButton) findViewById(appCompatButton_waitfornewsongs);

    }

    private void initListeners() {

        // New Game Button Listener
        appCompatButtonNewGame.setOnClickListener(this);

        // Wait For New Songs Button Listener
        appCompatButtonWaitForSongs.setOnClickListener(this);
    }

    /********************** ON CLICK ***************************************************************/
    public void onClick(final View v) {

        // Initialise new Intent to Users Activity (Main Screen)
        Intent intentUsersActivity = new Intent(getApplicationContext(), UsersActivity.class);
        intentUsersActivity.putExtra("EMAIL", getIntent().getStringExtra("EMAIL"));

        // Initialise and Alert Dialog
         AlertDialog.Builder alertDialog = new AlertDialog.Builder(TheEndActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Change...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to start a new game?");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                switch (v.getId()){

                    // Case where user clicks on New Game Button
                    case R.id.appCompatButton_startover:

                        // Send him to Users Activity (Main Screen)
                        Intent intentUsersActivity = new Intent(getApplicationContext(), UsersActivity.class);
                        intentUsersActivity.putExtra("EMAIL", getIntent().getStringExtra("EMAIL"));

                        // Reset all of its records on database
                        databaseHelper.changeDistanceWalked(email,0);
                        databaseHelper.changeScore(email,0);
                        databaseHelper.changeSong(email,1);
                        List<String> removedMarkers = new ArrayList<>();
                        removedMarkers.add(" ");
                        Gson gson = new Gson();
                        String inputString = gson.toJson(removedMarkers);
                        databaseHelper.changeWordsJson(email,inputString);
                        databaseHelper.changeCoins(email,0);
                        databaseHelper.changeHint(email,0);

                        startActivity(intentUsersActivity);
                        finish();
                        break;

                }

                // On "YES" event, display a toast informing the user that new game started
                Toast.makeText(getApplicationContext(), "New Game Started!", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // On "NO" event, display a toast informing the user that nothing has changed
                Toast.makeText(getApplicationContext(), "Nothing changed!", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });


        switch (v.getId()) {

            // Case where user clicks on Start New Game
            case R.id.appCompatButton_startover:

                // Show alert, waiting for confirmation by user
                alertDialog.show();
                break;

            // Case where user clicks on Wait For New Songs Button
            case R.id.appCompatButton_waitfornewsongs:

                // Change nothing
                // Take user to Main Screen
                startActivity(intentUsersActivity);
                finish();
                break;
        }
    }

    /*********************** ON PHONE BACK BUTTON PRESS ********************************************/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {

            // Take user to Users Activity (Main Screen)
            Intent accountsIntent = new Intent(getApplicationContext(), UsersActivity.class);
            accountsIntent.putExtra("EMAIL", email);
            startActivity(accountsIntent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}



