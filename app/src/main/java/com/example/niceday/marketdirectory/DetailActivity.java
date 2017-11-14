package com.example.niceday.marketdirectory;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Market selectedMarket = (Market)getIntent().getSerializableExtra("SelectedMarket");
        Log.d("DetailActivity", selectedMarket.marketName);

        boolean istablet = getResources().getBoolean(R.bool.isTablet);
        if(istablet){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        ((TextView)findViewById(R.id.marketNameTxt)).setText(selectedMarket.marketName);
        ((TextView)findViewById(R.id.marketIdTxt)).setText(String.valueOf("Market ID: " + selectedMarket.id));
        ((TextView)findViewById(R.id.marketDistanceTxt)).setText(String.valueOf(" Distance: " + selectedMarket.distance));
        ((TextView)findViewById(R.id.marketAdressTxt)).setText("Address: \n" + selectedMarket.marketDetail.address);
        ((TextView)findViewById(R.id.marketLinkTxt)).setText("The Google Link: \n"+selectedMarket.marketDetail.googleLink);
        ((TextView)findViewById(R.id.marketProductTxt)).setText("Products: \n"+selectedMarket.marketDetail.products);
        ((TextView)findViewById(R.id.marketScheduleTxt)).setText("Open Hours: \n"+selectedMarket.marketDetail.schedule);


    }
}
