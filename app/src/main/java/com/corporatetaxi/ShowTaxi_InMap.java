package com.corporatetaxi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.corporatetaxi.service.DrivermoveService;
import com.corporatetaxi.utils.JSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.corporatetaxi.beans.Allbeans;
import com.corporatetaxi.utils.AppConstants;
import com.corporatetaxi.utils.AppPreferences;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShowTaxi_InMap extends AppCompatActivity implements LocationListener {
   public static GoogleMap map;
    LatLng mylocation;
    ArrayList<Allbeans> al;
  public static LatLng loc2;
    double lat, lon;
    LocationManager locationManager;
    public static String DRIVERNAME, DRIVERMOBILE, DRIVERID;
    double DRIVERLATITUDE, DRIVERLONGITUDE;
    Location location;
    boolean isGPSEnabled, isNetworkEnabled, canGetLocation;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    Button btn_confirm;
    PopupWindow pwindo;
    RadioButton rd1, rd2, rd3;
    String canceltaxirequest, currentDateTimeString;
    TextView txt, textheader;
    ImageButton cross_btn;
    Dialog dialog;
  public  static   MarkerOptions marker2;
    public static ShowTaxi_InMap instance = null;

    public static boolean showTaxiInMap = false;
    public  static ShowTaxi_InMap showTaxi_InMapInstance = null;

    String sourceaddress, sorcelatitude,sourcelangitude;

    @Override
    protected void onStop() {
        showTaxiInMap = false;
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        showTaxi_InMapInstance = null;
    }
    @Override
    protected void onResume() {
        super.onResume();
        showTaxiInMap=true;

        try{
            System.out.println(" Notification message : "+AppPreferences.getNotificationMessage(getApplicationContext()));
            if(!AppPreferences.getNotificationMessage(getApplicationContext()).equalsIgnoreCase(""))
            {
                AppPreferences.setNotificationMessage(getApplicationContext(), "");
                Intent intent = new Intent(this, TaxiOntheWay_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data", AppPreferences.getNotificationMessage(getApplicationContext()));
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                GCMIntentService.mNotificationManager.cancel(GCMIntentService.NOTIFICATION_ID);

                finish();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_taxi__in_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getString(R.string.title_activity_showtaxi);
        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle(title);
        showTaxi_InMapInstance = this;
        instance = this;
        sorcelatitude = getIntent().getStringExtra("sorcelatitude");
        sourcelangitude = getIntent().getStringExtra("sourcelongitude");
        sourceaddress = getIntent().getStringExtra("fulladdress");


        ////////current date time//////////////////
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Log.d("currentdatetime", currentDateTimeString);
        al = new ArrayList<Allbeans>();
        new FetchdriverData().execute();
       // getApplicationContext().startService(new Intent(getApplicationContext(), DrivermoveService.class));
        //startService(new Intent(ShowTaxi_InMap.this, MyService.class));
        Typeface tf = Typeface.createFromAsset(ShowTaxi_InMap.this.getAssets(),
                "Montserrat-Regular.ttf");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.crossbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiatePopupWindowcanceltaxi();
            }
        });
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        getLocation();
        if(AppPreferences.getSorcAdd(instance)!=null){
            final LatLng loc = new LatLng(new Double(AppPreferences.getSorclat(instance)), new Double(AppPreferences.getSrcLng(instance)));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
            MarkerOptions marker = new MarkerOptions().position(loc).title(AppPreferences.getSorcAdd(instance));
          //  marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_three));
            map.addMarker(marker);
        }

        /////back arrow ////////////
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorblack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }


    public Location getLocation() {
        try {
            map.setMyLocationEnabled(true);
            locationManager = (LocationManager) this
                    .getSystemService(LOCATION_SERVICE);

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
        System.out.println("location latitude:-------" + location.getLongitude());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.request, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            initiatePopupWindowcanceltaxi();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener cancle_btn_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            dialog.dismiss();
        }
    };

    public void initiatePopupWindowcanceltaxi() {
        dialog = new Dialog(ShowTaxi_InMap.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.canceltaxi_popup);

        cross_btn = (ImageButton) dialog.findViewById(R.id.cross);
        cross_btn.setOnClickListener(cancle_btn_click_listener);
        rd1 = (RadioButton) dialog.findViewById(R.id.radioButton);
        rd2 = (RadioButton) dialog.findViewById(R.id.radioButton2);
        rd3 = (RadioButton) dialog.findViewById(R.id.radioButton3);
        btn_confirm = (Button) dialog.findViewById(R.id.btn_acceptor);
        txt = (TextView) dialog.findViewById(R.id.textView);
        textheader = (TextView) dialog.findViewById(R.id.popup_text);
        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Montserrat-Regular.ttf");
        rd1.setTypeface(tf);
        rd2.setTypeface(tf);
        rd3.setTypeface(tf);
        btn_confirm.setTypeface(tf);
        txt.setTypeface(tf);
        textheader.setTypeface(tf);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (rd1.isChecked()) {

                    canceltaxirequest = getResources().getString(R.string.prompt_cancel_reason_one);

                } else if (rd2.isChecked()) {
                    canceltaxirequest = getResources().getString(R.string.prompt_cancel_reason_two);

                } else if (rd3.isChecked()) {
                    canceltaxirequest = getResources().getString(R.string.prompt_cancel_reason_three);

                }


                Allbeans allbeans = new Allbeans();

                allbeans.setCanceltaxirequest(canceltaxirequest);

                new CancelTaxiAsynch(allbeans).execute();
                dialog.dismiss();


            }
        });

        dialog.show();

    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radioButton:
                if (checked)

                    break;
            case R.id.radioButton2:
                if (checked)
                    break;

            case R.id.radioButton3:
                if (checked)
                    break;
        }
    }


    private class CancelTaxiAsynch extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;
        Allbeans allbeans;
        private JSONObject jsonObj;
        ArrayList<Integer> catogariesid;
        private int status = 0;

        public CancelTaxiAsynch(Allbeans allbeans) {
            this.allbeans = allbeans;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ShowTaxi_InMap.this);
            mProgressDialog.setMessage(getResources().getString(R.string.progressmsg));
            mProgressDialog.show();

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
                System.out.println("Cancel Taxi Value:------------"
                        + AppConstants.CANCELTAXI);
                HttpPost httppost = new HttpPost(AppConstants.CANCELTAXI);
                jsonObj = new JSONObject();
                jsonObj.put("uid", AppPreferences.getCustomerId(ShowTaxi_InMap.this));
                jsonObj.put("trip_id", AppPreferences.getTripId(ShowTaxi_InMap.this));
                jsonObj.put("account_type", "99");
                jsonObj.put("canceltaxirequest", allbeans.getCanceltaxirequest());
                jsonObj.put("dateTime", currentDateTimeString);
                jsonObj.put("latitude", AppPreferences.getLatutude(ShowTaxi_InMap.this));
                jsonObj.put("longitude", AppPreferences.getLongitude(ShowTaxi_InMap.this));


                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObj);

                Log.d("json value:---------", jsonArray.toString());


                StringEntity se = null;

                try {
                    se = new StringEntity(jsonArray.toString(),"UTF-8");


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.v("json-:", jsonArray.toString(2));
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
                System.out.println("JSONString response is : " + jsonString);
                if (jsonString != null) {
                    if (jsonString.contains("result")) {
                        JSONObject jsonObj = new JSONObject(jsonString);
                        jsonString = jsonObj.getString("result");
                        // JSONArray jsonChildArray = jsonObj
                        // .getJSONArray("result");
                        if (jsonObj.getString("status").equalsIgnoreCase("200")) {
                            System.out.println("--------- message 200 got ----------");
                            status = 200;
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
            mProgressDialog.dismiss();
            Log.d("status", status + "");
            if (status == 200) {
                AppPreferences.setApprequestTaxiScreen(ShowTaxi_InMap.this, false);
                Intent intent = new Intent(ShowTaxi_InMap.this, DrawerMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        }

    }


    private class FetchdriverData extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;
        int status=0;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog = new ProgressDialog(ShowTaxi_InMap.this);
            dialog.setMessage("Loading...");
            dialog.show();
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
                System.out.println("REQUEST TAXI VALUE : ------------ "
                        + AppConstants.DRIVERINFO);
                HttpPost httppost = new HttpPost(AppConstants.DRIVERINFO);
                JSONObject  jsonObject = new JSONObject();
                jsonObject.put("customer_id", AppPreferences.getCustomerId(ShowTaxi_InMap.this));
                jsonObject.put("latitute", sorcelatitude);
                jsonObject.put("longitude", sourcelangitude);


                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                Log.d("json value:----", jsonArray.toString());


                StringEntity se = null;
                try {
                    se = new StringEntity(jsonArray.toString(),"UTF-8");

//                    se.setContentEncoding(new BasicHeader(
//                            HTTP.CONTENT_ENCODING, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.v("json : ", jsonArray.toString(2));
                System.out.println("SENT JSON is : " + jsonArray.toString());
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
                System.out.println("JSONString response is : " + jsonString);
                if (jsonString != null) {
                    if (jsonString.contains("result")) {
                        JSONObject jsonObj = new JSONObject(jsonString);
                        JSONArray array = jsonObj.getJSONArray("result");
                        //  AppPreferences.setTripId(getActivity(), jsonObject.getString("id"));
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jo = array.getJSONObject(i);
                            DRIVERNAME = jo.getString("username");
                            DRIVERMOBILE = jo.getString("mobile");
                            DRIVERID = jo.getString("driver_id");
                            DRIVERLATITUDE = jo.getDouble("latitude");
                            DRIVERLONGITUDE = jo.getDouble("longitude");
                            Allbeans allbeans = new Allbeans();
                            allbeans.setDrivername(DRIVERNAME);
                            allbeans.setDrivermobilenumber(DRIVERMOBILE);
                            allbeans.setDriverid(DRIVERID);
                            allbeans.setDriverlatitute(DRIVERLATITUDE);
                            allbeans.setDriverlongitude(DRIVERLONGITUDE);
                            al.add(allbeans);

                        }


                        if (jsonObj.getString("status").equalsIgnoreCase("200")) {
                            System.out
                                    .println("--------- message 200 got ----------");
                            status = 200;
                            return jsonString;
                        } else if (jsonObj.getString("status")
                                .equalsIgnoreCase("400")) {
                            status = 400;
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

            Log.d("status", status + "");
            if (status == 200) {

                for (int j = 0; j < al.size(); j++) {
                    Log.d("latlng", "" + al.get(j).getDriverlatitute());
                    Log.d("drivername", al.get(j).getDrivername());

                    loc2 = new LatLng(al.get(j).getDriverlatitute(), al.get(j).getDriverlongitude());
                    String drivername = al.get(j).getDrivername();
                    marker2 = new MarkerOptions().position(loc2);
                    marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.drivertaxi));
                    map.addMarker(marker2.title(drivername));

                }

            } else if(status==400) {

                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.no_driver), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
            dialog.dismiss();
        }
    }


//    private class FetchdriverData extends AsyncTask<Void, Void, Void> {
//        ProgressDialog dialog;
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            dialog = new ProgressDialog(ShowTaxi_InMap.this);
//            dialog.setMessage("Loading...");
//            dialog.show();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            // TODO Auto-generated method stub
//            try {
//                HttpParams httpParameters = new BasicHttpParams();
//                ConnManagerParams.setTimeout(httpParameters,
//                        AppConstants.NETWORK_TIMEOUT_CONSTANT);
//                HttpConnectionParams.setConnectionTimeout(httpParameters,
//                        AppConstants.NETWORK_CONNECTION_TIMEOUT_CONSTANT);
//                HttpConnectionParams.setSoTimeout(httpParameters,
//                        AppConstants.NETWORK_SOCKET_TIMEOUT_CONSTANT);
//
//                HttpClient httpclient = new DefaultHttpClient(httpParameters);
//                JSONParser parser = new JSONParser();
//                JSONObject object = parser.getJSON(AppConstants.DRIVERINFO);
//                android.util.Log.e("JSON DRIVER", object.toString());
//                try {
//                    JSONArray array = object.getJSONArray("result");
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject jo = array.getJSONObject(i);
//                        DRIVERNAME = jo.getString("username");
//                        DRIVERMOBILE = jo.getString("mobile");
//                        DRIVERID = jo.getString("driver_id");
//                        DRIVERLATITUDE = jo.getDouble("latitude");
//                        DRIVERLONGITUDE = jo.getDouble("longitude");
//                        Allbeans allbeans = new Allbeans();
//                        allbeans.setDrivername(DRIVERNAME);
//                        allbeans.setDrivermobilenumber(DRIVERMOBILE);
//                        allbeans.setDriverid(DRIVERID);
//                        allbeans.setDriverlatitute(DRIVERLATITUDE);
//                        allbeans.setDriverlongitude(DRIVERLONGITUDE);
//                        al.add(allbeans);
//
//                    }
//
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            for (int j = 0; j < al.size(); j++) {
//                Log.d("latlng", "" + al.get(j).getDriverlatitute());
//                Log.d("drivername", al.get(j).getDrivername());
//                loc2 = new LatLng(al.get(j).getDriverlatitute(), al.get(j).getDriverlongitude());
//                String drivername = al.get(j).getDrivername();
//                marker2 = new MarkerOptions().position(loc2);
//                marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.drivertaxi));
//                map.addMarker(marker2.title(drivername));
//
//            }
//            dialog.dismiss();
//        }
//    }

    @Override
    public void onBackPressed() {

    }
}


