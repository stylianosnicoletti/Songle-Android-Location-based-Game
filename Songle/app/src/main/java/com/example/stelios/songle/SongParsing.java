package com.example.stelios.songle;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class SongParsing {

    private Song song;
    private Context context;
    public static final String ns = null;

    public SongParsing(Context context){
        this.context = context;
    }


    /***************************** PARSE SONGS *****************************************************/
    public  List<Song> parseSongs(InputStream in) throws XmlPullParserException,
            IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,
                    false);
            parser.setInput(in, null);
            parser.nextTag();
            return readSongs(parser);
        } finally {
            in.close();
        }
    }

    /*************************** READ SONGS XML ****************************************************/
    private  List<Song> readSongs(XmlPullParser parser) throws
            XmlPullParserException, IOException {
        List<Song> songs = new ArrayList<Song>();
        parser.require(XmlPullParser.START_TAG, ns, "Songs");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            // Starts by looking for the Song tag
            if (name.equals("Song")) {
                songs.add(readSong(parser));
            } else {
                skip(parser);
            }
        }
        return songs;
    }

    /***************************** READ SONG FROM XML **********************************************/
    private  Song readSong(XmlPullParser parser) throws
            XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Song");
        String number = null;
        String artist = null;
        String title = null;
        String link = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            if (name.equals("Number")) {
                number = readNumber(parser);
                //System.out.println(number);
            } else if (name.equals("Artist")) {
                artist = readArtist(parser);
                //System.out.println(artist);
            } else if (name.equals("Title")) {
                title = readTitle(parser);
                //System.out.println(title);
            } else if (name.equals("Link")) {
                link = readLink(parser);
                //System.out.println(link);

            } else {
                skip(parser);
            }
        }
        song = new Song(number,artist,title,link);
        return song;
    }

    /*********************** READING SONG NUMBER FROM XML ******************************************/
    private  String readNumber(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "Number");
        String number = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Number");
        return number;
    }

    /*********************** READING SONG ARTIST FROM XML ******************************************/
    private  String readArtist(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "Artist");
        String artist = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Artist");
        return artist;
    }

    /*********************** READING SONG TITLE FROM XML *******************************************/
    private  String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "Title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Title");
        return title;
    }

    /*********************** READING SONG LINK FROM XML *******************************************/
    private  String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "Link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Link");
        return link;
    }

    /**************** READING TEXT *****************************************************************/
    private  String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /**************** SKIPPING UNINTERESTED TAGS ***************************************************/
    private  void skip(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
