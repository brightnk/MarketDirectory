package com.example.niceday.marketdirectory;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class MoreOptionsActivity extends AppCompatActivity implements MoreOptionListFragment.OnFragmentInteractionListener, MoreOptionDetailFragment.OnFragmentInteractionListener {

    @Override
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

        MoreOptionListFragment moreOptionListFragment = (MoreOptionListFragment) getSupportFragmentManager().findFragmentById(R.id.moreOptionList);
        MoreOptionDetailFragment moreOptionDetailFragment  = (MoreOptionDetailFragment) getSupportFragmentManager().findFragmentById(R.id.moreOptionDetail);


    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
