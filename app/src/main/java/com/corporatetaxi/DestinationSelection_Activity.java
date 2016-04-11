package com.corporatetaxi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.corporatetaxi.adapter.DestinationListAdapter;
import com.corporatetaxi.adapter.DestinationzoneRecycle_Adapter_;
import com.corporatetaxi.beans.Allbeans;
import com.corporatetaxi.utils.AppConstants;
import com.corporatetaxi.utils.AppPreferences;
import com.corporatetaxi.utils.JSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Eyon on 12/16/2015.
 */
public class DestinationSelection_Activity extends AppCompatActivity implements LocationListener {
    AutoCompleteTextView mautoserchtext;
    ListView mlistview;
    Button mbtn_requesttaxi, btn_ok;
    ImageButton cross;
    static Dialog dialog = null;
    //////////////////////////popup//////////////////////////
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
    String sourcefulladdress, fulladdressdestination, sorcelatitude, sourcelangitude, sourcecity, sourcestate, sourcecountry, sourcezipcodenumber, desticity, destistate, desticountry, destizipcode;
    //String destinationlatitute, destinationlangitude;
    String demodestiadddress;
    int destinationtripamount;
    double demodestinationlatitute;
    double demodestinationlangitude;

    LatLng loc2;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinationzone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Setting toolbar as the ActionBar with
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getString(R.string.action_destination);
        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle(title);
        Intent in = getIntent();
        sourcefulladdress = in.getStringExtra("city");
        sorcelatitude = in.getStringExtra("sorcelatitude");
        sourcelangitude = in.getStringExtra("sourcelongitude");
        sourcecity = in.getStringExtra("sourcecity");
        sourcestate = in.getStringExtra("sourcestate");
        sourcecountry = in.getStringExtra("sourcecountry");


        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorblack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        mautoserchtext = (AutoCompleteTextView) findViewById(R.id.search_text);
        mlistview = (ListView) findViewById(R.id.listView_destination);
        //recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Allbeans allbeans = al.get(position);
                Intent intent = new Intent(DestinationSelection_Activity.this, RequestTaxiActivity.class);
                intent.putExtra("city", sourcefulladdress);
                intent.putExtra("sorcelatitude", sorcelatitude);
                intent.putExtra("sourcelongitude", sourcelangitude);
                intent.putExtra("sourcecity", sourcecity);
                intent.putExtra("sourcestate", sourcestate);
                intent.putExtra("sourcecountry", sourcecountry);

                System.out.println("sourcezipcode--sending--" + sourcezipcodenumber);

                intent.putExtra("destinationaddress", allbeans.getPriviousdestinationaddress());
                System.out.println("destination--sending--" + allbeans.getPriviousdestinationaddress());

                intent.putExtra("destinationlatitude", allbeans.getPriviousdestinationlatitude());
                System.out.println("destinationlatitude-sending-" + allbeans.getPriviousdestinationlatitude());

                intent.putExtra("destinationlongitude", allbeans.getPriviousdestinationlongitude());
                System.out.println("destinationlongitude--sending--" + allbeans.getPriviousdestinationlongitude());

                intent.putExtra("tripamount", allbeans.getDestinationamount());
                System.out.println("destination_amount:------" + allbeans.getDestinationamount());

                intent.putExtra("desticity", desticity);
                intent.putExtra("destistate", destistate);
                intent.putExtra("desticountry", desticountry);
                startActivity(intent);
            }
        });
        mbtn_requesttaxi = (Button) findViewById(R.id.btn_request);
        Typeface tf = Typeface.createFromAsset(DestinationSelection_Activity.this.getAssets(),
                "Montserrat-Regular.ttf");
        mautoserchtext.setTypeface(tf);
        mbtn_requesttaxi.setTypeface(tf);

        mautoserchtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindowdestinationtaxi();

