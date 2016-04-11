package com.corporatetaxi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.corporatetaxi.beans.Allbeans;
import com.corporatetaxi.service.DriverService;
import com.corporatetaxi.utils.AppConstants;
import com.corporatetaxi.utils.AppPreferences;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Eyon on 11/9/2015.
 */
public class TaxiOntheWay_Activity extends AppCompatActivity implements LocationListener {
    Button btn_confirm;
    RadioButton rd1, rd2, rd3, rd4, rd5, rd6;
    String canceltaxirequest, sendmessage, currentDateTimeString;
    ImageButton cross, mcross;
    ImageView mdriverimage;
    Dialog dialog;
    LatLng loc2;
    double lat, lon;
    GoogleMap map;
    LocationManager locationManager;
    Location location;
    boolean isGPSEnabled, isNetworkEnabled, canGetLocation;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    TextView textheader, txt_header, textname, textmobilenumber, textcompanyname, texttaxinumber,
            mtextnamehead, mtextcompanyhead, mtextmobilehead, mtexttexinumhead, taxiname, taxinamehead,medriverinsurance,mdriverlicense;
    public static String drivermobile = "";
    public static String drivername = "";
    public static String drivercompanyname = "";
    public static String drivertexinumber = "";
    public static String drivertaxiname = "";
    public static String driverimage = "";
    public static String SourceAddress = "";
    public static String driverinsurance= "";
    public static String driverlicense = "";
    FloatingActionButton fab_msg, fab_call, fab_cancel;
    FloatingActionsMenu fab_menu;
    double driverlatitude, driverlongitude, sourcelatitude , sourcelongitude;
    public  static TaxiOntheWay_Activity taxiOntheWay_activity_instance = null;
    Marker marker1 = null;


    private GoogleApiClient client;

    public static boolean taxiOnTheWay = false;

    @Override
    public void onStop() {
        taxiOnTheWay = false;
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        taxiOnTheWay = true;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d("activityTest", "chanFoc");
        taxiOnTheWay = true;
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxiontheway);
        taxiOntheWay_activity_instance = this;
        AppPreferences.setApprequestTaxiScreen(TaxiOntheWay_Activity.this, true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getString(R.string.title_activity_taxidetail);
        getSupportActionBar().setTitle(title);

        fab_menu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
         fab_msg = (FloatingActionButton) findViewById(R.id.fab_message);
         fab_call = (FloatingActionButton) findViewById(R.id.fab_call);
         fab_cancel = (FloatingActionButton) findViewById(R.id.fab_cancel);

        textheader = (TextView) findViewById(R.id.textheader);
        textname = (TextView) findViewById(R.id.name_text);
        textmobilenumber = (TextView) findViewById(R.id.mobile_text);
        textcompanyname = (TextView) findViewById(R.id.companyname);
        texttaxinumber = (TextView) findViewById(R.id.taxinumber);
        taxiname = (TextView) findViewById(R.id.taxinametext);
        mtextnamehead = (TextView) findViewById(R.id.namehead);
        mtextcompanyhead = (TextView) findViewById(R.id.companyhead);
        mtextmobilehead = (TextView) findViewById(R.id.mobilehead);
        mtexttexinumhead = (TextView) findViewById(R.id.taxiplatthead);
        taxinamehead = (TextView) findViewById(R.id.taxinamehead);
        mdriverimage = (ImageView) findViewById(R.id.driver_image);
        mdriverlicense = (TextView) findViewById(R.id.driverlicense);
        medriverinsurance = (TextView) findViewById(R.id.driverinsurance);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        getLocation();


        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Montserrat-Regular.ttf");

        textheader.setTypeface(tf);
        mtextnamehead.setTypeface(tf);
        mtextcompanyhead.setTypeface(tf);
        mtextmobilehead.setTypeface(tf);
        mtexttexinumhead.setTypeface(tf);
        textname.setTypeface(tf);
        textmobilenumber.setTypeface(tf);
        textcompanyname.setTypeface(tf);
        texttaxinumber.setTypeface(tf);
        taxinamehead.setTypeface(tf);
        taxiname.setTypeface(tf);
        mdriverlicense.setTypeface(tf);
        medriverinsurance.setTypeface(tf);

        ////////current date time//////////////////
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Log.d("currentdatetime", currentDateTimeString);

        /////back arrow ////////////
//        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        upArrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        /////////////notification data///////////////
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Log.d("data value", data + "");
       // caceldialog();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            AppPreferences.setAcceptdriverId(TaxiOntheWay_Activity.this, jsonObject.getString("driverId"));
            Log.d("driverid---", AppPreferences.getAcceptdriverId(TaxiOntheWay_Activity.this));
            drivermobile = jsonObject.getString("mobile");
            drivername = jsonObject.getString("username");
            drivercompanyname = jsonObject.getString("taxicompany");
            drivertaxiname = jsonObject.getString("vehicalname");
            drivertexinumber = jsonObject.getString("vehicle_number");
           // driverlatitude = jsonObject.getDouble("latitude");
           // driverlongitude = jsonObject.getDouble("longitude");
            driverimage = jsonObject.getString("driverImage");
            SourceAddress = jsonObject.getString("source_address");
            sourcelatitude = jsonObject.getDouble("source_latitude");
            sourcelongitude = jsonObject.getDouble("source_longitude");
            driverlicense = jsonObject.getString("driverlicense");
            driverinsurance = jsonObject.getString("driverinsurance");

            Log.d("driveriamge", driverimage);

            final LatLng loc = new LatLng(new Double(sourcelatitude), new Double(sourcelongitude));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
            MarkerOptions marker = new MarkerOptions().position(loc).title(SourceAddress);
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_three));
            map.addMarker(marker);


            textname.setText(drivername);
            textmobilenumber.setText(drivermobile);
            textcompanyname.setText(drivercompanyname);
            texttaxinumber.setText(drivertexinumber);
            taxiname.setText(drivertaxiname);
            mdriverlicense.setText(driverlicense);
            medriverinsurance.setText(driverinsurance);

            if(mdriverlicense.length()==0){
                mdriverlicense.setVisibility(View.GONE);
            }
            if(medriverinsurance.length()==0){
                medriverinsurance.setVisibility(View.GONE);
            }

            Intent intent1 = new Intent(TaxiOntheWay_Activity.this, DriverService.class);
            intent1.putExtra("driverId", jsonObject.getString("driverId"));
            startService(intent1);

