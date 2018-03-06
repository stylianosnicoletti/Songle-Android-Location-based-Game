package com.example.stelios.songle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper  extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    // NAME OF DATABASE
    private static final String DATABASE_NAME = "UserManager15.db";

    // NAME OF TABLE
    private static final String TABLE_USER = "user";

    // COLUMNS IN TABLE
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_DIFFICULTY = "user_difficulty";
    private static final String COLUMN_USER_SCORE = "user_score";
    private static final String COLUMN_USER_SONG = "user_song";
    private static final String COLUMN_USER_JSON = "user_json";
    private static final String COLUMN_USER_DISTANCE = "user_distance";
    private static final String COLUMN_USER_COINS = "user_coins";
    private static final String COLUMN_USER_MUSIC = "user_music";
    private static final String COLUMN_USER_HINT = "user_hint";

    // CREATE TABLE WITH ITS COLUMN AND TYPES
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_USER_DIFFICULTY + " INTEGER,"
            + COLUMN_USER_SCORE + " INTEGER," + COLUMN_USER_SONG +" INTEGER," + COLUMN_USER_JSON + " TEXT,"
            + COLUMN_USER_DISTANCE + " REAL," + COLUMN_USER_COINS + " INTEGER," + COLUMN_USER_MUSIC + " INTEGER,"
            + COLUMN_USER_HINT +" INTEGER"+")";

    // DROP TABLE IF IT EXISTS
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /********* onCreate, onUpgrade *****************************************************************/
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    /*********** ADD USER (RECORD) IN TABLE ********************************************************/
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_DIFFICULTY,user.getDifficulty());
        values.put(COLUMN_USER_SCORE,user.getScore());
        values.put(COLUMN_USER_SONG,user.getSong());
        values.put(COLUMN_USER_JSON,user.getJson());
        values.put(COLUMN_USER_DISTANCE,user.getWalked());
        values.put(COLUMN_USER_COINS,user.getCoins());
        values.put(COLUMN_USER_MUSIC,user.getMusic());
        values.put(COLUMN_USER_HINT,user.getHint());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /*********** UPDATE IS HINT USED FOR CURRENT SONG  *********************************************/
    // Update hint to 1 or 0 (is hint used for current song)
    // Using user's email to identify the record
    public void changeHint(String email,int hint){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_hint",hint);
        db.update(TABLE_USER,
                cv, ""+COLUMN_USER_EMAIL+"= '"+email+"'",null);

    }

    /*********** UPDATE MUSIC ON BACKGROUND ENABLED/DISABLED ***************************************/
    // Update music to 1 or 0 (is music on background enabled)
    // Using user's email to identify the record
    public void changeMusic(String email,int music){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_music",music);
        db.update(TABLE_USER,
                cv, ""+COLUMN_USER_EMAIL+"= '"+email+"'",null);
    }

    /*********** UPDATE USER COINS *****************************************************************/
    // Update user's coins available
    // Using user's email to identify the record
    public void changeCoins(String email,int coins){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_coins",coins);
        db.update(TABLE_USER,
                cv, ""+COLUMN_USER_EMAIL+"= '"+email+"'",null);

    }

    /*********** UPDATE USER DISTANCE WALKED *******************************************************/
    // Update user's distance waked
    // Using user's email to identify the record
    public void changeDistanceWalked(String email,float distance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_distance",distance);
        db.update(TABLE_USER,
                cv, ""+COLUMN_USER_EMAIL+"= '"+email+"'",null);

    }

    /*********** UPDATE USER GAME DIFFICULTY *******************************************************/
    // Update user's current playing difficulty (1 to 5, that is Very Hard to Very Easy)
    // Using user's email to identify the record
    public void changeDifficulty(String email,int diff){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_difficulty",diff);
        db.update(TABLE_USER,
                cv, ""+COLUMN_USER_EMAIL+"= '"+email+"'",null);

    }

    /*********** UPDATE USER WORDS COLLECTED FOR CURRENT SONG **************************************/
    // Update user's words collected (json String that includes the ids of all words collected) for current song
    // Using user's email to identify the record
    public void changeWordsJson(String email,String json){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_json",json);
        db.update(TABLE_USER,
                cv, ""+COLUMN_USER_EMAIL+"= '"+email+"'",null);
    }

    /*********** UPDATE USER CURRENT PLAYING SONG **************************************************/
    // Update user's current song ( eg. 1 for fisrt song and so on..)
    // Using user's email to identify the record
    public void changeSong(String email,Integer song){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_song",song);
        db.update(TABLE_USER,
                cv, ""+COLUMN_USER_EMAIL+"= '"+email+"'",null);
    }

    /*********** UPDATE USER CURRENT SCORE *********************************************************/
    // Update user's current score
    // Using user's email to identify the record
    public void changeScore(String email,Integer score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("user_score",score);
        db.update(TABLE_USER,
                cv, ""+COLUMN_USER_EMAIL+"= '"+email+"'",null);
    }

    /********** GET USER CURRENT STATUS FOR HINT ***************************************************/
    // Get user's current status for hint (if hint used or not)
    // Using user's email to identify the record
    public int getHintByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_hint FROM user WHERE user_email=?", new String[] {email + ""});
        int hint =0;
        try {
            if(cursor.getCount() > 0) {
                cursor.moveToLast();
                hint = cursor.getInt(cursor.getColumnIndex("user_hint"));
            }
            return hint;
        }finally {
            cursor.close();
        }
    }

    /********** GET USER CURRENT STATUS FOR MUSIC IN BACKGROUND ************************************/
    // Get user's current status for music in background (if it is enabled or not)
    // Using user's email to identify the record
    public int getMusicByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_music FROM user WHERE user_email=?", new String[] {email + ""});
        int music =0;
        try {
            if(cursor.getCount() > 0) {
                cursor.moveToLast();
                music = cursor.getInt(cursor.getColumnIndex("user_music"));
            }
            return music;
        }finally {
            cursor.close();
        }
    }

    /********** GET USER COINS *********************************************************************/
    // Get user's current status of his coins available
    // Using user's email to identify the record
    public int getCoinsByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_coins FROM user WHERE user_email=?", new String[] {email + ""});
        int coins =0;
        try {
            if(cursor.getCount() > 0) {
                cursor.moveToLast();
                coins = cursor.getInt(cursor.getColumnIndex("user_coins"));
            }
            return coins;
        }finally {
            cursor.close();
        }
    }

    /********** GET USER CURRENT STATUS OF DIFFICULTY **********************************************/
    // Get user's current status of playing difficulty selected
    // Using user's email to identify the record
    public int getDifficultyByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_difficulty FROM user WHERE user_email=?", new String[] {email + ""});
        int diff =3;
        try {
            if(cursor.getCount() > 0) {
                cursor.moveToLast();
                diff = cursor.getInt(cursor.getColumnIndex("user_difficulty"));
            }
            return diff;
        }finally {
            cursor.close();
        }
    }

    /********** GET USER SCORE *********************************************************************/
    // Get user's score points
    // Using user's email to identify the record
    public int getScoreByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_score FROM user WHERE user_email=?", new String[] {email + ""});
        int score =0;
        try {
            if(cursor.getCount() > 0) {
                cursor.moveToLast();
                score = cursor.getInt(cursor.getColumnIndex("user_score"));
            }
            return score;
        }finally {
            cursor.close();
        }
    }

    /********** GET USER CURRENT DISTANCE WALKED ***************************************************/
    // Get user's current distance walked
    // Using user's email to identify the record
    public float getDistanceByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_distance FROM user WHERE user_email=?", new String[] {email + ""});
        float distance =0;
        try {
            if(cursor.getCount() > 0) {
                cursor.moveToLast();
                distance = cursor.getFloat(cursor.getColumnIndex("user_distance"));
            }
            return distance;
        }finally {
            cursor.close();
        }
    }

    /********** GET USER CURRENT PLAYING SONG ******************************************************/
    // Get user's current playing song number
    // Using user's email to identify the record
    public int getSongByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_song FROM user WHERE user_email=?", new String[] {email + ""});
        int song =0;
        try {
            if(cursor.getCount() > 0) {
                cursor.moveToLast();
                song = cursor.getInt(cursor.getColumnIndex("user_song"));
            }
            return song;
        }finally {
            cursor.close();
        }
    }

    /********** GET USER REGISTERED NAME ***********************************************************/
    // Get user's Name
    // Using user's email to identify the record
    public String getNameByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_name FROM user WHERE user_email=?", new String[] {email + ""});
        String name = "";
        try {
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                name = cursor.getString(cursor.getColumnIndex("user_name"));
            }
            return name;
        }finally {
            cursor.close();
        }
    }

    /*********** GET USER CURRENT WORDS COLLECTED **************************************************/
    // Get user's word ids collected for current playing song
    // Using user's email to identify the record
    public String getJsonByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_json FROM user WHERE user_email=?", new String[] {email + ""});
        String json = "";
        try {
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                json = cursor.getString(cursor.getColumnIndex("user_json"));
            }
            return json;
        }finally {
            cursor.close();
        }
    }

    /************************* CHECK IF USER ALREADY EXISTS ****************************************/
    // Return true if the user exists, false instead
    public boolean checkUser(String email){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    /*************** CHECK IF A USER WITH THE PASSWORD EXISTS **************************************/
    // Return true if the user exists with that password, false instead
    public boolean checkUser(String email, String password){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

}
