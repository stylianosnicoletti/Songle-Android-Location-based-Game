package com.example.stelios.songle;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PlaceMarkParsing {

    private Placemark placemark;

    private Context context;

    public PlaceMarkParsing(Context context){
        this.context = context;
    }

    /***************************** PARSE PLACE MARKS ***********************************************/
    private static final String nsp = null;

    public List<Placemark> parsePlaceMarks(InputStream in) throws XmlPullParserException,
            IOException {
        try {
            XmlPullParser parser2 = Xml.newPullParser();
            parser2.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,
                    false);
            parser2.setInput(in, null);
            parser2.nextTag();
            return readPlaceMarks(parser2);
        } finally {
            in.close();
        }
    }

    /***************************** READ PLACE MARKS FROM XML ***************************************/
    private List<Placemark> readPlaceMarks(XmlPullParser parser2) throws
            XmlPullParserException, IOException {
        List<Placemark> placemarks = new ArrayList<Placemark>();
        parser2.require(XmlPullParser.START_TAG, nsp, "kml");
        while (parser2.next() != XmlPullParser.END_TAG) {
            if (parser2.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String namee = parser2.getName();

            // Start by looking on Documents Tag
            if (namee.equals("Document")) {

                placemarks = readPlaceMark1(parser2);
            } else {
                skip(parser2);
            }
        }
        return placemarks;
    }

    private List<Placemark> readPlaceMark1(XmlPullParser parser2) throws
            XmlPullParserException, IOException {
        List<Placemark> placemarks2 = new ArrayList<Placemark>();
        parser2.require(XmlPullParser.START_TAG, nsp, "Document");
        while (parser2.next() != XmlPullParser.END_TAG) {
            if (parser2.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String namee = parser2.getName();

            // Start by looking for the Placemark tag
            if (namee.equals("Placemark")) {
                placemarks2.add(readPlaceMark2(parser2));
            } else {
                skip(parser2);
            }
        }
        return placemarks2;
    }

    private Placemark readPlaceMark2(XmlPullParser parser2) throws
            XmlPullParserException, IOException {
        parser2.require(XmlPullParser.START_TAG, nsp, "Placemark");
        String name = null;
        String description = null;
        String styleUrl = null;
        String coordinates = null;
        while (parser2.next() != XmlPullParser.END_TAG) {
            if (parser2.getEventType() != XmlPullParser.START_TAG)
                continue;
            String namee = parser2.getName();
            if (namee.equals("name")) {

                // Place Mark Name
                name = readName(parser2);
            } else if (namee.equals("description")) {

                // Place Mark Description
                description = readDescription(parser2);
            } else if (namee.equals("styleUrl")) {

                // Place Mark Style
                styleUrl = readStyleUrl(parser2);
            } else if (namee.equals("Point")) {

                // Place Mark Coordinates
                coordinates = readCoordinates(parser2);
            } else {
                skip(parser2);
            }
        }

        placemark = new Placemark(name,description,styleUrl,coordinates);
        return  placemark;

    }

    /***************************** READ PLACE MARK NAME FROM XML ***********************************/
    private String readName(XmlPullParser parser2) throws IOException, XmlPullParserException {

        parser2.require(XmlPullParser.START_TAG, nsp, "name");
        String name = readText(parser2);
        parser2.require(XmlPullParser.END_TAG, nsp, "name");
        return name;
    }

    /***************************** READ PLACE MARK DESCRIPTION FROM XML ****************************/
    private String readDescription(XmlPullParser parser2) throws IOException, XmlPullParserException {

        parser2.require(XmlPullParser.START_TAG, nsp, "description");
        String description = readText(parser2);
        parser2.require(XmlPullParser.END_TAG, nsp, "description");
        return description;
    }

    /***************************** READ PLACE MARK STYLE FROM XML **********************************/
    private String readStyleUrl(XmlPullParser parser2) throws IOException, XmlPullParserException {

        parser2.require(XmlPullParser.START_TAG, nsp, "styleUrl");
        String styleUrl = readText(parser2);
        parser2.require(XmlPullParser.END_TAG, nsp, "styleUrl");
        return styleUrl;
    }

    /***************************** READ PLACE MARK COORDINATES FROM XML ****************************/
    private String readCoordinates(XmlPullParser parser2) throws IOException, XmlPullParserException {

        parser2.require(XmlPullParser.START_TAG, nsp, "Point");
        String coordinatesReceived = null;
        while (parser2.next() != XmlPullParser.END_TAG) {
            if (parser2.getEventType() != XmlPullParser.START_TAG)
                continue;
            String namee = parser2.getName();
            if (namee.equals("coordinates")) {
                coordinatesReceived = readText(parser2);
            } else {
                skip(parser2);
            }
        }
        return coordinatesReceived;
    }

    /**************** READING TEXT *****************************************************************/
    private String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /**************** SKIPPING UNINTERESTED TAGS ***************************************************/
    private void skip(XmlPullParser parser) throws XmlPullParserException,
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