//                Allbeans allbeans = new Allbeans();
//                al = new ArrayList<Allbeans>();
//                allbeans.setPriviousdestinationaddress(demodestiadddress);
//                allbeans.setPriviousdestinationlatitude(demodestinationlatitute);
//                allbeans.setPriviousdestinationlongitude(demodestinationlangitude);
//                new PriviousDestinationmappoint(allbeans).execute();


            }
        });
        mbtn_requesttaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mautoserchtext.length() == 0) {
                    mautoserchtext.setError(getResources().getString(R.string.error_field_required));

                } else {

                    Intent intent = new Intent(DestinationSelection_Activity.this, RequestTaxiActivity.class);
                    intent.putExtra("city", sourcefulladdress);
                    intent.putExtra("sorcelatitude", sorcelatitude);
                    intent.putExtra("sourcelongitude", sourcelangitude);
                    intent.putExtra("sourcecity", sourcecity);
                    intent.putExtra("sourcestate", sourcestate);
                    intent.putExtra("sourcecountry", sourcecountry);
                    intent.putExtra("sourcezipcode", sourcezipcodenumber);
                    System.out.println("sourcezipcode--sending--" + sourcezipcodenumber);
                    intent.putExtra("destinationaddress", fulladdressdestination);
                    intent.putExtra("destinationlatitude", demodestinationlatitute);
                    intent.putExtra("destinationlongitude", demodestinationlangitude);
                    intent.putExtra("desticity", desticity);
                    intent.putExtra("destistate", destistate);
                    intent.putExtra("desticountry", desticountry);
                    intent.putExtra("tripamount", destinationtripamount);

                    startActivity(intent);
                }
            }
        });

        Allbeans allbeans = new Allbeans();
        al = new ArrayList<Allbeans>();
        allbeans.setPriviousdestinationaddress(demodestiadddress);
        allbeans.setPriviousdestinationlatitude(demodestinationlatitute);
        allbeans.setPriviousdestinationlongitude(demodestinationlangitude);
        new PriviousDestinationaddress(allbeans).execute();
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

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener cancle_btn_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            dialog.dismiss();
        }
    };

    public void initiatePopupWindowdestinationtaxi() {
        boolean status = false;
        if (dialog == null) {
            status = true;
        }
        if (status) {
            dialog = new Dialog(DestinationSelection_Activity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.destination_popup);
        }
        cross = (ImageButton) dialog.findViewById(R.id.cross_popup);
        cross.setOnClickListener(cancle_btn_click_listener);
        btn_ok = (Button) dialog.findViewById(R.id.button_ok);
        TextView txt = (TextView) dialog.findViewById(R.id.popup_text);
        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Montserrat-Regular.ttf");
        txt.setTypeface(tf);
        btn_ok.setTypeface(tf);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapview)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        getLocation();
        LatLng loc = new LatLng(lat, lon);
        MarkerOptions marker3 = new MarkerOptions().position(loc);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        //  LatLng loc = new LatLng(demodestinationlatitute, demodestinationlangitude);
//        MarkerOptions marker2 = new MarkerOptions().position(loc);
//        marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        for (int j = 0; j < al.size(); j++) {
            Log.d("latlng", "" + al.get(j).getPrivioussourcelatitude());
