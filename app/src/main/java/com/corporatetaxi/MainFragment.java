package com.corporatetaxi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.corporatetaxi.beans.Allbeans;
import com.corporatetaxi.classes.GPSTracker;
import com.corporatetaxi.utils.AppConstants;
import com.corporatetaxi.utils.AppPreferences;
import com.corporatetaxi.utils.ConnectionDetector;
import com.corporatetaxi.utils.FusedLocationService;
import com.corporatetaxi.utils.JSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainFragment extends Fragment implements LocationListener {
    public static GoogleMap map;
    LatLng mylocation;
    ArrayList<Allbeans> al;
    double lat, lon;
    private static LatLng fromPosition = null;
    private static LatLng toPosition = null;
    LocationManager locationManager;
    Location location;
    boolean isGPSEnabled, isNetworkEnabled, canGetLocation;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    String fulladdress, latitute, langitude, PRIVIOUSTRIPID, address, city, state, country, postalCode, PRIVIOUSSOURCEADDRESS, PRIVIOUSDESTINATIONADDRESS;
    double PRIVIOUSSOURCELATITUDE, PRIVIOUSSOURCELONGITUDE;
    String PRIVIOUSDESTINATIONLATITUDE, PRIVIOUSDESTINATIONLONGITUDE;
    int REFRESHAMOUNT;
    LatLng loc2;
    FusedLocationService fusedLocationService;
    AutoCompleteTextView sourceedit;
    Boolean isInternetPresent = false;
    Handler handler = new Handler();
    ProgressBar addressDialogProgressbar;

    int mydummymarker = 0;
    Marker draggableMarker = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        fusedLocationService = new FusedLocationService(getActivity());
        al = new ArrayList<Allbeans>();
        Button mbtnrequest = (Button) view.findViewById(R.id.btn_request);
        sourceedit = (AutoCompleteTextView) view.findViewById(R.id.show_address);

        map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        getLocation();
        LatLng loc = new LatLng(lat, lon);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                "Montserrat-Regular.ttf");
        mbtnrequest.setTypeface(tf);
        sourceedit.setTypeface(tf);
        mbtnrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (langitude == null && langitude == null || latitute.equalsIgnoreCase("0.0") && langitude.equalsIgnoreCase("0.0")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.service_not_available), Toast.LENGTH_LONG).show();
                } else {
                    LatLng draggablePos = draggableMarker.getPosition();
                    System.out.println(" ***************** sorcelatitude : "+latitute+"  sourcelongitude :  "+langitude+" draggable lat : "+draggablePos.latitude+"  longi : "+draggablePos.longitude);
                    Intent requestintent = new Intent(getActivity(), DestinationSelection_Activity.class);
                    requestintent.putExtra("city", fulladdress);
                    requestintent.putExtra("sorcelatitude", String.valueOf(draggablePos.latitude));
                    Log.d("lattlong",String.valueOf(draggablePos.latitude));
                    requestintent.putExtra("sourcelongitude", String.valueOf(draggablePos.longitude));
                    Log.d("lattlong",String.valueOf(draggablePos.longitude));
                    requestintent.putExtra("sourcezipcode", postalCode);
                    System.out.println("sourcezipcode--getting----" + postalCode);
                    requestintent.putExtra("sourcecountry", country);
                    requestintent.putExtra("sourcestate", state);
                    requestintent.putExtra("sourcecity", city);

                    startActivity(requestintent);
                }

            }
        });

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                map.getCameraPosition();
                cameraPosition = map.getCameraPosition();
                Log.d("postionnnnn", "" + cameraPosition.target);
                LatLng latLng = cameraPosition.target;
                Log.d("postionnnnn", "" + latLng.longitude + " : " + latLng.latitude);

                if(draggableMarker != null)
                {
                    draggableMarker.setPosition(cameraPosition.target);
                }
                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());

                try {

                    addresses = geocoder.getFromLocation(cameraPosition.target.latitude, cameraPosition.target.longitude, 1);
                    String postalCodee = "";
//                    latitute = String.valueOf(addresses.get(0).getLatitude());
//                    langitude = String.valueOf(addresses.get(0).getLongitude());
                    try {

                        latitute = String.valueOf(addresses.get(0).getLatitude());
                        langitude = String.valueOf(addresses.get(0).getLongitude());


                        if (addresses.get(0).getAddressLine(3) != null) {
                            postalCodee = "," + addresses.get(0).getAddressLine(3);
                        }
                        fulladdress = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getAddressLine(1) + ","
                                + addresses.get(0).getAddressLine(2) + postalCodee;
                        sourceedit.setText(fulladdress);
                        Log.d("fulladdresss", fulladdress);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();

                        latitute = "0.0";
                        langitude = "0.0";
                        LatLng latLng1 = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);

                        if(ConnectionDetector.isConnectingToInternet(getActivity().getApplicationContext()))
                        {
                            new fetchLatLongFromService(latLng1).execute();
                        }else
                        {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG).show();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    LatLng latLng1 = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
                    if(ConnectionDetector.isConnectingToInternet(getActivity().getApplicationContext()))
                    {
                        new fetchLatLongFromService(latLng1).execute();
                    }else
                    {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG).show();
                    }
                }
                if(mydummymarker == 0)
                {
                    draggableMarker = map.addMarker(new MarkerOptions().position(cameraPosition.target).draggable(true));
                    draggableMarker.setVisible(false);
                    mydummymarker = 1;
                }
                sourceedit.setText("");
                addressDialogProgressbar.setVisibility(View.VISIBLE);
                sourceedit.setVisibility(View.GONE);
                handler.postDelayed(runnable, 3000);

            }
        });

        //  getActivity().startService(new Intent(getActivity(),DriverService.class));

        Allbeans allbeans = new Allbeans();
        allbeans.setRefreshamount(REFRESHAMOUNT);
        new RefreshAmount(allbeans).execute();
        addressDialogProgressbar = (ProgressBar) view.findViewById(R.id.download_image_progressbar);
        return view;
    }
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sourceedit.setText(fulladdress);
//            progressDialog.dismiss();
            sourceedit.setVisibility(View.VISIBLE);
            addressDialogProgressbar.setVisibility(View.GONE);
            //handler.postDelayed(runnable, 6000);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        final GPSTracker gps = new GPSTracker(getActivity());
        gps.getLocation();
        final Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    if (gps.getLatitude() == 0.0 && gps.getLongitude() == 0.0) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                showSettingsAlert();
                            }
                        });

                    }
                }
            }

        };
        timerThread.start();
    }

    public Location getLocation() {
        try {
            map.setMyLocationEnabled(true);
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        lat = location.getLatitude();
        System.out.println("location latitude:-------" + location.getLatitude());
        lon = location.getLongitude();
        System.out.println("location longitude:-------" + location.getLongitude());


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }


    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getResources().getString(R.string.gps_settingg));
        alertDialog.setMessage(getResources().getString(R.string.gps_alert));

        alertDialog.setPositiveButton(getResources().getString(R.string.gps_setting), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.prompt_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    private class RefreshAmount extends AsyncTask<Void, Void, String> {

        Allbeans allbeans;
        private JSONObject jsonObj;
        ArrayList<Integer> catogariesid;
        private int status = 0;
        ProgressDialog mProgressDialog;

        public RefreshAmount(Allbeans allbeans) {
            this.allbeans = allbeans;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpParams httpParameters = new BasicHttpParams();
                ConnManagerParams.setTimeout(httpParameters,
                        AppConstants.NETWORK_TIMEOUT_CONSTANT);
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        AppConstants.NETWORK_CONNECTION_TIMEOUT_CONSTANT);
                HttpConnectionParams.setSoTimeout(httpParameters,
                        AppConstants.NETWORK_SOCKET_TIMEOUT_CONSTANT);

                HttpClient httpclient = new DefaultHttpClient();
                System.out.println("refreshamount value : ------------"
                        + AppConstants.PRIVIOUSAMOUNT);
                HttpPost httppost = new HttpPost(AppConstants.PRIVIOUSAMOUNT);
                jsonObj = new JSONObject();
                jsonObj.put("uid", AppPreferences.getCustomerId(getActivity()));
                jsonObj.put("account_type", "99");
                jsonObj.put("latitute", AppPreferences.getLatutude(getActivity()));
                jsonObj.put("longitude", AppPreferences.getLongitude(getActivity()));

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObj);

                Log.d("json value:---------", jsonArray.toString());


                StringEntity se = null;

                try {
                    se = new StringEntity(jsonArray.toString(),"UTF-8");


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.v("json : ", jsonArray.toString(2));
                System.out.println("Sent JSON is : " + jsonArray.toString());

                httppost.setEntity(se);
                HttpResponse response = null;
                response = httpclient.execute(httppost);
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(response
                            .getEntity().getContent(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String jsonString = "";
                try {
                    jsonString = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("JSONString Response is --" + jsonString);
                if (jsonString != null) {
                    if (jsonString.contains("result")) {
                        JSONObject jsonObj = new JSONObject(jsonString);
                        // jsonString = jsonObj.getString("result");
                        // JSONArray jsonChildArray = jsonObj
                        // .getJSONArray("result");
                        if (jsonObj.getString("status").equalsIgnoreCase("200")) {
                            System.out
                                    .println("--------- message 200 got ----------");
                            status = 200;
                            //   JSONTokener tokener = new JSONTokener(jsonString);
                            JSONObject object = new JSONObject();
                            android.util.Log.e("JSON LOCATION", object.toString());
                            try {
                                JSONArray array = jsonObj.getJSONArray("result");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jo = array.getJSONObject(i);
                                    AppPreferences
                                            .setRefreshamount(getActivity(),jo.getInt("refreshamount"));
                                    Log.d("refreshamount---", String.valueOf(AppPreferences.getRefreshamount(getActivity())));

                                    Allbeans allbeans = new Allbeans();
                                    allbeans.setRefreshamount(REFRESHAMOUNT);
                                    al.add(allbeans);

                                }

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            return jsonString;
                        } else if (jsonObj.getString("status")
                                .equalsIgnoreCase("404")) {
                            status = 404;
                            return jsonString;
                        } else if (jsonObj.getString("status")
                                .equalsIgnoreCase("500")) {
                            status = 500;
                            return jsonString;
                        }
                    }
                }

            } catch (ConnectTimeoutException e) {
                System.out.println("Time out");
                status = 600;
            } catch (SocketTimeoutException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


        }

    }


    public class fetchLatLongFromService extends
            AsyncTask<Void, Void, StringBuilder> {

        String mLatlng;
        LatLng latLng;
        ProgressDialog mprProgressDialog;

        public fetchLatLongFromService(LatLng latLng) {
            super();
            this.latLng = latLng;
            this.mLatlng = latLng.latitude + "," + latLng.longitude;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprProgressDialog = new ProgressDialog(getActivity());
            mprProgressDialog.setMessage("Loading..");
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            this.cancel(true);
        }


        @Override
        protected StringBuilder doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                //http://maps.googleapis.com/maps/api/geocode/json?latlng=22.7000,75.9000&sensor=false

                String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
                        + this.mLatlng + "&sensor=false";
                Log.d("Fromserver", googleMapUrl);
                URL url = new URL(googleMapUrl);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(
                        conn.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
                String a = "";
                return jsonResults;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                JSONObject jsonObj = new JSONObject(result.toString());
                JSONArray resultJsonArray = jsonObj.getJSONArray("results");

                // Extract the Place descriptions from the results
                // resultList = new ArrayList<String>(resultJsonArray.length());

                // for (int i = 0; i < resultJsonArray.length(); i++) {
                JSONObject before_geometry_jsonObj = resultJsonArray
                        .getJSONObject(0);
                Log.d("Fromserver be", before_geometry_jsonObj.toString());
                JSONObject jsonObject = before_geometry_jsonObj.getJSONObject("geometry");
                Log.d("Fromserver ge", jsonObject.toString());
                JSONObject jsonObjectInner = jsonObject.getJSONObject("location");
                Log.d("Fromserver lo", jsonObjectInner.toString());
                String lat = jsonObjectInner.getString("lat");
                String lng = jsonObjectInner.getString("lng");
                Log.d("Fromserver", lat + " : " + lng);
                // if (lat.equalsIgnoreCase(String.valueOf(latLng.latitude)) || lng.equalsIgnoreCase(String.valueOf(latLng.longitude))) {
                fulladdress = before_geometry_jsonObj.getString("formatted_address");
                sourceedit.setText(fulladdress);
                latitute = String.valueOf(latLng.latitude);
                langitude = String.valueOf(latLng.longitude);
                mprProgressDialog.dismiss();


                //  }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    }

}
