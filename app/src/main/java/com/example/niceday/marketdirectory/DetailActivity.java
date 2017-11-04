package com.example.niceday.marketdirectory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Market selectedMarket = (Market)getIntent().getSerializableExtra("SelectedMarket");
        Log.d("DetailActivity", selectedMarket.marketName);


    }
}
