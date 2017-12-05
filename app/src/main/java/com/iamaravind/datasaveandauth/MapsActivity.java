package com.iamaravind.datasaveandauth;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, LocationSource.OnLocationChangedListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
   // Firebase mFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Log.e("sdhfgjshdf","ready");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Log.e("sdhfgjshdf","onready");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

    protected void buildGoogleApiClient(){
        //Log.e("sdhfgjshdf","build");
        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)

                .build();



        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Log.e("sdhfgjshdf","changed");

                mLastLocation = location;

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                Log.d("Latitude", " " + location.getLatitude());
                Log.d("Longitude", " " + location.getLongitude());
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("currentLocation");
                // lat 10.0377826 long 76.3292726
                GeoFire geoFire = new GeoFire(ref);
               // geoFire.setLocation(ref.push().getKey(), new GeoLocation(location.getLatitude(), location.getLongitude()));
                geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("sdhfgjshdf","sdkfjs");
       // Firebase.setAndroidContext(this);
        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Log.d("Latitude", " " + location.getLatitude());
        Log.d("Longitude", " " + location.getLongitude());
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("currentLocation");
       /* mFire = new Firebase("https://datasaveandauth.firebaseio.com/CurrentLoc/"+userId);
        Firebase myNewChild = mFire.child("lat");
        myNewChild.setValue(location.getLatitude());
        Firebase myNewChild1 = mFire.child("lon");
        myNewChild1.setValue(location.getLongitude());*/

        GeoFire geoFire = new GeoFire(ref);
        geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("sdhfgjshdf","sdkfjs");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Log.e("sdhfgjshdf","noper");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("sdhfgjshdf","fail;ed");
    }

    @Override
    protected void onStop() {
        super.onStop();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CurrentLocation");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);
    }
}
