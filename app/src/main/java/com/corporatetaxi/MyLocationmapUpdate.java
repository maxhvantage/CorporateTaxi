package com.corporatetaxi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.corporatetaxi.utils.AppConstants;
import com.corporatetaxi.utils.AppPreferences;
import com.corporatetaxi.utils.Function;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.corporatetaxi.utils.ConnectionDetector;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


//////////////////////its is testing class no need in project//////////////

public class MyLocationmapUpdate extends AppCompatActivity implements LocationListener, AdapterView.OnItemClickListener {
    Toolbar toolbar;
    GoogleMap map;
    LatLng mylocation;
    double lat, lon;
    LocationManager locationManager;
    Location location;
    Address address;
    Button mbtnrequest;
    // DialogManager alert = new DialogManager();
    boolean isGPSEnabled, isNetworkEnabled, canGetLocation;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    AutoCompleteTextView searchtext;
    private GoogleMap googleMap;
    private MapFragment mapFragment;
    double newlatitude, newlongitude;

    // ------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyAQ5Se4Tu1LmgZDAwQ-mguA73x__s8HsTY";
    Marker gmarkers = null;
    String str;
    LatLng loc;
    String cityname,fulladdress;
    String sorcelatitude = "";
    String sourcelangitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_locationmap_update);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout
        setSupportActionBar(toolbar); // Setting toolbar as the ActionBar with
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getString(R.string.action_destination);

        getSupportActionBar().setTitle(title);
        Intent in = getIntent();
        cityname = in.getStringExtra("city");
        sorcelatitude = in.getStringExtra("sorcelatitude");
        sourcelangitude = in.getStringExtra("sourcelongitude");
        searchtext = (AutoCompleteTextView) findViewById(R.id.search_text);
        mbtnrequest = (Button) findViewById(R.id.btn_request);
        searchtext.setText(cityname);
        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Montserrat-Regular.ttf");
        searchtext.setTypeface(tf);
        //    Toast.makeText(this, "  --------------- Location filter Activity ---------------", Toast.LENGTH_SHORT).show();
        searchtext.setAdapter(new GooglePlacesAutocompleteAdapter(this,
                R.layout.list_item));
        searchtext.setOnItemClickListener(this);


        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        getLocation();
        if (AppPreferences.getUserLatitude(MyLocationmapUpdate.this).equalsIgnoreCase("0.0") && AppPreferences.getUserLongitude(MyLocationmapUpdate.this).equalsIgnoreCase("0.0")) {
            loc = new LatLng(lat, lon);
        } else {
            System.out.println("show latitude ---------------- " + Double.parseDouble(AppPreferences.getUserLatitude(MyLocationmapUpdate.this)) + " " + Double.parseDouble(AppPreferences.getUserLongitude(MyLocationmapUpdate.this)));
            loc = new LatLng(Double.parseDouble(AppPreferences.getUserLatitude(MyLocationmapUpdate.this)), Double.parseDouble(AppPreferences.getUserLongitude(MyLocationmapUpdate.this)));
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        // map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.addMarker(new MarkerOptions().position(loc).title("Your Location"));
        mbtnrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyLocationmapUpdate.this,RequestTaxiActivity.class);
                intent.putExtra("city",cityname);
                intent.putExtra("sorcelatitude", sorcelatitude);
                intent.putExtra("sourcelongitude", sourcelangitude);
                startActivity(intent);

            }
        });
        /*
         * LocationManager locationManager = (LocationManager)
		 * getSystemService(LOCATION_SERVICE);
		 * locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		 * 0, 0, this);
		 *
		 * Location location = locationManager
		 * .getLastKnownLocation(LocationManager.GPS_PROVIDER);// lat =
		 * location.getLatitude(); lon = location.getLongitude();
		 */
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorblack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        getlocationfromaddress(str);
    }

    public void getlocationfromaddress(String strAddress) {
        if (new Function().CheckGpsEnableOrNot(getApplicationContext())) {
            System.out.println("sourceAddressText----------------------------" + strAddress);
            address = new Function().getLatLongFromGivenAddress(strAddress, getApplicationContext());
            // gmarkers.remove();
            googleMap = null;
            // googleMap.clear();
            initilizeMap();
        } else {
            new Function().showGpsSettingsAlert("GPS Settings...", "GPS is not enabled. Do you want to go to settings menu?", this);
        }
    }

    public Location getLocation() {
        try {
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
//                            lat = Double.parseDouble(AppPreferences.getUserLatitude(MyLocationmapUpdate.this));
//                            lat = Double.parseDouble(AppPreferences.getUserLongitude(MyLocationmapUpdate.this));
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
//        lat = Double.parseDouble(AppPreferences.getUserLatitude(MyLocationmapUpdate.this));
//        lat = Double.parseDouble(AppPreferences.getUserLongitude(MyLocationmapUpdate.this));
        lat = location.getLatitude();
        System.out.println(("Latitude:" + location.getLatitude()));
        lon = location.getLongitude();
        System.out.println(("Longitude:" + location.getLongitude()));

    }

/*
    Load URL data all country
*/

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE
                    + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            System.out.println("Google<<<<<<<<<<<URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("LOG_TAG", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("LOG_TAG", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString(
                        "description"));
                System.out
                        .println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString(
                        "description"));
            }
        } catch (JSONException e) {
            Log.e("LOG_TAG", "Cannot process JSON results", e);
        }

        return resultList;
    }


    public class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String>
            implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context,
                                               int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.

                        System.out.println("------------------- adapter calling ---------------");

                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint,
                                              Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    @SuppressLint("NewApi")
    private void initilizeMap() {
        try {
            System.out.println("---initialize Map called-----" + googleMap);
            if (googleMap == null) {
                mapFragment = ((MapFragment) getFragmentManager()
                        .findFragmentById(R.id.map));
                googleMap = mapFragment.getMap();
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setMyLocationEnabled(true);
                newlatitude = address.getLatitude();
                newlongitude = address.getLongitude();
                //   LatLng location = new LatLng(lat, lon);
                try {
                    System.out.println("lat :---------- " + address.getLatitude() + " long :---------- " + address.getLongitude());
                    if (address.getLatitude() != 0.0 && address.getLongitude() != 0.0) {

                        googleMap.addMarker(new MarkerOptions()
                                .position(
                                        new LatLng(address.getLatitude(),
                                                address.getLongitude()))
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        // MarkerOptions marker = new MarkerOptions().position(location).title("New Marker ").draggable(true);
                    }
                    ViewGroup.LayoutParams params = mapFragment.getView()
                            .getLayoutParams();
                    mapFragment.getView().setLayoutParams(params);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(address.getLatitude(), address.getLongitude()), 13.0f));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
//            Intent intent = new Intent(RequestTaxiActivity.this, DrawerMainActivity.class);
//            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
