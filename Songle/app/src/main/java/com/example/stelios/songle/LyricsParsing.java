package com.example.stelios.songle;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LyricsParsing {

    private Context context;

    public LyricsParsing(Context context){
        this.context = context;
    }

    /**************** PARSE LYRICS *****************************************************************/
    public String parseLyrics(InputStream is) throws IOException {

        // Initialise a buffer reader
        BufferedReader br = null;

        // Initialise a string builder
        StringBuilder sb = new StringBuilder();

        // Initialise a String
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
