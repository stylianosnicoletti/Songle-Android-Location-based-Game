package com.example.stelios.songle;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity
        extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        View.OnClickListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSIONS_REQUEST_ACCESS_FINELOCATION = 1;
    private boolean mLocationPermissionGranted = false;

    // FOR LAST LOCATION BEFORE CURRENT ONE
    private Location mLastLocation;

    // FOR MAPS ACTIVITY
    private static final String TAG = "MapsActivity";
    private final MapsActivity activitymap = MapsActivity.this;

    // FOR STORING EMAIL OF THE USER PLAYING
    private String email;
    // STORING THE LAST SONG THE USER IS PLAYING
    private int lastSongIndex;

    //FOR MY WORDS BUTTON
    private AppCompatButton mywordsbutton;

    // FOR XML PARSE
    private List<Song> songs_list = new ArrayList<>();
    private List<Placemark> placemark_list = new ArrayList<>();

    // FOR USING DATABASE
    private DatabaseHelper databaseHelper;

    // FOR STORING THE CURRENT DIFFICULTY OF THE GAME
    private int difficulty_int;

    //FOR LYRICS TXT
    private String txt_string = null;
    private String[] lyrics_array;

    //The BroadcastReceiver that tracks network connectivity changes
    private NetworkReceiver receiver = new NetworkReceiver();

    // FOR MARKERS
    private List<String> removedMarkers = new ArrayList<>();

    // FOR JSON
    private Gson gson = new Gson();

    //FOR CURRENT USER SCORE
    private int score;

    //FOR CURRENT USER PLAYING SONG
    private String songintforurl;

    //FOR CURRENT LOCATION
    LatLng mylocation;

    //FOR CURRENT USER DISTANCE WALKED
    float metersWalked;

    //FOR MUSIC PLAYED IN BACKGROUND
    MediaPlayer mediaPlayer;

    // FOR USER CURRENT COINS COLLECTED
    int coins;

    // FOR SONG PARSING
    private SongParsing songParsing;

    // FOR LYRICS PARSING
    private LyricsParsing lyricsParsing;

    // FOR PLACE MARKS PARCING
    private PlaceMarkParsing placeMarkParsing;

    /**************************** ON CREATE ********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialise button and listener
        mywordsbutton = (AppCompatButton) findViewById(R.id.buttonMapToBag);
        mywordsbutton.setOnClickListener(activitymap);

        // Initialise Objects
        initObjects();

        // Get the email for current user playing as identification for further use
        String emailFromI = getIntent().getStringExtra("EMAIL");
        email = emailFromI;

        // Play music in background if the user enabled it on settings screen
        if (databaseHelper.getMusicByEmail(email) == 1) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.my_song);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        // Get distance walked for the current user, using his email as identity
        metersWalked = databaseHelper.getDistanceByEmail(email);

        // Get difficulty for the current user, using his email as identity
        difficulty_int = databaseHelper.getDifficultyByEmail(emailFromI.trim());

        // Get playing song for the current user, using his email as identity
        int songint = databaseHelper.getSongByEmail(email);

        // For dealing with Songs URL where song 1 is 01, song 2 is 02 and so on...
        if (songint == 1 || songint == 2 || songint == 3 || songint == 4 || songint == 5 || songint == 6 || songint == 7 || songint == 8 || songint == 9) {
            songintforurl = "0" + String.valueOf(databaseHelper.getSongByEmail(email));
        } else {
            songintforurl = String.valueOf(databaseHelper.getSongByEmail(email));
        }
        System.out.println(songint);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Register BroadcastReceiver to track connection changes
        IntentFilter filter = new
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
    }

    /********************* ON MAP READY ************************************************************/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Set style to day or night mode
        setMapStyle();

        View locationButton = ((View) activitymap.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

        // Set the position of the location button on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mMap.setPadding(0, 0, 60, 92);

        // Location in center Edinburgh
        LatLng edinburgh = new LatLng(55.953251, -3.188267);

        // When user clicks on a place mark
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                // When place mark collected
                if (mylocation != null) {
                    // Hide Marker when collected
                    marker.hideInfoWindow();

                    // Current location
                    Location here = new Location("");
                    here.setLatitude(mylocation.latitude);
                    here.setLongitude(mylocation.longitude);

                    // Marker location
                    Location markerloc = new Location("");
                    markerloc.setLatitude(marker.getPosition().latitude);
                    markerloc.setLongitude(marker.getPosition().longitude);

                    // Distance of marker to current location
                    float distance = here.distanceTo(markerloc);

                    // Limit in meters where the user can collect a marker is set to 25
                    float limit = 25;

                    // If the distance from user to place mark is less than 15 meters
                    if (distance < limit) {

                        // Add word marker collected to removeMarkers List
                        removedMarkers.add(marker.getTitle().toString());
                        System.out.println(marker.getTitle());

                        // For adding removedMarkers List for current user to database
                        String inputString = gson.toJson(removedMarkers);
                        databaseHelper.changeWordsJson(email, inputString);

                        // Add ten points to current user score and save in database
                        score = databaseHelper.getScoreByEmail(email);
                        score = score + 10;
                        databaseHelper.changeScore(email, score);

                        // Add 1 coin to current user and save in database
                        coins = databaseHelper.getCoinsByEmail(email);
                        coins = coins + 1;
                        databaseHelper.changeCoins(email, coins);

                        // Getting word stored in the place mark collected
                        String wordForDialog = "";

                        for (int i = 0; i < lyrics_array.length; i++) {
                            if (returnLine(marker.getTitle()).equals(lyrics_array[i])) {
                                wordForDialog = (lyrics_array[i + Integer.valueOf(returnLocation(marker.getTitle()))]);
                            }
                        }

                        // Remove word marker
                        marker.remove();

                        // Display dialog box with the word collected
                        ViewDialogWordFound wordFoundAlert = new ViewDialogWordFound();
                        wordFoundAlert.showDialog(activitymap, "Word \"" + wordForDialog.toUpperCase() + "\" Found!");


                    } else {

                        // If the user clicks on word place mark and is at a distance from it larger than 15 meters
                        // Display a dialog box telling the user that is far away from place mark
                        ViewDialogNoWordFound wordNotFoundAlert = new ViewDialogNoWordFound();
                        wordNotFoundAlert.showDialog(activitymap, "Too Far Away!");
                    }
                }
                return true;
            }
        });

        // Making the user's location visible
        try {

            // Visualise current position with a small blue circle
            mMap.setMyLocationEnabled(true);

        } catch (SecurityException se) {
            System.out.println("Security exception thrown [onMapReady]");
        }

        // Add "My location" button to the user interface
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // When user clicks on location button
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (mylocation != null) {

                    // Move and zoom camera to current location
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
                    float maxZoom = 17.0f;
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(maxZoom));
                } else {

                    // If location cannot be found (eg. not location service enabled)
                    // Display a toast telling the user that location is not available
                    Toast noLocation = Toast.makeText(activitymap, "No Location Available!", Toast.LENGTH_SHORT);
                    noLocation.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    noLocation.show();

                    // Recheck if permission was granted ( Added to fix when granting permission and location was turned off)
                    createLocationRequest();
                    if (ContextCompat.checkSelfPermission(activitymap,
                            Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                        mLastLocation =
                                LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

               }}
               return true;
           }
       });

    }
    /**************** CHANGING MAP STYLE ACCORDING TO TIME OF DAY **********************************/
    private void setMapStyle() {
        String currentTimeString = DateFormat.getTimeInstance().format(new Date());
        System.out.println("*date*" + currentTimeString);
        String substring = currentTimeString.substring(Math.max(currentTimeString.length() - 2, 0));
        substring = substring.toUpperCase();
        System.out.println("*date*" + substring);
        Integer time = Integer.valueOf(returnLine(currentTimeString));
        System.out.println(time);

        /************ FOR NIGHT MODE 18:00-5:59 ****************************************************/
        if (((time == 0 | time == 1 | time == 2 | time == 3 | time == 4 | time == 5 | time == 18 | time == 19 | time == 20 | time == 21 | time == 22 | time == 23)&& (!substring.equals("AM") && !substring.equals("PM")))
            | (substring.equals("AM") && ((time == 12| time == 1 | time == 2 | time == 3 | time == 4 | time == 5)))
            | (substring.equals("PM") && ((time == 6| time == 7 | time == 8 | time == 9 | time == 10 | time == 11))))
        {
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = mMap.setMapStyle(

                        MapStyleOptions.loadRawResourceStyle(
                                MapsActivity.this, R.raw.jsonmapnightmode));

                if (!success) {
                    Log.e("MapsActivity", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("MapsActivity", "Can't find style.", e);
            }
        }

        /************** FOR DAY MODE 6:00-17:59 ****************************************************/
        if (((time == 6 | time == 7 | time == 8 | time == 9 | time == 10 | time == 11 | time == 12 | time == 13 | time == 14 | time == 15 | time == 16 | time == 17) && (!substring.equals("AM") && !substring.equals("PM")))
                | (substring.equals("AM") && ((time == 6| time == 7 | time == 8 | time == 9 | time == 10 | time == 11)))
                | (substring.equals("PM") && ((time == 12| time == 1 | time == 2 | time == 3 | time == 4 | time == 5))))
        {
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = mMap.setMapStyle(

                        MapStyleOptions.loadRawResourceStyle(
                                MapsActivity.this, R.raw.jsonmapdaymode));

                if (!success) {
                    Log.e("MapsActivity", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("MapsActivity", "Can't find style.", e);
            }
        }
    }

    /************************ onStart(), onStop(), onResume() **************************************/
    @Override
    public void onStart() {
        super.onStart();
        initObjects();

        // Play music in background if the user has enabled it from settings
        if(databaseHelper.getMusicByEmail(email)==1){
        mediaPlayer.start();}
        mGoogleApiClient.connect();
    }
    @Override
    public void onResume(){
        super.onResume();
        initObjects();

        // Play music in background if the user has enabled it from settings
        if(databaseHelper.getMusicByEmail(email)==1){
        mediaPlayer.start();}
    }

    @Override
    public void onStop() {
        super.onStop();
        initObjects();

        // Play music in background if the user has enabled it from settings
        if(databaseHelper.getMusicByEmail(email)==1){
        mediaPlayer.pause();}

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**************** INITIALISE OBJECTS ***********************************************************/
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activitymap);
        songParsing = new SongParsing(activitymap);
        lyricsParsing = new LyricsParsing(activitymap);
        placeMarkParsing = new PlaceMarkParsing(activitymap);
    }


    /*************************** LOCATION REQUEST **************************************************/
    public void createLocationRequest() {
        // Set parameters for location request
        LocationRequest mLocationRequest = new LocationRequest();

        // Every 5 seconds
        // mLocationRequest.setFastestInterval(5000);

        // At most every second
        mLocationRequest.setFastestInterval(1000);  // at most every second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Can we access the user's current location
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    /*************************************** ON CONNECTED ******************************************/
    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            createLocationRequest();

        } catch (java.lang.IllegalStateException ise) {
            System.out.println("IllegalStateException thrown [onConnected]");
        }

        // Can we access the user’s current location?
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mLastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            // Avoid app crash when location cannot be taken
            // Automatic zoom at current location when user start Map Activity
            if (mLastLocation != null) {
                LatLng lastlocationlatlong = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(lastlocationlatlong)
                        .zoom(15)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINELOCATION);
            onConnected(connectionHint);

                try {

                    // Visualise current position with a small blue circle
                    mMap.setMyLocationEnabled(true);
                    // Add "My location" button to the user interface
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);

                } catch (SecurityException se) {
                    System.out.println("Security exception thrown [onMapReady]");
                }

            }
        }


    /******************************* onConnectionSuspended, onConnectionFailed *********************/
    @Override
    public void onConnectionSuspended(int flag) {
        System.out.println(" >>>> onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently
        System.out.println(" >>>> onConnectionFailed");
    }

    /********************* ON LOCATION CHANGED *****************************************************/
    @Override
    public void onLocationChanged(Location current) {
        System.out.println(
                " [onLocationChanged] Lat/long now (" +
                        String.valueOf(current.getLatitude()) + "," +
                        String.valueOf(current.getLongitude()) + ")");

        mylocation = new LatLng(current.getLatitude(),current.getLongitude());

        // Avoid app crash when location cannot be taken
        if(mLastLocation!=null) {

            // For debugging, print current and last location
            System.out.println(current.toString() + "**CURRENT LOCATION**");
            System.out.println(mLastLocation.toString() + "**LAST LOCATION**");

            // If the user travels faster than 2.78 meters per second do not add it to distance walked
            // That is when user travels more than 10 km/h
            // For not driving and playing
            if (mLastLocation.distanceTo(current) < 2.78) {
                databaseHelper.changeDistanceWalked(email, databaseHelper.getDistanceByEmail(email) + mLastLocation.distanceTo(current));
            }

            // For debugging
            System.out.println(mLastLocation.distanceTo(current));
            System.out.println(databaseHelper.getDistanceByEmail(email));

            // Change last location to current
            mLastLocation = current;
}
    }

    /******************************* ON BAG (WORDS) BUTTON CLICK ***********************************/
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.buttonMapToBag:

                // Pause music on background
                if(databaseHelper.getMusicByEmail(email)==1){
                mediaPlayer.pause();}

                // Take user to Bag activity
                Intent intentBag = new Intent(getApplicationContext(), BagActivity.class);
                intentBag.putExtra("EMAIL", getIntent().getStringExtra("EMAIL"));

                // Variables to store title and artist of song
                String titleOfSong = "";
                String artistOfSong = "";

                // Fill variables
                for(Song s :songs_list){
                    if(Integer.valueOf(s.number)==databaseHelper.getSongByEmail(email)){
                        titleOfSong = s.title;
                        artistOfSong = s.artist;
                    }
                }

                // Send to bag activity he title of the current song and the artist
                intentBag.putExtra("TITLEOFSONG",titleOfSong);
                intentBag.putExtra("ARTISTSONG",artistOfSong);
                startActivity(intentBag);
                finish();
                break;
        }
    }

    /***********************  Accessing remote data and XML parsing ********************************/

    // A typical BroadcastReceiver to conserve data use
    public class NetworkReceiver extends BroadcastReceiver {

        // Url for getting Songs
        String urlSongs = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml";

        // Url for getting Place Marks
        String urlPlaceMarks = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+songintforurl+"/map" + Integer.toString(difficulty_int) + ".kml";

        // Url for getting Lyrics for current song played
        String urlLyrics ="http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+songintforurl+"/words.txt";

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
                new DownloadXmlTask().execute(urlSongs, urlPlaceMarks,urlLyrics);
            } else if (networkInfo != null
                    && networkInfo.getType() ==
                    ConnectivityManager.TYPE_MOBILE) {

                // Have a network connection and permission, so use data
                new DownloadXmlTask().execute(urlSongs, urlPlaceMarks,urlLyrics);

            } else {

                // If there is no internet connection
                // Display a toast informing the user
                Toast noInternet = Toast.makeText(activitymap,"No Internet Connection!", Toast.LENGTH_SHORT);
                noInternet.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
                noInternet.show();
            }
        }
    }

    /***************** DOWNLOAD XML TASK ***********************************************************/
    private class DownloadXmlTask extends AsyncTask<String, Void, MyLists> {

        /************ DO IN BACKGROUND *************************************************************/
        @Override
        protected MyLists doInBackground(String... urls) {
            try {

                // Call loadXmlFromNetwork for the three urls initiated
                return loadXmlFromNetwork(urls[0], urls[1], urls[2]);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /************* ON POST EXECUTE *************************************************************/
        @Override
        protected void onPostExecute(MyLists mylists) {

            // If there are no more songs available online, end game
            if (lastSongIndex < databaseHelper.getSongByEmail(email)) {
                Intent theEndIntent = new Intent(getApplicationContext(), TheEndActivity.class);
                theEndIntent.putExtra("EMAIL", email.toString());
                startActivity(theEndIntent);
                finish();

            } else {

                // If there are still songs available to play
                // Put lyrics of current song in lyrics_array
                lyrics_array = mylists.getLyrics();

                // Separate each word from lyrics_array into list
                List<Placemark> list = mylists.getPlaceList();

                // Get already catched words for current song and user into removedMarkers from database
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();

                removedMarkers = gson.fromJson(databaseHelper.getJsonByEmail(email), type);

                // For debugging, Print removedMarkers
                for (String s : removedMarkers) {
                    System.out.println("***" + s.toString()+"***");
                }

                // Put place marks for current song into lista
                // Remove already catched place marks for words from lista
                List<Placemark> lista = new ArrayList<Placemark>(list);

                for (Placemark p : placemark_list) {

                    // For debugging
                    System.out.println(p.name.toString());

                    for (String l : removedMarkers) {
                        if (p.name.toString().equals(l.toString())) {
                            lista.remove(p);
                        }
                    }
                }

                // For each placemark in lista
                for (Placemark p : lista) {

                    // Separate coordinates
                    String[] tokens = p.coordinates.split(",");
                    float[] numbers = new float[tokens.length];
                    for (int i = 0; i < tokens.length; i++) {
                        numbers[i] = Float.parseFloat(tokens[i]);
                    }

                    // Add place mark to map
                    // Change place mark icon according to how it is classified
                    if (p.description.toString().equals("unclassified")) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(numbers[1], numbers[0])).title(p.name)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.aa));
                    } else if (p.description.toString().equals("notboring")) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(numbers[1], numbers[0])).title(p.name)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bb));
                    } else if (p.description.toString().equals("boring")) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(numbers[1], numbers[0])).title(p.name)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.cc));
                    } else if (p.description.toString().equals("interesting")) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(numbers[1], numbers[0])).title(p.name)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.dd));
                    } else if (p.description.toString().equals("veryinteresting")) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(numbers[1], numbers[0])).title(p.name)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ee));
                    }
                }
            }
        }
    }

    /*********** LOAD XML FROM NETWORK *************************************************************/
    // Method loadXmlFromNetwork, returns a MyList Object
    private MyLists loadXmlFromNetwork(String urlString_Songs, String urlString_Placemarks, String urlString_Lyrics) throws
            XmlPullParserException, IOException {

        // InputStream for Songs
        InputStream streamSongs = downloadUrl(urlString_Songs);

        // Parse Songs
        songs_list = songParsing.parseSongs(streamSongs);
        streamSongs.close();

        // InputStream for Place Marks
        InputStream streamPlacemarks = downloadUrl(urlString_Placemarks);

        // Parse PlaceMarks
        placemark_list = placeMarkParsing.parsePlaceMarks(streamPlacemarks);
        streamPlacemarks.close();

        // InputStream for Song Lyrics
        InputStream streamLyrics = downloadUrl(urlString_Lyrics);

        // Parse Lyrics
        txt_string = lyricsParsing.parseLyrics(streamLyrics);

        // From songs downloaded get the current song that the user is playing
        int sizeOfsongfromList = songs_list.size()-1;
        lastSongIndex = Integer.valueOf(songs_list.get(sizeOfsongfromList).number);

        // For debugging
        System.out.println("SIZE OF SONG FROM LIST " +sizeOfsongfromList );
        System.out.println("SONG FROM LIST " +lastSongIndex );
        System.out.println("DATABASE SONG" + databaseHelper.getSongByEmail(email) );
        System.out.println("LYRICS "+ txt_string);

        // Separate lyrics of the current song
        String[] words = txt_string.split("\\s+");
        for (int i =0;i<words.length;i++){
            words[i] = words[i].replaceAll("[^\\w]", "");
        }

        // For debugging
        for (int i =0;i<words.length;i++) {
            System.out.println(words[i]);
        }

        // For debugging
        for (Song p : songs_list) {
            System.out.println(p.number + " " + p.artist + " " + p.title + " " + p.link);
        }

        // For debugging
        System.out.println("SIZE SONGS: " + songs_list.size() + "   SIZE PLACEMARKS: " + placemark_list.size());
        for (Placemark iP : placemark_list) {
            System.out.println(iP.name + " " + iP.description + " " + iP.styleUrl + " " + iP.coordinates);
        }

        // Return a MyList object that includes songs_list, placemark_list and separated words array
        MyLists a = new MyLists(songs_list, placemark_list,words);
        return a;
    }

    /**************************** DOWNLOAD URL *****************************************************/
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

    /********************************** MY LISTS OBJECT ********************************************/
    // My list class that includes two lists and an array
    // The purpose of this object is to be returned by loadXmlFromNetwork and then be used onPostExecute
    public class MyLists {
        private List<Song> s;
        private List<Placemark> p;
        private String[] l;

        // Constructor
        public MyLists(List<Song> s, List<Placemark> p,String[] l) {
            this.s = s;
            this.p = p;
            this.l = l;
        }

        // Getters and Setters
        public List<Song> getSongList() {
            return s;
        }

        public void setSongList(List<Song> s) {
            this.s = s;
        }

        public List<Placemark> getPlaceList() {
            return p;
        }

        public void setPlaceList(List<Placemark> p) {
            this.p = p;
        }

        public String[] getLyrics() {
            return l;
        }

        public void setLyrics(String[] l) {
            this.l = l;
        }
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

    /******************* WORD FOUND DIALOG BOX (ON PLACE MARK CLICK ) ******************************/
    public class ViewDialogWordFound {

        public void showDialog(Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.found_dialog_layout);

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

    /****************** NO WORD FOUND DIALOG BOX (FAR AWAY FROM PLACE MARK ON CLICK) ***************/
    public class ViewDialogNoWordFound {

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

    /*********************** ON PHONE BACK BUTTON PRESS ********************************************/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if(databaseHelper.getMusicByEmail(email)==1){

                // Pause background music
                mediaPlayer.pause();}

                // Take user to Main user activity
                Intent accountsIntent = new Intent(getApplicationContext(), UsersActivity.class);

                // Send current user email to Main user activity for future use
                accountsIntent.putExtra("EMAIL", email.toString());
                startActivity(accountsIntent);
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}