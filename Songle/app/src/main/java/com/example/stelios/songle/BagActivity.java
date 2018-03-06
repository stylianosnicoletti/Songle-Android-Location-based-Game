package com.example.stelios.songle;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BagActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activitybagg = com.example.stelios.songle.BagActivity.this;

    // FOR DATABASE CALLING
    private DatabaseHelper databaseHelper;

    // FOR STORING CURRENT USER EMAIL
    private String email;

    // FOR LIST VIEW OF WORDS
    ListView bagListView;

    // FOR STORING ALREADY CATCHED PLACE MARKS IDS (WORD LOCATION)
    List<String> idLyrics = new ArrayList<>();

    // FOR JSON
    private Gson gson = new Gson();

    // FOR STORING ALL WORDS (LYRICS) OF CURRENT SONG
    private String txt_string;

    // FOR NETWORK RECEIVER
    private NetworkReceiver receiver = new NetworkReceiver();

    // FOR TEXT VIEW ON TOP OF SCREEN
    private TextView youhavecolleted;

    // FOR BUTTONS
    private AppCompatButton submitSongButton;
    private ImageButton hintButton;
    private ImageButton surrenderButton;

    // FOR STORING CURRENT SONG TITLE
    private String titleOfSong;

    // FOR STORING CURRENT SONG ARTIST
    private String artistOfSong;

    // FOR STORING INPUT BY USER WHEN GUESSING THE SONG
    private TextInputEditText songIn;

    // URL OF CURRENT PLAYING SONG
    private String songforurl;

    // FOR STORING SCORE OF CURRENT USER
    private int score;

    // FOR STORING COINS OF CURRENT USER
    private int coins;

    // FOR STORING IF A USER USED OR NOT THE HINT AVAILABLE
    private int hint;

    private LyricsParsing lyricsParsing;

    /**************************** ON CREATE ********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        bagListView = (ListView) findViewById(R.id.list);

        // Get email of current playing user
        email = getIntent().getStringExtra("EMAIL");

        // Get title of current playing song
        titleOfSong = getIntent().getStringExtra("TITLEOFSONG");

        // Get artist of current playing song
        artistOfSong = getIntent().getStringExtra("ARTISTSONG");

        // For debugging, print current playing song
        System.out.println(titleOfSong+" *** SONG ***");

        // Initialise Objects
        initObjects();

        // If the user has used or not the hint of the current song (either 1 or 0)
        hint = databaseHelper.getHintByEmail(email);

        // Get the coins that the current user has
        coins = databaseHelper.getCoinsByEmail(email);

        // Get the current playing song number
        int songint = databaseHelper.getSongByEmail(email);

        // For dealing with Songs URL where song 1 is 01, song 2 is 02 and so on...
        if(songint == 1 ||songint == 2 ||songint == 3 ||songint == 4 ||songint == 5 ||songint == 6 ||songint == 7 ||songint == 8 ||songint == 9 ){
            songforurl = "0"+ String.valueOf(databaseHelper.getSongByEmail(email));
        }else{songforurl = String.valueOf(databaseHelper.getSongByEmail(email));}

        // For debugging, print current song url
        System.out.println(songforurl+" *** SONG URL ***");

        // Initialise Views and Listeners
        initViews();
        initListeners();

        // Get word ids collected from the user for the current song
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        idLyrics = gson.fromJson(databaseHelper.getJsonByEmail(email), type);

        // Register BroadcastReceiver to track connection changes
        IntentFilter filter = new
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
    }

    /*************************** INITIALISE OBJECTS ************************************************/
    private void initObjects() {

        databaseHelper = new DatabaseHelper(activitybagg);
        lyricsParsing = new LyricsParsing(activitybagg);
    }

    /*************************** INITIALISE VIEWS **************************************************/
    private void initViews() {

        // Text on top
        youhavecolleted = (TextView) findViewById(R.id.textViewYouHaveCollected);

        // Submit song button
        submitSongButton = (AppCompatButton) findViewById(R.id.appCompatButton_guessTheSong);

        // Hint button
        hintButton =(ImageButton) findViewById(R.id.imageButtonHint);

        // Song User Parse In
        songIn = (TextInputEditText) findViewById(R.id.textInputEditTextSong);

        // Surrender button
        surrenderButton=(ImageButton) findViewById(R.id.imageButtonSurrender);
    }

    /*************************** INITIALISE LISTENERS **********************************************/
    private void initListeners() {

        // Submit song button listener
        submitSongButton.setOnClickListener(this);

        // Hint button listener
        hintButton.setOnClickListener(this);

        // Surrender button listener
        surrenderButton.setOnClickListener(this);
    }

    /*************************** ON CLICK **********************************************************/
    public void onClick(View v) {

        // Prepare an empty list of word collected for the next song
        List<String> removedMarkers = new ArrayList<>();
        removedMarkers.add(" ");
        Gson gson = new Gson();
        String inputString = gson.toJson(removedMarkers);

        switch (v.getId()){

            // Case where the user clicks on submitting the song title parsed in by him
            case R.id.appCompatButton_guessTheSong:

                // Song title parsed in by user
                songIn = (TextInputEditText) findViewById(R.id.textInputEditTextSong);

                // Actual song title
                String result = songIn.getText().toString().trim();

                // Compare the actual song title string with users parsed in title string
                // Using Levenshtein distance with threshold 0.66
                // If the titled parsed is 66% or more similar to the actual one, take it as correct
                if(StringSimilarity.similarity(titleOfSong,result)>0.660){

                    // For debugging, guessing the right song
                    System.out.println("*** Right Song Guessed ***");

                    // Get current user score
                    score = databaseHelper.getScoreByEmail(email);

                    // Update current user score, add 1000 points to it for guessing right
                    score=score+1000;
                    databaseHelper.changeScore(email,score);

                    // Prepare isHint used for the next song
                    databaseHelper.changeHint(email,0);

                    // Display a dialog box telling the user that his song guess is correct
                    ViewDialogSongFound songFoundAlert = new ViewDialogSongFound();
                    songFoundAlert.showDialog(activitybagg, "Song Found! New Song Added to Map!");

                    // Update current song to next one available
                    databaseHelper.changeWordsJson(email,inputString);
                    databaseHelper.changeSong(email,databaseHelper.getSongByEmail(email)+1);

                }else {

                    // If the titled parsed is less than 66% similar to the actual one, take it as wrong
                    // Display a dialog box telling the user that his song guess is wrong
                    ViewDialogNoSongFound noSongFoundAlert = new ViewDialogNoSongFound();
                    noSongFoundAlert.showDialog(activitybagg, "Try Again!");

                    // For debugging
                    System.out.println(songIn.getText().toString());
                    System.out.println(titleOfSong);
                }
                break;

            // Case where the user clicks on the hint button
            case R.id.imageButtonHint:

                // If hint already bought
                if(hint ==1) {

                    // Display a dialog box with the hint (Artist)
                    ViewDialogHint hintAlert = new ViewDialogHint();
                    hintAlert.showDialog(activitybagg, "Artist : " + artistOfSong);
                }
                // If hint was not bought yet
                else if(hint==0){

                    // Display a dialog box asking the user to buy hint for 25 coins
                    ViewDialogBuyHint hintbuy = new ViewDialogBuyHint();
                    hintbuy.showDialog(activitybagg,"Hint Costs 25 Coins!");
                }
                break;

            // Case where the user clicks on the surrender button
            case R.id.imageButtonSurrender:

                // Display a dialog box informing the user that skipping the song will not give him any score points
                ViewDialogSurrender surAlert = new ViewDialogSurrender();
                surAlert.showDialog(activitybagg,"You will not receive any points by skipping the song!");
                break;
        }
    }

    /***********************  Accessing remote data and XML parsing ********************************/

    // A typical BroadcastReceiver to conserve data use
    public class NetworkReceiver extends BroadcastReceiver {

        // Url for getting Lyrics for current song played
        String urlLyrics = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+songforurl+"/words.txt";

        /****************************** ON RECEIVE *************************************************/
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            // For debugging, print current song url
            System.out.println(songforurl+" *** CURRENT SONG URL ***");

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null
                    && networkInfo.getType() ==
                    ConnectivityManager.TYPE_WIFI) {

                // Wi´Fi is connected, so use Wi´Fi
                new DownloadXmlTask().execute(urlLyrics);

            } else if (networkInfo != null
                    && networkInfo.getType() ==
                    ConnectivityManager.TYPE_MOBILE) {

                // Have a network connection and permission, so use data
                new DownloadXmlTask().execute(urlLyrics);

            } else {

                // If there is no internet connection
                // Display a toast informing the user
                Toast noInternet = Toast.makeText(activitybagg,"No Internet Connection!", Toast.LENGTH_SHORT);
                noInternet.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
                noInternet.show();
            }
        }
    }


    /***************** DOWNLOAD XML TASK ***********************************************************/
    private class DownloadXmlTask extends AsyncTask<String, Void, String[]> {

        /************ DO IN BACKGROUND *************************************************************/
        @Override
        protected String[] doInBackground(String... urls) {
            try {

                // Call loadXmlFromNetwork for the url initiated above
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /************* ON POST EXECUTE *************************************************************/
        @Override
        protected void onPostExecute(String[] arraywords) {

            // Initialise list updated to store actual words collected
            List<String> updated = new ArrayList<>();

            // Count words collected for current song
            int wordsCollected = idLyrics.size() -1;

            // Displaying the user user the how much words he have collected for current song
            youhavecolleted.setText("You have collected " + wordsCollected + " words !");

            // Much word ids (eg. 1:15 line 1 location 15) from place marks collected to words from lyrics
            // Get actual words collected into updated list
            for (String s : idLyrics) {
                for (int i = 0; i < arraywords.length; i++) {
                    if (returnLine(s).equals(arraywords[i])) {
                        updated.add(arraywords[i + Integer.valueOf(returnLocation(s))].toUpperCase());
                        System.out.println(arraywords[i + Integer.valueOf(returnLocation(s))]);
                    }
                }
            }

            // Fill in list view with words collected for current song
            ArrayAdapter<String> words = new ArrayAdapter<String>(activitybagg,android.R.layout.simple_dropdown_item_1line, updated);
            bagListView.setAdapter(words);
        }
    }

        /*********** LOAD XML FROM NETWORK *********************************************************/
        // Method loadXmlFromNetwork, returns a String Array
        private String[] loadXmlFromNetwork(String urlString_Lyrics) throws IOException {

            // InputStream for Song Lyrics
            InputStream streamLyrics = downloadUrl(urlString_Lyrics);

            // Parse Lyrics
            txt_string = lyricsParsing.parseLyrics(streamLyrics);

            // Separate lyrics of the current song
            String[] words = txt_string.split("\\s+");
            for (int i = 0; i < words.length; i++) {
                words[i] = words[i].replaceAll("[^\\w]", "");
            }
            return words;
        }

        /************************ DOWNLOAD URL *****************************************************/
        // Given a string representation of a URL, sets up a connection and gets an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Also available: HttpsURLConnection
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }

    /**************************** RETURN LINE ******************************************************/
    // Used to get the line from place mark title, where it will be then used to get the word from lyrics
    // If for example the place mark title is (1:15) then get line 1
    public String returnLine (String LineLocation){
        String[] parts = LineLocation.split(":");
        return parts[0];
    }

    /**************************** RETURN LOCATION **************************************************/
    // Used to get the location of the word in that line from place mark title, where it will be then used to get the word from lyrics
    // If for example the place mark title is (1:15) then get location 15
    public String returnLocation (String LineLocation){
        return LineLocation.substring(LineLocation.lastIndexOf(":")+1);
    }

    /****************** WRONG SONG GUESSED DIALOG BOX **********************************************/
    public class ViewDialogNoSongFound {

        public void showDialog(Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.not_found_dialog_layout);

            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            dialog.show();

        }
    }

    /****************** CORRECT SONG GUESSED DIALOG BOX **********************************************/
    public class ViewDialogSongFound {

        public void showDialog(Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.found_dialog_layout);

            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);

            // If song guessed is correct, and user clicks on okay button
            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Take user to Maps activity to play with the next song
                    Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);

                    // Take user email to Maps Activity for identification and future use
                    mapIntent.putExtra("EMAIL", email);
                    startActivity(mapIntent);
                    finish();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

    /****************** HINT DIALOG BOX ************************************************************/
    public class ViewDialogHint {

        public void showDialog(Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.hint_dialog_layout);

            // Display the hint to the user (Artist)
            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

    /****************** SURRENDER DIALOG BOX ************************************************************/
    public class ViewDialogSurrender {

        public void showDialog(Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.surrender_dialog_layout);

            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);

            // When user accepts to skip the current song
            Button dialogButtonOk = (Button) dialog.findViewById(R.id.btn_dialog_surrender_yes);
            dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Prepare an empty list of words collected for the next song
                    List<String> removedMarkers = new ArrayList<>();
                    removedMarkers.add(" ");
                    Gson gson = new Gson();
                    String inputString = gson.toJson(removedMarkers);

                    // Update database isHint used to 0 for new song
                    databaseHelper.changeHint(email,0);

                    // Display Dialog Box informing the user that skipping the song will not give him any score points
                    ViewDialogSongFound songFoundAlert = new ViewDialogSongFound();
                    songFoundAlert.showDialog(activitybagg, "Song Skipped! New Song Added to Map!");

                    // Update database of current user
                    // Add next song as current playing song
                    databaseHelper.changeWordsJson(email,inputString);
                    databaseHelper.changeSong(email,databaseHelper.getSongByEmail(email)+1);

                    dialog.dismiss();
                }
            });

            // When user does not accept to skip the current song
            Button dialogButtonCancelSur = (Button) dialog.findViewById(R.id.btn_dialog_surrender_no);
            dialogButtonCancelSur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Do nothing in case of not accepting
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    /****************** BUY HINT DIALOG BOX ************************************************************/
    public class ViewDialogBuyHint {

        public void showDialog(Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.buy_hint_dialog_layout);

            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);

            // If user accepts to buy the hint for 25 coins
            Button dialogButtonOk = (Button) dialog.findViewById(R.id.btn_dialog_ok);
            dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // If the current user has 25 coins or more to buy the hint
                    if(coins>=25){

                        // Decrement current user's coins by 25
                        databaseHelper.changeCoins(email,coins-25);

                        // Update isHint used to 1
                        databaseHelper.changeHint(email,1);

                        // Display a dialog box showing the user the hint (Artist)
                        ViewDialogHint hintAlert = new ViewDialogHint();
                        hintAlert.showDialog(activitybagg, "Artist : " + artistOfSong);
                        hint=1;
                    }

                    // If the current user has less than 25 coins
                    else{

                        // Display a dialog box informing the user that he does not have enough coins
                        ViewDialogNoSongFound noMoney = new ViewDialogNoSongFound();
                        noMoney.showDialog(activitybagg, "Not Enough Coins!");
                    }
                    dialog.dismiss();
                }
            });

            // If user does not accept to buy the hint for 25 coins
            Button dialogButtonCancel = (Button) dialog.findViewById(R.id.cancel_button);
            dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            // Do nothing in that case
            dialog.show();
        }
    }

    /*********************** ON PHONE BACK BUTTON PRESS ********************************************/
    @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {

                // Take user to Maps activity
                Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);

                // Send current user email to Maps activity for future use
                mapIntent.putExtra("EMAIL", email);

                startActivity(mapIntent);
                finish();
                return true;
            }

            return super.onKeyDown(keyCode, event);
        }
    }


