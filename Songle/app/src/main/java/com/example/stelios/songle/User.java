package com.example.stelios.songle;

public class User {

    // STORE USER ID
    private int id;

    // STORE USER NAME
    private String name;

    // STORE USER EMAIL
    private String email;

    // STORE USER PASSWORD
    private String password;

    // STORE USER CURRENT PLAYING DIFFICULTY
    private int difficulty;

    // STORE USER CURRENT PLAYING SONG
    private int song;

    // STORE USER CURRENT SCORE
    private int score;

    // STORE USER COLLECTED WORDS FOR CURRENT SONG
    private String json;

    // STORE USER TOTAL DISTANCE WALKED
    private float distancewalked;

    // STORE USER COINS AVAILABLE
    private int coins;

    // STORE USER MUSIC IN BACKGROUND (ENABLED/DISABLED)
    private int music;

    // STORE USER HINT USAGE (BOUGHT/ NOT BOUGHT HINT FOR CURRENT SONG)
    private int hint;

    /*************************** Getters and Setters ***********************************************/
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
       return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public int getDifficulty(){return difficulty;}

    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }

    public int getSong(){return song;}

    public void setSong(int song){
        this.song = song;
    }

    public int getScore(){return score;}

    public void setScore(int score){
        this.score = score;
    }

    public String getJson(){return json;}

    public void setJson(String json){this.json = json;}

    public float getWalked(){return distancewalked;}

    public void setWalked(float distancewalked){this.distancewalked = distancewalked;}

    public int getCoins(){return coins;}

    public void setCoins(int coins){
        this.coins = coins;
    }

    public int getMusic(){return music;}

    public void setMusic(int music){
        this.music = music;
    }

    public int getHint(){return hint;}

    public void setHint(int music){
        this.hint = hint;
    }

}


