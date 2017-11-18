package com.example.niceday.marketdirectory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MoreOptionsActivity extends AppCompatActivity implements MoreOptionListFragment.OnFragmentInteractionListener, MoreOptionDetailFragment.OnFragmentInteractionListener {

    private TheResponse response;
    MoreOptionDetailFragment moreOptionDetailFragment;
    MoreOptionListFragment moreOptionListFragment;
    private TextView pageTitle;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_options);

        boolean istablet = getResources().getBoolean(R.bool.isTablet);
        if(istablet){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        moreOptionListFragment = (MoreOptionListFragment) getSupportFragmentManager().findFragmentById(R.id.moreOptionList);
        moreOptionDetailFragment  = (MoreOptionDetailFragment) getSupportFragmentManager().findFragmentById(R.id.moreOptionDetail);
        moreOptionListFragment.updateMoreOptionDetailFragment('A','B','C','D');
        response = new TheResponse(this);
        pageTitle = (TextView) findViewById(R.id.moreOptionTitle);
        pageTitle.setTextSize(40);
        pageTitle.setText("Select the State");
    }

    protected void onPause(){
        super.onPause();
        unregisterReceiver(response);
    }

    protected void onResume(){
        super.onResume();
        IntentFilter filter1 = new IntentFilter(TheResponse.STATUS_DONE_3);
        //IntentFilter filter2 = new IntentFilter(TheResponse.STATUS_DONE_2);
        registerReceiver(response,filter1);
        //registerReceiver(response,filter2);
    }

    @Override
    public void onFragmentInteraction(String zipcode) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("zipcode", zipcode);
        setResult(StartActivity.RESULTCODE,resultIntent);
        finish();
    }

    @Override
    public <E> void onFragmentInteraction(ArrayList<E> states, boolean showCity) {
        moreOptionDetailFragment.setupDetailView(states, showCity);
    }



    class TheResponse extends BroadcastReceiver {

        Context c;
        public TheResponse(Context c) {
            this.c = c;
        }
        public static final String STATUS_DONE_3 = "com.example.intentservebroaddemo_v1.ALL_CITIES_DONE";
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(STATUS_DONE_3)) {
                String cityText= intent.getStringExtra("output");
                if(cityText!=null){
                    Log.d("newCity Service", cityText);
                    pageTitle.setText("Select the City:");
                    ArrayList<USCities> citysList = new ArrayList<>();
                    try {
                        JSONObject cities = new JSONObject(cityText);
                        JSONArray cityArr = cities.getJSONArray("result");
                        JSONObject element;
                        USCities city;
                        for (int i = 0; i < cityArr.length(); i++) {
                            element = cityArr.getJSONObject(i);
                            city = new USCities();
                            city.name = element.getString("City");
                            city.postcode = element.getString("Zipcode");
                            citysList.add(city);
                        }
                        moreOptionListFragment.setCities(citysList);
                        moreOptionListFragment.updateMoreOptionDetailFragment('A','B','C','D');
                    }catch (Exception e){
                        Log.d("CITYUPDATE", "Error"+e.getMessage());
                    }



                }

            }
        }

        }



}
