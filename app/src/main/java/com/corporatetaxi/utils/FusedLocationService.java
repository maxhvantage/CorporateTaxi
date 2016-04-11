package com.corporatetaxi.utils;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class FusedLocationService implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final long INTERVAL = 0;//10 * 1000;
    private static final long FASTEST_INTERVAL = 0;//1 * 1000;
    Context locationActivity;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Location location;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

    public FusedLocationService(Context context) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        this.locationActivity = context;

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        System.out.println("google api client is on fused location provider is --$$$$$$$$--------------- " + googleApiClient);

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location currentLocation;

        currentLocation = fusedLocationProviderApi
                .getLastLocation(googleApiClient);

        if (currentLocation != null) {
            if (currentLocation.getLatitude() != 0.0d && currentLocation.getLongitude() != 0.0d) {
                System.out.println("location service >> latitude is : " + currentLocation.getLatitude() + " longitude : " + currentLocation.getLongitude());
                AppPreferences.setLatitude(locationActivity.getApplicationContext(),
                        "" + currentLocation.getLatitude());
                AppPreferences.setLongitude(
                        locationActivity.getApplicationContext(), "" + currentLocation.getLongitude());
            }
        }
        fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    public Location getLocation() {
        return this.location;
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("-----------------------	fused location provider onconnection failed listener ------------------------------------");
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        int suitableMeter = 20;// && location.getAccuracy() <= suitableMeter
        if (location.getLatitude() != 0.0d && location.getLongitude() != 0.0d) {
            System.out.println("-------- fused provider on location changed of taxi called -------- lat : " + location.getLatitude() + " long : " + location.getLongitude() + " accuracy : " + location.getAccuracy());
            AppPreferences.setLatitude(locationActivity.getApplicationContext(), ""
                    + location.getLatitude());
            AppPreferences.setLongitude(locationActivity.getApplicationContext(), ""
                    + location.getLongitude());
        }
    }
}