//            loc2 = new LatLng(driverlatitude, driverlongitude);
//            MarkerOptions marker2 = new MarkerOptions().position(loc2);
//            marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.drivertaxi));
//            map.addMarker(marker2);
            Timer timer;
            TimerTask task;
            int delay = 10000;
            int period = 10000;

            timer = new Timer();

            timer.scheduleAtFixedRate(task = new TimerTask() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*loc2 = new LatLng(new Double(AppPreferences.getCurrentlat(TaxiOntheWay_Activity.this)),
                                    new Double(AppPreferences.getCurrentlong(TaxiOntheWay_Activity.this)));
                            MarkerOptions marker2 = new MarkerOptions().position(loc2);
                            map.clear();
                            marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.drivertaxi));
                            map.addMarker(marker2.title(drivername));*/
                            loc2 = new LatLng(new Double(AppPreferences.getCurrentlat(TaxiOntheWay_Activity.this)),
                                    new Double(AppPreferences.getCurrentlong(TaxiOntheWay_Activity.this)));

                            if(marker1==null){
                                marker1 = map.addMarker(new MarkerOptions()
                                        .position(loc2)
                                        .title(drivername)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.drivertaxi)));

                            }
                            animateMarker(marker1, loc2, false);


                        }
                    });






                }
            }, delay, period);


        } catch (JSONException e) {
            e.printStackTrace();
        }