//                Log.d("drivername", al.get(j).getDrivername());
        loc2 = new LatLng(al.get(j).getPriviousdestinationlatitude(), al.get(j).getPriviousdestinationlongitude());


            MarkerOptions marker2 = new MarkerOptions().position(loc2);
            marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            //  marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.drivertaxi));
            // map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc2, 15));
            map.addMarker(marker2);
        }


        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                map.getCameraPosition();
                cameraPosition = map.getCameraPosition();
                Log.d("postionnnnn", "" + cameraPosition.target);
                LatLng latLng = cameraPosition.target;
                Log.d("postionnnnn", "" + latLng.longitude + " : " + latLng.latitude);

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(DestinationSelection_Activity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(cameraPosition.target.latitude, cameraPosition.target.longitude, 1);
                    String destizipcodee = "";// Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    try {
                        demodestinationlatitute = (addresses.get(0).getLatitude());
                        demodestinationlangitude = (addresses.get(0).getLongitude());
                        if (addresses.get(0).getAddressLine(3) != null) {
                            destizipcodee = "," + addresses.get(0).getAddressLine(3);
                        }
                        fulladdressdestination = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getAddressLine(1) + ","
                                + addresses.get(0).getAddressLine(2) + destizipcodee;
                        Log.d("fulladdresss", fulladdressdestination);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        demodestinationlatitute = 0.0;
                        demodestinationlangitude = 0.0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mautoserchtext.setText(fulladdressdestination);


                if (demodestinationlatitute == 0.0 && demodestinationlangitude == 0.0) {
                    Toast.makeText(getApplicationContext(), "Our sevice is not available in this area", Toast.LENGTH_LONG).show();
                } else {
                    dialog.dismiss();
                }


            }
        });

        dialog.show();

    }


    public Location getLocation() {
        try {
            map.setMyLocationEnabled(true);
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog = null;
    }

    private class PriviousDestinationaddress extends AsyncTask<Void, Void, String> {

        Allbeans allbeans;
        private JSONObject jsonObj;
        ArrayList<Integer> catogariesid;
        private int status = 0;
        ProgressDialog mProgressDialog;

        public PriviousDestinationaddress(Allbeans allbeans) {
            this.allbeans = allbeans;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(DestinationSelection_Activity.this);
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
                System.out.println("destination selection: ------------ "
                        + AppConstants.PRIVIOUSSOURCEANDDESTINATION);
                HttpPost httppost = new HttpPost(AppConstants.PRIVIOUSSOURCEANDDESTINATION);
                jsonObj = new JSONObject();
                jsonObj.put("uid", AppPreferences.getCustomerId(DestinationSelection_Activity.this));
                jsonObj.put("account_type", "99");
                jsonObj.put("latitute", AppPreferences.getLatutude(DestinationSelection_Activity.this));
                jsonObj.put("longitude", AppPreferences.getLongitude(DestinationSelection_Activity.this));
                jsonObj.put("sourcefulladdress", sourcefulladdress);
                jsonObj.put("sourcelatitude", String.valueOf(sorcelatitude));
                jsonObj.put("sourcelongitude", String.valueOf(sourcelangitude));

//                jsonObj.put("sourcelatitude", "19.2946327");
//                jsonObj.put("sourcelongitude", "-99.66685230000002");
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObj);
                Log.d("json value:---------", jsonArray.toString());

                StringEntity se = null;

                try {
                    se = new StringEntity(jsonArray.toString(),"UTF-8");

//                    se.setContentEncoding(new BasicHeader(
//                            HTTP.CONTENT_ENCODING, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.v("json : ", jsonArray.toString(2));
                System.out.println("Sent JSON is : " + jsonArray.toString());

                httppost.setEntity(se);
                HttpResponse response = null;
                response = httpclient.execute(httppost);
                BufferedReader reader = null;

                HttpEntity resEntity = response.getEntity();

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
                    //jsonString = EntityUtils.toString(resEntity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("JSONString_response_is_priviousdestination -" + jsonString);
                if (jsonString != null) {
                    if (jsonString.contains("result")) {
                        JSONObject jsonObj = new JSONObject(jsonString);
                        if (jsonObj.getString("status").equalsIgnoreCase("200")) {
                            System.out
                                    .println("--------- message 200 got ----------");
                            status = 200;
                            JSONObject object = new JSONObject();
                            android.util.Log.e("JSON LOCATION", object.toString());
                            try {
                                JSONArray array = jsonObj.getJSONArray("result");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jo = array.getJSONObject(i);

//                                    String PRIVIOUSTRIPID = jo.getString("uid");

                                    demodestiadddress = jo.getString("destination_landmark");
                                    Log.d("destination--------", jo.getString("destination_landmark"));
                                    demodestinationlatitute = jo.getDouble("destination_latitude");
                                    demodestinationlangitude = jo.getDouble("destination_longitude");
                                    destinationtripamount = jo.getInt("destination_amount");

                                    Allbeans allbeans = new Allbeans();
                                    //  allbeans.setDriverid(PRIVIOUSTRIPID);
                                    allbeans.setPriviousdestinationlatitude(demodestinationlatitute);
                                    allbeans.setPriviousdestinationlongitude(demodestinationlangitude);
                                    allbeans.setPriviousdestinationaddress(demodestiadddress);
                                    allbeans.setDestinationamount(destinationtripamount);
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


            if (status == 200) {
                DestinationListAdapter adapter = new DestinationListAdapter(DestinationSelection_Activity.this, R.layout.customdestination, al);
                mlistview.setAdapter(adapter);
                mProgressDialog.dismiss();
            } else {
//                Toast.makeText(DestinationSelection_Activity.this, "Destination Addresses Not found", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(android.R.id.content), "There is no route defined to book a trip", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mProgressDialog.dismiss();
            }
        }

    }


}