package com.example.niceday.marketdirectory;

import java.io.Serializable;

/**
 * Created by NiceDay on 2017-11-01.
 */

public class Market implements Serializable{

    int id=0;
    String marketName = "Loading...";
    double distance = 0;
    MarketDetail marketDetail = new MarketDetail();

    public Market(){

    }


    class MarketDetail implements Serializable{
        String address="";
        String googleLink="";
        String products="Loading";
        String schedule="";

    }





}