/////////////notification dataend///////////////
        fab_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindowcanceltaxi();
            }
        });


        fab_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindowsendmesage();
            }
        });
        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + drivermobile));
                startActivity(callIntent);
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        if(driverimage.equalsIgnoreCase("")){
            mdriverimage.setImageResource(R.drawable.ic_action_user);

        }else{
        Picasso.with(getApplicationContext()).load(driverimage)
                .error(R.drawable.ic_action_user)
                .resize(200, 200)
                .into(mdriverimage);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }


    private View.OnClickListener cancle_btn_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            dialog.dismiss();
        }
    };

    private void initiatePopupWindowcanceltaxi() {
        try {
            dialog = new Dialog(TaxiOntheWay_Activity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.canceltaxi_popup);

            cross = (ImageButton) dialog.findViewById(R.id.cross);
            cross.setOnClickListener(cancle_btn_click_listener);
            rd1 = (RadioButton) dialog.findViewById(R.id.radioButton);
            rd2 = (RadioButton) dialog.findViewById(R.id.radioButton2);
            rd3 = (RadioButton) dialog.findViewById(R.id.radioButton3);
            btn_confirm = (Button) dialog.findViewById(R.id.btn_acceptor);
            TextView txt = (TextView) dialog.findViewById(R.id.textView);
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


                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radioButton:
                if (checked)

                    break;
            case R.id.radioButton1:
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "TaxiOntheWay_ Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.corporatetaxi/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
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
            mProgressDialog = new ProgressDialog(TaxiOntheWay_Activity.this);
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
                System.out.println("Cancel Taxi Value :-------------- "
                        + AppConstants.CANCELTAXI);
                HttpPost httppost = new HttpPost(AppConstants.CANCELTAXI);
                jsonObj = new JSONObject();
                jsonObj.put("uid", AppPreferences.getCustomerId(TaxiOntheWay_Activity.this));
                jsonObj.put("trip_id", AppPreferences.getTripId(TaxiOntheWay_Activity.this));
                jsonObj.put("canceltaxirequest", allbeans.getCanceltaxirequest());
                jsonObj.put("account_type", "99");
                jsonObj.put("dateTime", currentDateTimeString);
                jsonObj.put("latitude", AppPreferences.getLatutude(TaxiOntheWay_Activity.this));
                jsonObj.put("longitude", AppPreferences.getLongitude(TaxiOntheWay_Activity.this));


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

                        if (jsonObj.getString("status").equalsIgnoreCase("200")) {
                            System.out
                                    .println("--------- message 200 got ----------");
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
                Intent intent = new Intent(TaxiOntheWay_Activity.this, DrawerMainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        }

    }

    private void initiatePopupWindowsendmesage() {
        try {
            dialog = new Dialog(TaxiOntheWay_Activity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.sendmesssage_popup);


            Button mbtn_sendmesssage = (Button) dialog.findViewById(R.id.btn_acceptor);
            Button mbtn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
            rd4 = (RadioButton) dialog.findViewById(R.id.radioButton1);
            rd5 = (RadioButton) dialog.findViewById(R.id.radioButton2);
            rd6 = (RadioButton) dialog.findViewById(R.id.radioButton3);
            mcross = (ImageButton) dialog.findViewById(R.id.cross);
            txt_header = (TextView) dialog.findViewById(R.id.popup_text);
            mcross.setOnClickListener(cancle_btn_click_listener);
            mbtn_cancel.setOnClickListener(cancle_btn_click_listener);
            Typeface tf = Typeface.createFromAsset(this.getAssets(),
                    "Montserrat-Regular.ttf");
            rd4.setTypeface(tf);
            rd5.setTypeface(tf);
            rd6.setTypeface(tf);
            mbtn_sendmesssage.setTypeface(tf);
            txt_header.setTypeface(tf);
            mbtn_cancel.setTypeface(tf);
            mbtn_sendmesssage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    if (rd4.isChecked()) {
                        sendmessage = getResources().getString(R.string.prompt_message_one);

                    } else if (rd5.isChecked()) {
                        sendmessage = getResources().getString(R.string.prompt_message_two);

                    } else if (rd6.isChecked()) {
                        sendmessage = getResources().getString(R.string.prompt_message_three);

                    }


                    Allbeans allbeans = new Allbeans();

                    allbeans.setSendmessage(sendmessage);

                    new SendmessageAsynch(allbeans).execute();


                }
            });
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SendmessageAsynch extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;
        Allbeans allbeans;
        private JSONObject jsonObj;
        ArrayList<Integer> catogariesid;
        private int status = 0;

        public SendmessageAsynch(Allbeans allbeans) {
            this.allbeans = allbeans;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(TaxiOntheWay_Activity.this);
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
                System.out.println("SEND MESSAGE VALUE:------------"
                        + AppConstants.SENDMESSAGE);
                HttpPost httppost = new HttpPost(AppConstants.SENDMESSAGE);
                jsonObj = new JSONObject();
                jsonObj.put("uid", AppPreferences.getCustomerId(TaxiOntheWay_Activity.this));
                jsonObj.put("message", allbeans.getSendmessage());
                jsonObj.put("account_type", "99");
                jsonObj.put("tripId", AppPreferences.getTripId(TaxiOntheWay_Activity.this));
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
                            System.out
                                    .println("--------- message 200 got ----------");
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
                dialog.dismiss();
//                Intent intent = new Intent(TaxiOntheWay_Activity.this, MainActivity.class);
//                startActivity(intent);
                //      finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        }

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
    public void onBackPressed() {

    }

    public void caceldialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(TaxiOntheWay_Activity.this);
//builder.setTitle(getString(R.string.cancel_trip));
//builder.setIcon(R.drawable.ic_launcher);
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.dialog_heading, null);
        TextView textView = (TextView) header.findViewById(R.id.text);
        ImageView icon = (ImageView) header.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.ic_launcher);
        textView.setText("Taxi On The Way");
        builder.setCustomTitle(header);
        builder.setCancelable(false);


        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        AppPreferences.setTripId(DrawerMainActivity.this, "");
//                        AppPreferences.setDriverId(DrawerMainActivity.this, "");
                        dialog.dismiss();
                    }
                });

        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taxiOntheWay_activity_instance = null;
        finish();
    }
    private void animateMarker(final Marker marker, final LatLng toPosition,
                               final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = map.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng =  proj.fromScreenLocation(startPoint);//marker1.getPosition();
        final long duration = 600;

        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
            }
        });



    }


}
