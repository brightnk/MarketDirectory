package com.example.niceday.marketdirectory;

/**
 * Created by NiceDay on 2017-11-01.
 */

public class Market {

    int id=0;
    String marketName = "Loading...";
    double distance = 0;
    MarketDetail marketDetail = new MarketDetail();

    public Market(){

    }


    class MarketDetail{
        String address="";
        String googleLink="";
        String products="Loading";
        String schedule="";

    }





}
