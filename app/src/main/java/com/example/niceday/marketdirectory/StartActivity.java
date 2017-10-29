package com.example.niceday.marketdirectory;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.R.id.text1;

public class StartActivity extends AppCompatActivity implements MarketListFragment.OnFragmentInteractionListener{

    private static final int REQUEST_PERMISSION_LOCATION = 1001;
    TextView text1;
    LocationManager mLocationManager;
    String receivePostCode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if(!checkPermission()){
            return;
        }

        text1 = (TextView) findViewById(R.id.txt1);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,
                10, mLocationListener);

        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        getZipCode(location);
        text1.setText(receivePostCode);
    }



    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            getZipCode(location);
            String msg = "New Latitude: " + latitude +"  New Longitude: " + longitude + "zipcode : "+ receivePostCode;


            text1.setText(msg);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    //check if have user's permission of reading location
    private boolean checkPermission(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //if not, request for the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSION_LOCATION);
            return false;
        }

        return true;
    }

    @Override
    //handle permission result
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Locatioin permission granted",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                } else {
                    Toast.makeText(this, "You must grant permission!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }


    public void getZipCode(Location location){
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Log.d("LOCATION", String.valueOf(longitude) + "   "+String.valueOf(latitude));

        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> address = null;

        if (geoCoder != null) {
            try {
                address = geoCoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if (address.size() > 0) {
                receivePostCode = address.get(0).getPostalCode();

            }else{
                receivePostCode = "The Location hasn't any postal code";
            }

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        
    }
}


