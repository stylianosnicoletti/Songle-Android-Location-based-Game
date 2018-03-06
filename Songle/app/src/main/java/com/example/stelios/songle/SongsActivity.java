package com.example.stelios.songle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SongsActivity
     extends AppCompatActivity  {
        private final AppCompatActivity activitysongs = com.example.stelios.songle.SongsActivity.this;

        // FOR DATABASE USE
        private DatabaseHelper databaseHelper;

        // FOR STORING USER'S EMAIL
        private String email;

        // FOR STORING SONGS
        private List<Song> songs_list = new ArrayList<>();

        // FOR NETWORK RECEIVER
        private NetworkReceiver receiver = new NetworkReceiver();

        // FOR LIST/TEXT VIEWS
        ListView songsListView;
        private TextView listenToVideo;

        // FOR STORING CURRENT PLAYING SONG BY USER
        private int songuser;

        // FOR SONG PARSING
        private SongParsing songParsing;

        //private Song song;

        /************************* ON CREATE *******************************************************/
        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            // Set content view of activity_songs.xml
            setContentView(R.layout.activity_songs);

            // Set list view for song list from xml
            songsListView = (ListView) findViewById(R.id.listSongs);

            // Get current user's email
            email = getIntent().getStringExtra("EMAIL");

            // Initialise objects, view and listeners
            initObjects();
            initViews();

            // If user has not collected any songs yet
            if(databaseHelper.getSongByEmail(email)==1) {

                // Set top text to "You have not found any words yet!"
                listenToVideo.setText("You have not found any words yet!");
            }
            // If the user has collected one or more song
            else{

                // Set top text to "Click on the Song to Listen it!"
                listenToVideo.setText("Click on the Song to Listen it!");
            }

            // Get current playing song by user
            songuser = databaseHelper.getSongByEmail(email);

            // Register BroadcastReceiver to track connection changes
            IntentFilter filter = new
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            receiver = new NetworkReceiver();
            this.registerReceiver(receiver, filter);
        }

    /********** INITIALISE OBJECTS AND VIEWS *******************************************************/
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activitysongs);
        songParsing = new SongParsing(activitysongs);
    }

    private void initViews() {
       listenToVideo = (TextView) findViewById(R.id.textViewClickForVideo);
    }

    /***********************  Accessing remote data and XML parsing ********************************/

    // A typical BroadcastReceiver to conserve data use
    public class NetworkReceiver extends BroadcastReceiver {

        // Url for getting Songs
        String urlSongs = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml";

        /****************************** ON RECEIVE *************************************************/
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null
                    && networkInfo.getType() ==
                    ConnectivityManager.TYPE_WIFI) {

                // Wi´Fi is connected, so use Wi´Fi
                new DownloadXmlTask().execute(urlSongs);

            } else if (networkInfo != null
                    && networkInfo.getType() ==
                    ConnectivityManager.TYPE_MOBILE) {

                // Have a network connection and permission, so use data
                new DownloadXmlTask().execute(urlSongs);
            } else {

                // No Wi´Fi and no permission, or no network connection
                // Display toast informing user
                Toast noInternet = Toast.makeText(activitysongs,"No Internet Connection!", Toast.LENGTH_SHORT);
                noInternet.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
                noInternet.show();
            }
        }
    }

    /***************** DOWNLOAD XML TASK ***********************************************************/
    private class DownloadXmlTask extends AsyncTask<String, Void, List<Song>> {

        /************ DO IN BACKGROUND *************************************************************/
        @Override
        protected List<Song> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /************* ON POST EXECUTE *************************************************************/
        @Override
        protected void onPostExecute(final List<Song> songslist) {

            // Get current playing song for that user
            songuser = databaseHelper.getSongByEmail(email);

            // Initiate a new list of Strings
           List<String> result = new ArrayList<>();

            // Fill in result list with songs the user already found
            for (Song p : songs_list) {

                // For debugging
                System.out.println(p.number + " " + p.artist + " " + p.title + " " + p.link);

                // Up to which song number the user has found songs? Compare with all songs available
                if (songuser > Integer.valueOf(p.number)) {

                    // For debugging
                    System.out.println("SONG USER: "+songuser + "INTEGER VALUE OF P NUMBER: "+Integer.valueOf(p.number));
                    System.out.println(p.number + "   " + p.artist + " - " + p.title );

                    // Add to result list songs already found
                    result.add(p.number + "   " + p.artist + " - " + p.title );
                }
            }

            // Fill in List View with songs already found
            ArrayAdapter<String> songss = new ArrayAdapter<String>(activitysongs,android.R.layout.simple_dropdown_item_1line, result);
            songsListView.setAdapter(songss);

            // When user clicks on a song from list view
            songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // Get song String on that position from list view
                    String entry = (String) parent.getAdapter().getItem(position);

                    // For debugging
                    System.out.println("*** ENTRY *** "+entry);

                    // Take song number of song clicked
                    String aa = entry.substring(0, entry.indexOf(" "));

                    // For debugging
                    System.out.println("*** ENTRY ONLY SONG NUMBER *** "+aa);


                    for(Song s : songslist){

                    if(s.number.equals(aa)){

                        // For debugging, print song YouTube Link
                        System.out.println(s.link);

                        // Check if YouTube App is installed on the device
                        String packageName = "com.google.android.youtube";
                        boolean isYoutubeInstalled = isAppInstalled(packageName);

                        // If it is installed
                        if(isYoutubeInstalled) {

                            // Play song on YouTube using it's ling
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s.link));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setPackage("com.google.android.youtube");
                            startActivity(intent);
                        }
                        // If it is not installed
                        else{

                            // Inform user that he does not have the YouTube App installed
                            Toast inrange = Toast.makeText(activitysongs, "NO YOUTUBE APPLICATION INSTALLED!", Toast.LENGTH_LONG);
                            inrange.show();
                        }
                    }
                    }
                }
            });
        }
    }
    /*********** LOAD XML FROM NETWORK *********************************************************/
    // Method loadXmlFromNetwork, returns a List of Songs
    private List<Song> loadXmlFromNetwork(String urlString_Songs) throws IOException, XmlPullParserException {

        // InputStream for Song
        InputStream streamSongs = downloadUrl(urlString_Songs);

        // Parse Songs
        songs_list = songParsing.parseSongs(streamSongs);

        // For debugging, print songs parsed
        for (Song p : songs_list) {
            System.out.println(p.number + " " + p.artist + " " + p.title + " " + p.link);
        }
        return songs_list;
    }

    /************************ DOWNLOAD URL *****************************************************/
    // Given a string representation of a URL, sets up a connection and gets an input stream
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

    /***************************** IS APP INSTALLED ************************************************/
    // Check if the package is installed on the phone
    // To be used to check if YouTube App is installed
    protected boolean isAppInstalled(String packageName) {
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        }
        else {
            return false;
        }
    }

    /*********************** ON PHONE BACK BUTTON PRESS ********************************************/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // Take user to Users Activity (Main)
            Intent userIntent = new Intent(getApplicationContext(), UsersActivity.class);
            userIntent.putExtra("EMAIL", email);
            startActivity(userIntent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
