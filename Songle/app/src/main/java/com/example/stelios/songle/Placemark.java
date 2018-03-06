package com.example.stelios.songle;


import android.content.Context;

public class Placemark {

    /****************************** PLACE MARK CLASS ***********************************************/
    //Class for storing Song entries
        public  String name;
        public  String description;
        public  String styleUrl;
        public  String coordinates;

    private Context context;
    public Placemark(Context context){
        this.context = context;
    }

        // Constructor
        public Placemark(String name, String description, String styleUrl, String coordinates) {
            this.name = name;
            this.description = description;
            this.styleUrl = styleUrl;
            this.coordinates = coordinates;
        }
    }
