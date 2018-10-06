package com.example.masst.nearbyme;

import com.google.api.client.util.Key;

import java.io.Serializable;

public class Place implements Serializable {
    @Key
    public String id;

    @Key
    public String name;

    @Key
    public String reference;

    @Key
    public String icon;

    @Key
    public String vicinity;

    @Key
    public Geometry geometry;

    @Key
    public String formatted_address;

    @Key
    public String formatted_phone_number;

    @Override
    public String toString() {
        return name + " - " + id + " - " + reference;
    }

    public static class Geometry implements Serializable
    {
        @Key
        public Location location;
    }

    public static class Location implements Serializable
    {
        @Key
        public double lat;

        @Key
        public double lng;
    }
}
//    public String id;
//
//    public String name;
//
//    public String reference;
//
//    public String icon;
//
//    public String vicinity;
//
//    public Geometry geometry;
//
//    public String formatted_address;
//
//    public String formatted_phone_number;
//
//    @Override
//    public String toString() {
//        return name + " - " + id + " - " + reference;
//    }
//
//    public static class Geometry implements Serializable
//    {
//        public Location location;
//    }
//
//    public static class Location implements Serializable
//    {
//        public double lat;
//
//        public double lng;
//    }
