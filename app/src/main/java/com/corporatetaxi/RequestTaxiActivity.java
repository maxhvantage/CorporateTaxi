package com.corporatetaxi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.corporatetaxi.beans.Allbeans;
import com.corporatetaxi.gcmclasses.Config;
import com.corporatetaxi.utils.AppConstants;
import com.corporatetaxi.utils.AppPreferences;
import com.corporatetaxi.utils.JSONfunctions;
import com.google.android.gcm.GCMRegistrar;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class RequestTaxiActivity extends AppCompatActivity implements LocationListener, AdapterView.OnItemClickListener {
    //////////places api///////////
    double lat, lon;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    AutoCompleteTextView msourceaddress, mdestiaddress, mdestinationtext, mstreetname;
    // ------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyDj-pGVjfFdWqmzmODEB3gx74YIEOsOvBE";
    private JSONObject jsonObj;
    JSONArray jsonarray;
    private ProgressDialog mProgressDialog;
    EditText mstreetnumber, mstreetdescription, mtripdate, mtriptime, mcompanyname;
    String streetname, streetnumber, stringdescription, companyname, sourceaddress, destiaddress, destination, tripdate, triptime;
    ArrayList<String> companylist;
    ArrayList<Allbeans> allbeanses;
    String datetrip = "";
    static final int DATE_DIALOG_ID = 999;
    Calendar mCalendar = Calendar.getInstance();
    private int mhour;
    private int mminute;
    private int msec;
    static final int TIME_DIALOG_ID = 1;
    String sourcefulladdress = "";
    String sorcelatitude = "";
    String sourcelangitude = "";
    String destinationaddress = "";
    double destinationlatitude;
    double destinationlongitude;
    String sourcecityname = "";
    String sourcestate = "";
    String sourcecountry = "";
    String sourcezipcodenumber;
    String desticityname = "";
    String destistate = "";
    String desticountry = "";
    int TRIPAMOUNT;
    String currentDateTimeString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_taxi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getString(R.string.title_activity_request_taxi);
        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle(title);
        ////////current date time//////////////////
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Log.d("currentdatetime", currentDateTimeString);

        Intent in = getIntent();
        sourcefulladdress = in.getStringExtra("city");
        sorcelatitude = in.getStringExtra("sorcelatitude");
        sourcelangitude = in.getStringExtra("sourcelongitude");
        destinationaddress = in.getStringExtra("destinationaddress");
        destinationlatitude = in.getDoubleExtra("destinationlatitude", 0.0);
        destinationlongitude = in.getDoubleExtra("destinationlongitude", 0.0);
        sourcecityname = in.getStringExtra("sourcecity");
        sourcestate = in.getStringExtra("sourcestate");
        sourcecountry = in.getStringExtra("sourcecountry");
        desticityname = in.getStringExtra("desticity");
        destistate = in.getStringExtra("destistate");
        desticountry = in.getStringExtra("desticountry");
        TRIPAMOUNT = in.getIntExtra("tripamount", 0);


        mstreetname = (AutoCompleteTextView) findViewById(R.id.street_name);
        mdestinationtext = (AutoCompleteTextView) findViewById(R.id.street_destination);
        msourceaddress = (AutoCompleteTextView) findViewById(R.id.source_addressname);
        mdestiaddress = (AutoCompleteTextView) findViewById(R.id.destination_address);
        // mstreetnumber = (EditText) findViewById(R.id.strret_number);
        mstreetdescription = (EditText) findViewById(R.id.description);
        mcompanyname = (EditText) findViewById(R.id.company_name);
        // sourcetext = (AutoCompleteTextView) findViewById(R.id.street_source);


        Log.d("cityname:--------", sourcefulladdress + "");
        msourceaddress.setText(sourcefulladdress);
        mstreetname.setText(sourcefulladdress);
        mdestinationtext.setText(destinationaddress);
        mdestiaddress.setText(destinationaddress);
        mcompanyname.setText(AppPreferences.getCompanyname(RequestTaxiActivity.this));


        Button confirm_btn = (Button) findViewById(R.id.confirmbutton);
        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Montserrat-Regular.ttf");

        confirm_btn.setTypeface(tf);
        mstreetname.setTypeface(tf);
        //   mstreetnumber.setTypeface(tf);
        mstreetdescription.setTypeface(tf);
        mcompanyname.setTypeface(tf);
        msourceaddress.setTypeface(tf);
        mdestiaddress.setTypeface(tf);
        //   mtripdate.setTypeface(tf);
        //  mtriptime.setTypeface(tf);
        mdestinationtext.setTypeface(tf);

        confirm_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                streetname = mstreetname.getText().toString();
                //    streetnumber = mstreetnumber.getText().toString();
                sourcefulladdress = msourceaddress.getText().toString();
                destiaddress = mdestiaddress.getText().toString();
                //   source = sourcetext.getText().toString().trim();
                destination = mdestinationtext.getText().toString().trim();
                stringdescription = mstreetdescription.getText().toString().trim();
                //  tripdate = datetrip;
                // triptime = mtriptime.getText().toString().trim();
                companyname = mcompanyname.getText().toString().trim();
                if (streetname.length() == 0) {
                    mstreetname.setError(getString(R.string.error_field_required));

                } else if (sourcefulladdress.length() == 0) {
                    msourceaddress.setError(getString(R.string.error_field_required));

                } else if (mdestinationtext.length() == 0) {
                    mdestinationtext.setError(getString(R.string.error_field_required));

                } else if (destiaddress.length() == 0) {
                    mdestiaddress.setError(getString(R.string.error_field_required));

                } else if (stringdescription.length() == 0) {
                    mstreetdescription.setError(getString(R.string.error_field_required));

                } else if (TRIPAMOUNT > AppPreferences.getRefreshamount(RequestTaxiActivity.this) ) {

                    amountcheckdialog();
                  //  Payment_Activity.mbtnpaycreadit.setEnabled(false);

                    //    Payment_Activity.mbtncash.setEnabled(true);


                } else {


                    Allbeans allbeans = new Allbeans();

                    allbeans.setStreetname(streetname);
                    allbeans.setStreetdescription(stringdescription);
                    allbeans.setCompanyname(companyname);
                    allbeans.setSourcelandmark(sourcefulladdress);
                    allbeans.setDestinationlandmark(destiaddress);
                    allbeans.setDestinationresuest(destination);
                     new RequestTaxiAsynch(allbeans).execute();
                }

            }
        });

        ///////////////////////////locationupdate//////////////////////


        //    Toast.makeText(this, "  --------------- Location filter Activity ---------------", Toast.LENGTH_SHORT).show();
        mstreetname.setAdapter(new GooglePlacesAutocompleteAdapter(this,
                R.layout.list_item));
        mdestinationtext.setAdapter(new GooglePlacesAutocompleteAdapter(this,
                R.layout.list_item));
