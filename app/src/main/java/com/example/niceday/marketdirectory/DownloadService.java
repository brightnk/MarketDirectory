package com.example.niceday.marketdirectory;

/**
 * Created by NiceDay on 2017-10-23.
 */

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by NiceDay on 2017-09-30.
 */

public class DownloadService extends IntentService{

    public DownloadService() { super (DownloadService.class.getName());


    }





    protected void onHandleIntent(Intent intent){

        String searchLink = "";
        String finishFlag = "";
        String results="";
        switch(intent.getStringExtra("SERVICETYPE")){

            case "byZipCode": searchLink = "https://search.ams.usda.gov/farmersmarkets/v1/data.svc/zipSearch?zip=" + intent.getStringExtra("Zipcode");
                              finishFlag = StartActivity.TheResponse.STATUS_DONE_1;
                              results = getRemoteData(searchLink);
                break;

            case "byMarketIDs": results="[";
                                for(String id: intent.getStringArrayListExtra("IDS"))
                            {
                                searchLink = "https://search.ams.usda.gov/farmersmarkets/v1/data.svc/mktDetail?id="+id;
                                String temp = getRemoteData(searchLink);
                                if(temp!= null) results += getRemoteData(searchLink) +",";
                                else results += "{marketdetails:{Address: 'n/a', GoogleLink: 'n/a', Products:'', Schedule:'n/a'}}, ";
                            }
                            results = results.substring(0, results.length()-1)+"]";
                            finishFlag =StartActivity.TheResponse.STATUS_DONE_2;
                break;

        }



        Intent broadcast = new Intent();
        broadcast.setAction(finishFlag);
        broadcast.putExtra("output", results);
        sendBroadcast(broadcast);
        stopSelf();

    }


    private String getRemoteData(String site) {

        HttpURLConnection c = null;
        try {
            URL u = new URL(site);
            c = (HttpURLConnection) u.openConnection();
            c.connect();
            int status = c.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+ '\n');
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (Exception ex) {
            return ex.toString();
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    //disconnect error
                }
            }
        }
        return null;

    }




}
