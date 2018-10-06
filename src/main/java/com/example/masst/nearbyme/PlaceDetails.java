package com.example.masst.nearbyme;

import com.google.api.client.util.Key;

import java.io.Serializable;

public class PlaceDetails implements Serializable{
    @Key
    public String status;

    @Key
    public Place result;

    @Override
    public String toString() {
        if (result!=null) {
            return result.toString();
        }
        return super.toString();
    }
//    public String status;
//    public Place result;
//
//    @Override
//    public String toString() {
//        if (result != null){
//            return result.toString();
//        }
//        return super.toString();
//    }
}
