package com.example.niceday.marketdirectory;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity implements MarketFragment.OnListFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int REQUEST_PERMISSION_LOCATION = 1001;
    GoogleMap mMap;
    private GoogleApiClient mLocationClient;

    private Geocoder geocoder;
    private LocationManager mLocationManager;
    private String currentPostCode =null;
    private Location currentLocation;
    private TheResponse response;
    SupportMapFragment mapFragment;
    MarketFragment listFragment;
    private int spinnerSelectedIndex=0;
    EditText searchText;
    Location location;
    //control user search and move map view to searched Market
    boolean moveMapToMarket = false;
    //for testing purpose only
    private Marker userPostionMarker;
    private ArrayList<Marker> markers = new ArrayList<>();
    ArrayList<Market> marketArrayList= new ArrayList<>();
    ArrayList<Market> marketArrayListAfterSearch = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        geocoder = new Geocoder(this);
        mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.marketMap);
        listFragment = (MarketFragment) getSupportFragmentManager().findFragmentById(R.id.marketList);
        response = new TheResponse(this);
        Spinner searchSpinner = (Spinner) findViewById(R.id.searchSpinner);
        String[] spinnerItems = new String[]{"by Zipcode","by Product", "by Name","by City"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        searchSpinner.setAdapter(spinnerAdapter);
        searchSpinner.setSelection(0);
        searchText = (EditText) findViewById(R.id.searchTxt);



        if(!checkPermission()){
            //text1.setText("Please grant permission first");

        }else {

            //set location listener
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0, this);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,
                    10, mLocationListener);
            //get last updated location
            location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location == null) {
                location=mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            //get current location's zipcode

            if(location==null){
                Log.d("Location", "can not get location");
            }else{

                currentLocation = location;
                currentPostCode = getZipCode(location);
                if(servicesOK())initMap();
                searchByZipCode(currentPostCode);




                //text1.setText(currentPostCode);

                }

            //check device type
            boolean istablet = getResources().getBoolean(R.bool.isTablet);
            if(istablet){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
            }



        }

        //Search spinner listener
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    protected void onPause(){
        super.onPause();
        unregisterReceiver(response);
    }


    protected void onResume(){
        super.onResume();
        IntentFilter filter1 = new IntentFilter(TheResponse.STATUS_DONE_1);
        IntentFilter filter2 = new IntentFilter(TheResponse.STATUS_DONE_2);
        registerReceiver(response,filter1);
        registerReceiver(response,filter2);
    }

    @Override
    public void onListFragmentInteraction(Market item) {

        Log.d("marketClicked", item.marketName);
        startDetailActivity(item);



    }

    //search button listener
    public void userSearch(View view) {
        String searchInput = searchText.getText().toString().trim();
        searchText.setText("");
        if(searchInput.length()>0&&marketArrayList.size()>0){
            switch (spinnerSelectedIndex){
                case 0:
                    Log.d("searchClicked", "by Zipcode");
                    searchByZipCode(searchInput);
                    moveMapToMarket=true;
                    break;
                case 1:
                    Log.d("searchClicked", "by Product");
                    marketArrayListAfterSearch = searchByProduct(searchInput, marketArrayList);
                    listFragment.setDataView(marketArrayListAfterSearch);
                    createMapMarker(marketArrayListAfterSearch);
                    break;
                case 2:
                    Log.d("searchClicked", "by Name");
                    marketArrayListAfterSearch = searchByName(searchInput, marketArrayList);
                    listFragment.setDataView(marketArrayListAfterSearch);
                    createMapMarker(marketArrayListAfterSearch);
                    break;
                case 3:
                    Log.d("searchClicked", "by City");
                    break;

            }
        }



    }


    //Response class to receive broadcast
    class TheResponse extends BroadcastReceiver {

        Context c;
        public TheResponse(Context c) {
            this.c = c;
        }
        public static final String STATUS_DONE_1 = "com.example.intentservebroaddemo_v1.ALL_DONE";
        public static final String STATUS_DONE_2 = "com.example.intentservebroaddemo_v1.ALL_Detail_DONE";
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(STATUS_DONE_1)) {
                String marketText= intent.getStringExtra("output");
                if(marketText != null) {
                    Log.d("newZipCodeStart Service", marketText);
                    marketArrayList = new ArrayList<>();
                    ArrayList<String> ids = new ArrayList<>();
                    try {
                        JSONObject markets = new JSONObject(marketText);
                        JSONArray marketAr = markets.getJSONArray("results");
                        JSONObject element;

                        String tempName;
                        Market mt;
                        for (int i = 0; i < marketAr.length(); i++) {
                            element = marketAr.getJSONObject(i);
                            mt = new Market();
                            mt.id = element.getInt("id");
                            tempName = element.getString("marketname").trim();
                            String[] tempNames = tempName.split(" ");
                            mt.distance = Double.parseDouble(tempNames[0]);
                            mt.marketName = tempName.substring(tempNames[0].length() + 1);
                            marketArrayList.add(mt);
                            ids.add(element.getString("id"));
                        }

                    } catch (Exception e) {
                        Log.d("MainActivity", e.getMessage());
                    }


                    listFragment.setDataView(marketArrayList);
                    Intent detailDownloadService = new Intent(StartActivity.this, DownloadService.class);
                    detailDownloadService.putStringArrayListExtra("IDS", ids);
                    detailDownloadService.putExtra("SERVICETYPE", "byMarketIDs");
                    startService(detailDownloadService);
                }else{
                    Toast.makeText(StartActivity.this, "The Postal Code is not Valid, please double check", Toast.LENGTH_LONG).show();
                    moveMapToMarket=false;

                }
            }
            else if(intent.getAction().equals(STATUS_DONE_2)){
                String marketDetail = intent.getStringExtra("output");

                try{
                    JSONArray marketDetailsArray  = new JSONArray(marketDetail);
                    JSONObject marketDetailobj;
                    Market tempMkt;
                    for(int i=0; i<marketDetailsArray.length();i++){
                        tempMkt = marketArrayList.get(i);
                        marketDetailobj =  marketDetailsArray.getJSONObject(i).getJSONObject("marketdetails");
                        tempMkt.marketDetail.address = marketDetailobj.getString("Address");
                        tempMkt.marketDetail.googleLink = marketDetailobj.getString("GoogleLink");
                        tempMkt.marketDetail.products = marketDetailobj.getString("Products");
                        tempMkt.marketDetail.schedule = marketDetailobj.getString("Schedule");
                    }

                }catch (Exception e){
                    Log.d("MainActivity", e.getMessage());
                }

                listFragment.setDataView(marketArrayList);

                if(moveMapToMarket){
                    LatLng searchedLatlng = getLatlng(marketArrayList.get(0).marketDetail.googleLink);
                   gotoLocation(searchedLatlng.latitude,searchedLatlng.longitude,10,null,true);
                }
                createMapMarker(marketArrayList);

            }
        }

    }


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

    public void searchByZipCode(String postCode){
        //start download serivce by zipcode
        if(postCode!=null){
        Intent downloadService = new Intent(StartActivity.this, DownloadService.class);
        downloadService.putExtra("SERVICETYPE", "byZipCode");
        downloadService.putExtra("Zipcode", postCode);
        startService(downloadService);
        }else{
            Toast.makeText(this, "No valid zipcode @ this point", Toast.LENGTH_SHORT).show();

        }
    }


    public String getZipCode(double longitude, double latitude){
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

    public String getZipCode(Location location){
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        return getZipCode(longitude,latitude);
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


    private void initMap() {
        if (mMap == null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    Toast.makeText(StartActivity.this, "Ready to map!", Toast.LENGTH_SHORT).show();
                    // initial setup
                    mMap = googleMap;
                    // reference: https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    //googleMap.setTrafficEnabled(true);
                    //googleMap.setIndoorEnabled(true);
                    googleMap.setBuildingsEnabled(true);

                    // reference: https://developers.google.com/android/reference/com/google/android/gms/maps/UiSettings.html#setZoomControlsEnabled(boolean)
                    // buttons on the lower right
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    gotoLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 10,null,true);

                    mLocationClient = new GoogleApiClient.Builder(StartActivity.this)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(StartActivity.this)
                            .addOnConnectionFailedListener(StartActivity.this)
                            .build();

                    mLocationClient.connect();


                    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            Toast.makeText(StartActivity.this,"Tap",Toast.LENGTH_SHORT).show();
                            searchByZipCode(getZipCode(latLng.longitude,latLng.latitude));
                            gotoLocation(latLng.latitude,latLng.longitude,10,null,false);

                        }
                    });

                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {}

                        @Override
                        public void onMarkerDrag(Marker marker) {}

                        @Override
                        public void onMarkerDragEnd(Marker marker) {
                            LatLng latLng = marker.getPosition();
                            searchByZipCode(getZipCode(latLng.longitude,latLng.latitude));
                            gotoLocation(latLng.latitude,latLng.longitude,10,null,false);
                        }
                    });

                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            String marketName = marker.getTitle();
                            for(Market market: marketArrayList){
                                if(marketName.equals(market.marketName)){
                                    Log.d("marketClicked", market.marketName);
                                    startDetailActivity(market);
                                }
                            }
                        }
                    });




                }
            });
        }

    }

    private void gotoLocation(double lat, double lng, float zoom, List<Address> list, boolean moveCamera) {
        LatLng latLng = new LatLng(lat, lng);

        if(markers.size()>0){
            for(Marker marker : markers) marker.remove();
            markers.clear();
        }


        // getting extra info (option)
        try {
            if (list == null)
                list = geocoder.getFromLocation(lat, lng, 1) ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // There are other information you could retrieve from Address
        String street = list.get(0).getAddressLine(0);
        String locality = list.get(0).getLocality();
        String country = list.get(0).getCountryName();

        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .snippet(street + "\n" + country)
                .draggable(true)
                .position(new LatLng(lat, lng));

        userPostionMarker = mMap.addMarker(options);
        markers.add(userPostionMarker);
        if (moveCamera) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            mMap.moveCamera(update);
        }
    }


    private void createMapMarker(ArrayList<Market> marketList){

        if(markers.size()>0){
            for(Marker marker : markers) {
                if (marker != userPostionMarker)
                    marker.remove();
            }
            markers.clear();
            markers.add(userPostionMarker);
        }


        String googleLink="";
        for(Market market: marketList){
            googleLink = market.marketDetail.googleLink;

            Marker marker = mMap.addMarker(new MarkerOptions().title(market.marketName)
                    .snippet(market.marketDetail.address)
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                    .position(getLatlng(googleLink))
                );
            markers.add(marker);

        }
    }

    private LatLng getLatlng(String googleLink){
        double lat, lng;

        String[] splitLink = googleLink.split("%2C%20");
        String[] splitLink2 = splitLink[1].split("%20");

        lat= Double.parseDouble(splitLink[0].substring(26));
        lng= Double.parseDouble(splitLink2[0]);

        return new LatLng(lat, lng);
    }

    private ArrayList<Market> searchByProduct(String keyword, ArrayList<Market> originalList){
        ArrayList<Market> searchResult = new ArrayList<>();

        for(Market market: originalList){
            if(market.marketDetail.products.contains(keyword)) searchResult.add(market);

        }

        return searchResult;
    }

    private ArrayList<Market> searchByName(String keyword, ArrayList<Market> originalList){
        ArrayList<Market> searchResult = new ArrayList<>();

        for(Market market: originalList){
            if(market.marketName.contains(keyword)) searchResult.add(market);

        }

        return searchResult;
    }


    private void startDetailActivity(Market market){

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

    //location change listener handler
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            currentLocation = location;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            currentPostCode = getZipCode(location);
            String msg = "New Latitude: " + latitude +"  New Longitude: " + longitude + "zipcode : "+ currentPostCode;


            //text1.setText(msg);
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




}


