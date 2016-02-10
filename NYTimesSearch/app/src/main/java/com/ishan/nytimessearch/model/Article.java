package com.ishan.nytimessearch.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Article implements Serializable{
    private String webURL;
    private String headLine;
    private String thumbNail;

    public String getWebURL() {
        return webURL;
    }

    public String getHeadLine() {
        return headLine;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public Article(JSONObject jsonObject){
        try{
            this.webURL = jsonObject.getString("web_url");
            this.headLine = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if(multimedia.length() > 0){
                JSONObject multimediaJSON = multimedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/"+multimediaJSON.getString("url");
            }
            else
                this.thumbNail = "";

        }catch(JSONException e){

        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array){
        ArrayList<Article> results = new ArrayList<>();

        for(int i = 0; i < array.length(); i++){
            try{
                results.add(new Article(array.getJSONObject(i)));
            }catch(JSONException e){

            }
        }
        return results;
    }

}