//        msourceaddress.setAdapter(new GooglePlacesAutocompleteAdapter(this,
//                R.layout.list_item));
//        mdestiaddress.setAdapter(new GooglePlacesAutocompleteAdapter(this,
//                R.layout.list_item));
        mstreetname.setOnItemClickListener(this);
        mdestinationtext.setOnItemClickListener(this);
//        msourceaddress.setOnItemClickListener(this);
//        mdestiaddress.setOnItemClickListener(this);

        ///////////////////////////locationupdate//////////////////////
        ///back arrow ////////////
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorblack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        System.out.println(("Latitude:" + location.getLatitude()));
        lon = location.getLongitude();
        System.out.println(("Longitude:" + location.getLongitude()));
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    private class RequestTaxiAsynch extends AsyncTask<Void, Void, String> {
        Allbeans allbeans;
        Context context;
        ArrayList<Integer> catogariesid;
        private int status = 0;

        public RequestTaxiAsynch(Allbeans allbeans) {
            this.allbeans = allbeans;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(RequestTaxiActivity.this);
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
                System.out.println("REQUEST TAXI VALUE : ------------ "
                        + AppConstants.REQUESTTAXI);
                HttpPost httppost = new HttpPost(AppConstants.REQUESTTAXI);
                jsonObj = new JSONObject();
                jsonObj.put("customer_id", AppPreferences.getCustomerId(RequestTaxiActivity.this));
                jsonObj.put("sourcelandmark", allbeans.getStreetname());
                jsonObj.put("source_city", sourcecityname);
                jsonObj.put("source_state", sourcestate);
                jsonObj.put("source_country", sourcecountry);

                jsonObj.put("tripType", "corporate");
                jsonObj.put("description", allbeans.getStreetdescription());
                jsonObj.put("companyname", allbeans.getCompanyname());
                jsonObj.put("currentlatitute", AppPreferences.getLatutude(RequestTaxiActivity.this));
                jsonObj.put("currentlongitude", AppPreferences.getLongitude(RequestTaxiActivity.this));
                jsonObj.put("sourcelatitute", sorcelatitude);
                jsonObj.put("sourcelongitude", sourcelangitude);
//                jsonObj.put("sourcelatitute", "19.2946327");
//                jsonObj.put("sourcelongitude", "-99.66685230000002");
                jsonObj.put("device_id", getDeviceId());
                jsonObj.put("destination_lat", destinationlatitude);
                jsonObj.put("destination_lon", destinationlongitude);
                jsonObj.put("destinationlandmark", destinationaddress);
                jsonObj.put("sourceaddress", sourcefulladdress);
                jsonObj.put("destination_address", destiaddress);
                jsonObj.put("account_type", AppPreferences.getAccounttype(RequestTaxiActivity.this));
                jsonObj.put("desti_city", desticityname);
                jsonObj.put("desti_state", destistate);
                jsonObj.put("desti_country", desticountry);
                jsonObj.put("dateTime", currentDateTimeString);
                jsonObj.put("device_type", "ANDROID");
                //jsonObj.put("desti_zip", destinationzipcode);
                //  System.out.println("destinationzip--sending--" + destinationzipcode);
                jsonObj.put("amount", TRIPAMOUNT);


                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObj);

                Log.d("json value:----", jsonArray.toString());


                StringEntity se = null;
                try {
                    se = new StringEntity(jsonArray.toString(), "UTF-8");

                    /*se.setContentEncoding(new BasicHeader(
                            HTTP.CONTENT_ENCODING, "UTF-8"));*/
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
                    jsonString = reader.readLine().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("JSONString response is : " + jsonString);
                if (jsonString != null) {
                    if (jsonString.contains("result")) {
                        JSONObject jsonObj = new JSONObject(jsonString);
                        JSONObject jsonObject = jsonObj.getJSONObject("result");

                        AppPreferences.setApprequestTaxiScreen(RequestTaxiActivity.this, true);
                        Log.d("trip-id", AppPreferences.getTripId(RequestTaxiActivity.this));
                        if (jsonObj.getString("status").equalsIgnoreCase("200")) {
                            AppPreferences.setTripId(RequestTaxiActivity.this, jsonObject.getString("id"));
                            System.out
                                    .println("--------- message 200 got ----------");
                            status = 200;
                            return jsonString;
                        }else if (jsonObj.getString("status")
                                .equalsIgnoreCase("500")) {
                            status = 500;
                            return jsonString;
                        } else if (jsonObj.getString("status")
                                .equalsIgnoreCase("400")) {
                            status = 400;
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



                Intent intent = new Intent(RequestTaxiActivity.this,
                        ShowTaxi_InMap.class);

                intent.putExtra("sorcelatitude", sorcelatitude);
                Log.d("sourcelatitude",sorcelatitude);
                intent.putExtra("sourcelongitude", sourcelangitude);
                intent.putExtra("fulladdress", sourcefulladdress);
                startActivity(intent);
                AppPreferences.setSorclat(RequestTaxiActivity.this, sorcelatitude);
                AppPreferences.setSrcLng(RequestTaxiActivity.this, sourcelangitude);
                AppPreferences.setSorcAdd(RequestTaxiActivity.this, sourcefulladdress);

            } else if(status==400){
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.driver_nothere), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

//                Toast.makeText(getApplicationContext(),
//                        "Check Your Network Connection", Toast.LENGTH_LONG).show();

            }
        }

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
            Intent intent = new Intent(RequestTaxiActivity.this, DrawerMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //////////////////Findlocation///////////
    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE
                    + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            // sb.append("&components=country:in");
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

    protected void amountcheckdialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(RequestTaxiActivity.this);
//builder.setTitle(getString(R.string.cancel_trip));
//builder.setIcon(R.drawable.ic_launcher);
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.dialog_heading, null);
        TextView textView = (TextView) header.findViewById(R.id.text);
        Typeface tf = Typeface.createFromAsset(RequestTaxiActivity.this.getAssets(),
                "Montserrat-Regular.ttf");
        textView.setTypeface(tf);
        ImageView icon = (ImageView) header.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.ic_launcher);
        textView.setText("Alert !");
        builder.setCustomTitle(header);
        builder.setCancelable(false);

        builder.setMessage(getResources().getString(R.string.payment_check));


        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Allbeans allbeans = new Allbeans();

                        allbeans.setStreetname(streetname);
                        //  allbeans.setStreetnumber(streetnumber);
                        allbeans.setStreetdescription(stringdescription);
                        allbeans.setCompanyname(companyname);
                        allbeans.setSourcelandmark(sourcefulladdress);
                        allbeans.setDestinationlandmark(destiaddress);
                        //  allbeans.setSourcerequest(source);
                        allbeans.setDestinationresuest(destination);
                        //allbeans.setTripdate(tripdate);
                        //allbeans.setTriptime(triptime);
                        new RequestTaxiAsynch(allbeans).execute();

                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });


        builder.show();
    }
    public String getDeviceId() {

        GCMRegistrar.checkDevice(this.getApplicationContext());
        GCMRegistrar.checkManifest(this.getApplicationContext());
        final String regId = GCMRegistrar.getRegistrationId(this
                .getApplicationContext());

        // Check if regid already presents
        if (regId.equals("")) {
            Log.i("GCM K", "--- Regid = ''" + regId);
            GCMRegistrar.register(this.getApplicationContext(),
                    Config.GOOGLE_SENDER_ID);
            AppPreferences.setDeviceid(this, regId);
            return regId;
        } else {

            if (GCMRegistrar.isRegisteredOnServer(this)) {

                Log.i("GCM K2", "--- Regid = ''" + regId);
            } else {
                Log.i("GCM K3", "--- Regid = ''" + regId);

            }
            AppPreferences.setDeviceid(this, regId);
            return regId;
        }

    }

}

