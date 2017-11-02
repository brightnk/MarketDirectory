package com.example.niceday.marketdirectory;

/**
 * Created by NiceDay on 2017-11-01.
 */

public class Market {

    int id;
    String marketName;
    double distance;
    MarketDetail marketDetail = new MarketDetail();

    public Market(){

    }


    class MarketDetail{
        String address;
        String googleLink;
        String products;
        String schedule;

    }





}
