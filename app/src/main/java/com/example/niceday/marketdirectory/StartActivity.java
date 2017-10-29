package com.example.niceday.marketdirectory;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;

public class StartActivity extends AppCompatActivity implements MarketListFragment.OnFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int REQUEST_PERMISSION_LOCATION = 1001;
    GoogleMap mMap;
    private GoogleApiClient mLocationClient;
    private Marker marker;
    private Geocoder geocoder;
    private LocationManager mLocationManager;
    private String currentPostCode =null;

    //for testing purpose only
    TextView text1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        geocoder = new Geocoder(this);
        text1 = (TextView) findViewById(R.id.txt1);

        if(!checkPermission()){
            text1.setText("Please grant permission first");

        }else {

            //set location listener
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,
                    10, mLocationListener);
            //get last updated location
            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location == null) location=mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            //get current location's zipcode
            else currentPostCode = getZipCode(location);
            text1.setText(currentPostCode);
        }


    }



    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            currentPostCode = getZipCode(location);
            String msg = "New Latitude: " + latitude +"  New Longitude: " + longitude + "zipcode : "+ currentPostCode;


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


    public String getZipCode(Location location){
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Log.d("LOCATION", String.valueOf(longitude) + "   "+String.valueOf(latitude));
        List<Address> address = null;

        if (geocoder != null) {
            try {
                address = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if (address.size() > 0) {
               return address.get(0).getPostalCode();

            }
        }
        return null;
    }


    public boolean servicesOK() {
        /*
        Reference: https://developers.google.com/android/reference/com/google/android/gms/common/GoogleApiAvailability
        */
        GoogleApiAvailability gApiAvail = GoogleApiAvailability.getInstance();

        int isAvailable = gApiAvail.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (gApiAvail.isUserResolvableError(isAvailable)) {
            //Activity, error code, request code
            Dialog dialog =
                    gApiAvail.getErrorDialog(this, isAvailable, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to mapping service", Toast.LENGTH_SHORT).show();
        }

        return false;
    }








    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLocationChanged(Location location) {

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


