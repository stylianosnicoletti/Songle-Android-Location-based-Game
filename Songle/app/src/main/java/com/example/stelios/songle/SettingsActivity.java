package com.example.stelios.songle;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity
extends AppCompatActivity implements View.OnClickListener{
    private final AppCompatActivity activitysettings = SettingsActivity.this;

    // FOR BUTTONS
    private AppCompatButton appCompatButtonChangeDifficulty;
    private AppCompatButton appCompatButtonReset;
    private AppCompatButton appCompatButtonHelp;

    // FOR TEXT VIEW
    private TextView settingsTextView;

    // FOR MUSIC ENABLE/DISABLE SWITCH
    private Switch musicMute;

    // FOR DATABASE USE
    private DatabaseHelper databaseHelper;

    // FOR CURRENT USER EMAIL
    private String email;


    /************** ON CREATE **********************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view from activity_settings.xml
        setContentView(R.layout.activity_settings);

        // Initialise objects, views and listeners
        initObjects();
        initViews();
        initListeners();

        // Get user email
        email =getIntent().getStringExtra("EMAIL");

        // Set top text view
        settingsTextView.setText("Settings");

        // Set music switch enabled or disabled according to user previous preferences
        musicMute.setChecked(databaseHelper.getMusicByEmail(email)==1);

        // For debugging, music switch button
        System.out.println("IS CHECKED: " + musicMute.isChecked());

    }

    /**************** INITIALISE OBJECTS ***********************************************************/
    private void initObjects(){
        databaseHelper = new DatabaseHelper(activitysettings);
    }

    /**************** INITIALISE VIEWS *************************************************************/
    private void initViews() {

        // Difficulty Button
        appCompatButtonChangeDifficulty = (AppCompatButton) findViewById(R.id.appCompatButton_changedifficulty);

        // Reset Game Button
        appCompatButtonReset = (AppCompatButton) findViewById(R.id.appCompatButton_reset_game);

        // How To Play Button
        appCompatButtonHelp = (AppCompatButton) findViewById(R.id.appCompatButton_howtoplay);

        // Text on top
        settingsTextView = (TextView) findViewById(R.id.text_settings);

        // Music Switch Button
        musicMute = (Switch) findViewById(R.id.switch_music);
    }

    /**************** INITIALISE LISTENERS *********************************************************/
    private void initListeners() {

        // Difficulty Button Listener
        appCompatButtonChangeDifficulty.setOnClickListener(this);

        // Reset Game Button Listener
        appCompatButtonReset.setOnClickListener(this);

        // How To PLay Button Listener
        appCompatButtonHelp.setOnClickListener(this);

        // Music Switch Listener
        musicMute.setOnClickListener(this);
    }

    /**************** ON CLICK *********************************************************************/
    public void onClick(final View v){
                switch (v.getId()){

                    // On Difficulty Button Click
                    case R.id.appCompatButton_changedifficulty:

                        // Take user to Difficulty Activity
                        Intent difIntent = new Intent(getApplicationContext(), DifficultyActivity.class);
                        difIntent.putExtra("EMAIL", email);
                        startActivity(difIntent);
                        finish();
                        break;

                    // On Reset Game Button Click
                    case R.id.appCompatButton_reset_game:

                        // Initialise new Alert dialog
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);

                        // Setting Dialog Title
                        alertDialog.setTitle("Confirm Change...");

                        // Setting Dialog Message
                        alertDialog.setMessage("Are you sure you want to reset game?\nEverything achieved up to now will be erased..");

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                                // Take user to Users Activity (Main Screen)
                                Intent intentUsersActivity = new Intent(getApplicationContext(), UsersActivity.class);
                                intentUsersActivity.putExtra("EMAIL", getIntent().getStringExtra("EMAIL"));

                                // Reset distance walked for current user
                                databaseHelper.changeDistanceWalked(email,0);

                                // Reset score for current user
                                databaseHelper.changeScore(email,0);

                                // Reset songs found for current user
                                databaseHelper.changeSong(email,1);

                                // Reset words collected for current user
                                List<String> removedMarkers = new ArrayList<>();
                                removedMarkers.add(" ");
                                Gson gson = new Gson();
                                String inputString = gson.toJson(removedMarkers);
                                databaseHelper.changeWordsJson(email,inputString);

                                // Reset coins for current user
                                databaseHelper.changeCoins(email,0);

                                // Reset is hint used for current user
                                databaseHelper.changeHint(email,0);

                                startActivity(intentUsersActivity);

                                finish();

                                // On "YES" event, display toast informing user
                                Toast.makeText(getApplicationContext(), "New Game Started!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Setting Negative "NO" Button
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // On "NO" event, display toast informing user
                                Toast.makeText(getApplicationContext(), "Game data did not change!", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                        break;

                    // On How To Play Button Click
                    case R.id.appCompatButton_howtoplay:

                        // Take user to VideoPlayer Activity
                        Intent videoPlaybackActivity = new Intent(this, VideoPlayerActivity.class);
                        int res=this.getResources().getIdentifier("songle_help_video", "raw", getPackageName());
                        videoPlaybackActivity.putExtra("fileRes", res);
                        startActivity(videoPlaybackActivity);
                        break;

                    // On Music Switch Click
                    case R.id.switch_music:

                        // Set Music on background (enabled or disabled)
                        if(!musicMute.isChecked()){databaseHelper.changeMusic(email,0);musicMute.setChecked(false);

                            // For debugging, music switch
                            System.out.println("IS CHECKED IF: " + musicMute.isChecked());}
                        if(musicMute.isChecked()){databaseHelper.changeMusic(email,1);musicMute.setChecked(true);

                            // For debugging. music switch
                            System.out.println("IS NOT CHECKED IF: " + musicMute.isChecked());}
                        break;
                }
            }

    /*********************** ON PHONE BACK BUTTON PRESS ********************************************/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {

            // Send user to Users Activity
            Intent accountsIntent = new Intent(getApplicationContext(), UsersActivity.class);
            accountsIntent.putExtra("EMAIL", email);
            startActivity(accountsIntent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
