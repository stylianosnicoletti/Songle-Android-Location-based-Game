package com.example.stelios.songle;

import android.content.Context;

/******************************** SONG CLASS **************************************************/
public class Song {
    public String number;
    public String artist;
    public String title;
    public String link;

    private Context context;
    public Song(Context context){
        this.context = context;
    }

    public Song(String number, String artist, String title, String link) {
        this.number = number;
        this.artist = artist;
        this.title = title;
        this.link = link;
    }

}