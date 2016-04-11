package com.corporatetaxi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.corporatetaxi.adapter.TripHistoryAdapter;
import com.corporatetaxi.beans.Allbeans;
import com.corporatetaxi.database.DBAdapter;
import com.corporatetaxi.utils.AppConstants;
import com.corporatetaxi.utils.AppPreferences;

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
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TripHistory_Activity extends AppCompatActivity {

    HashMap<String, ArrayList<Allbeans>> tripHistoryList;
    TripHistoryAdapter tripHistoryAdapter;
    TripHistory_Activity instance = null;
    ArrayList<String> monthList;

    ExpandableListView trip_listview;
    Dialog dialog;


    EditText mfromdate, mtodate;
    Button btn_fielter;
    Calendar mCalendar = Calendar.getInstance();
    String ed_datefrom = "";
    String ed_dateto = "";
    public static String fromdate, todate;
    PopupWindow pwindo;
    ImageButton cross;
    TextView textheader;
    // DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getString(R.string.title_activity_trip_history_);
        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle(title);
        //   db = new DBAdapter(TripHistory_Activity.this);
        trip_listview = (ExpandableListView) findViewById(R.id.trip_lv);
        tripHistoryList = new HashMap<String, ArrayList<Allbeans>>();
        monthList = new ArrayList<String>();
        tripHistoryAdapter = new TripHistoryAdapter(TripHistory_Activity.this, monthList, tripHistoryList);
        trip_listview.setAdapter(tripHistoryAdapter);
        // updateList("", "");
        Allbeans allbeans = new Allbeans();
        allbeans.setFromdate("");
        allbeans.setTodate("");
        new TripHistoryTask(allbeans).execute();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        trip_listview.setIndicatorBounds(width - GetDipsFromPixel(55), width - GetDipsFromPixel(18));

        trip_listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(TripHistory_Activity.this, TripForDetail_Activity.class);
                intent.putExtra("tripHistory", (Allbeans) tripHistoryAdapter.getChild(groupPosition, childPosition));
                startActivity(intent);
                return false;
            }
        });


        /////back arrow ////////////
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorblack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    public int GetDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.triphistory, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            Intent intent = new Intent(TripHistory_Activity.this, DrawerMainActivity.class);
            startActivity(intent);
            finish();
            return true;

        }
        if (id == R.id.action_fielter) {
            initiatePopupWindowfieltervalue();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // /////////////TRIPHISTORY////////////////////////////
    public class TripHistoryTask extends AsyncTask<String, Void, String> {
        ArrayList<Allbeans> tripList;
        ArrayList<String> monthArrayList;
        String fromdate, todate;
        String networkFlag = "false";
        private JSONObject jsonObj;
        Allbeans allbeans;
        private int status = 0;
        private ProgressDialog mProgressDialog;
        public ArrayList<HashMap<String, String>> data;

        public TripHistoryTask(Allbeans allbeans) {

            this.allbeans = allbeans;
            mProgressDialog = new ProgressDialog(TripHistory_Activity.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setTitle(getResources().getString(R.string.progressmsg));
            mProgressDialog.setMessage("Loading...");
            monthArrayList = new ArrayList<String>();
            //   db.deleteTripHistory();
        }

        public TripHistoryTask(String fromDate, String toDate) {
            monthArrayList = new ArrayList<String>();
            //mProgressDialog.showProcessDialog(TripHistoryActivity.this, "updating...");
            // db.deleteTripHistory();
            this.fromdate = fromDate;
            this.todate = toDate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("nik", "registration pre method");
            mProgressDialog.show();
        }

        protected String doInBackground(String... params) {
            try {
                HttpParams httpParameters = new BasicHttpParams();
                ConnManagerParams.setTimeout(httpParameters,
                        AppConstants.NETWORK_TIMEOUT_CONSTANT);
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        AppConstants.NETWORK_CONNECTION_TIMEOUT_CONSTANT);
                HttpConnectionParams.setSoTimeout(httpParameters,
                        AppConstants.NETWORK_SOCKET_TIMEOUT_CONSTANT);

                HttpClient httpclient = new DefaultHttpClient(httpParameters);
                HttpPost httppost = new HttpPost(AppConstants.TRIPHISTORY);
                JSONArray jsonArray = new JSONArray();
                StringEntity se = null;
                jsonObj = new JSONObject();

                jsonObj.put("uid", AppPreferences.getCustomerId(TripHistory_Activity.this));
                jsonObj.put("latitude", AppPreferences.getLatutude(TripHistory_Activity.this));
                jsonObj.put("longitude", AppPreferences.getLongitude(TripHistory_Activity.this));
                jsonObj.put("fromDate", fromdate);
                jsonObj.put("account_type", "99");
                jsonObj.put("toDate", todate);
                jsonArray.put(jsonObj);

                try {
                    se = new StringEntity(jsonArray.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println("json : " + jsonArray.toString());
                Log.v("json sent : ", jsonArray.toString());
                httppost.setEntity(se);

                try {
                    se = new StringEntity(jsonArray.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println("JSON sent is :---------- " + jsonArray.toString());
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
                System.out.println("jsonString response is : " + jsonString);
                if (jsonString != null && jsonString.length() > 0) {
                    if (jsonString.contains("status")) {
                        JSONTokener tokener = new JSONTokener(jsonString);

                        JSONObject finalResult = new JSONObject(tokener);// jsonString
                        System.out.println("JSON response is : " + finalResult);

                        if (finalResult.getString("status").equalsIgnoreCase("200")) {

                            System.out.println("--------- message 200 got ----------");
                            status = 200;

                            JSONArray monthArray = finalResult.getJSONArray("month");
                            for (int a = 0; a < monthArray.length(); a++) {
                                JSONObject monthObj = monthArray.getJSONObject(a);
                                monthArrayList.add(monthObj.getString("name"));
                            }
                            JSONArray jsonArrayy = finalResult.getJSONArray("result");

                            for (int i = 0; i < jsonArrayy.length(); i++) {

                                JSONObject jsonObj = jsonArrayy.getJSONObject(i);
                                for (int j = 0; j < jsonObj.length(); j++) {
                                    JSONArray jsonArray2 = jsonObj.getJSONArray(monthArrayList.get(j));

                                    tripList = new ArrayList<>();
                                    for (int k = 0; k < jsonArray2.length(); k++) {
                                        JSONObject object1 = jsonArray2.getJSONObject(k);
                                        Allbeans trip = new Allbeans();
                                        trip.setTriphistoryid(Integer.parseInt(object1.getString("id")));
                                        trip.setTripdate(object1.getString("tripdatetime"));
                                        trip.setDrivermobilenumber(object1.getString("contact_number"));
                                        trip.setDistance(object1.getString("trip_distance"));
                                        //  trip.setTriptime(object1.getString("tripdatetime"));
                                        trip.setTripprice(object1.getString("trip_ammount"));
                                        trip.setCompanyname(object1.getString("companyname"));
                                        if (!object1.getString("customer_rating").equalsIgnoreCase("")) {
                                            trip.setRatingvalue(new Float(object1.getString("customer_rating")));
                                        } else {
                                            trip.setRatingvalue(0);
                                        }

                                        //   trip.setTripamount(object1.getString("trip_ammount"));
                                        trip.setTripstatus(object1.getString("status"));
                                        trip.setComment(object1.getString("user_comment"));
                                        //  trip.setTripType(object1.getString("tripType"));
                                        trip.setDrivername(object1.getString("driverName"));
                                        trip.setDriverimage(object1.getString("driverImage"));
                                        trip.setTaxinumber(object1.getString("taxiNumber"));
                                        trip.setSourcerequest(object1.getString("source_address"));
                                        trip.setDestinationresuest(object1.getString("destination_address"));

                                        if(object1.getString("source_latitude").equalsIgnoreCase("")){
                                            trip.setSourcelatitute(0.0);
                                        }else{
                                            trip.setSourcelatitute(new Double(object1.getString("source_latitude")));
                                        }
                                        if(object1.getString("source_longitude").equalsIgnoreCase("")){
                                            trip.setSourcelongitude(0.0);
                                        }else{
                                            trip.setSourcelongitude(new Double(object1.getString("source_longitude")));
                                        }
                                        if(object1.getString("destination_latitude").equalsIgnoreCase("")){
                                            trip.setDestinationlatitude(0.0);
                                        }else{
                                            trip.setDestinationlatitude(new Double(object1.getString("destination_latitude")));
                                        }
                                        if(object1.getString("destination_longitude").equalsIgnoreCase("")){
                                            trip.setDestinationlongitude(0.0);
                                        }else{
                                            trip.setDestinationlongitude(new Double(object1.getString("destination_longitude")));
                                        }





                                        Log.d("dataImage.", trip.getDriverimage());
                                        Log.d("tripstatus", trip.getTripstatus());

                                        //  db.insertTripHistory(trip);
                                        tripList.add(trip);
                                        //    monthList.add(monthArrayList.get(j));

                                    }
                                    monthList.add(monthArrayList.get(j));
                                    tripHistoryList.put(monthList.get(j), tripList);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // tripHistoryAdapter.updateResult(monthList, tripHistoryList);
                                        }
                                    });
                                }

                            }

                            return jsonString;
                        } else if (finalResult.getString("status")
                                .equalsIgnoreCase("400")) {
                            status = 400;
                            return "";
                        } else if (finalResult.getString("status")
                                .equalsIgnoreCase("500")) {

                            status = 500;
                            return "";
                        } else {
                            return "";
                        }
                    }
                }

            } catch (ConnectTimeoutException e) {
                networkFlag = "false";
            } catch (SocketTimeoutException e) {
                networkFlag = "false";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return networkFlag;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //updateList("", "");
            try {
                mProgressDialog.dismiss();
                System.out.println("status--" + status);

                if (status == 200) {
                    System.out.println("200 dialog if");
                    tripHistoryAdapter.updateResult(monthList, tripHistoryList);
                    tripHistoryAdapter.notifyDataSetChanged();

                }else if(status==400){
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.no_data), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    ///////////////////filter datetime/////////////////
    private void setfromdateonview() {
        // TODO Auto-generated method stub
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updatelabel();
            }

            private void updatelabel() {
                // TODO Auto-generated method stub
                String myformat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myformat);
                mfromdate.setText(sdf.format(mCalendar.getTime()));

                String myformat1 = "yyyy-MM-dd";
                SimpleDateFormat sdf1 = new SimpleDateFormat(myformat1);
                ed_datefrom = sdf1.format(mCalendar.getTime());

            }
        };
        mfromdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                new DatePickerDialog(TripHistory_Activity.this, date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }

    private void settodateonview() {
        // TODO Auto-generated method stub
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updatelabel();
            }

            private void updatelabel() {
                // TODO Auto-generated method stub
                String myformat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myformat);
                mtodate.setText(sdf.format(mCalendar.getTime()));

                String myformat1 = "yyyy-MM-dd";
                SimpleDateFormat sdf1 = new SimpleDateFormat(myformat1);
                ed_dateto = sdf1.format(mCalendar.getTime());

            }
        };
        mtodate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                new DatePickerDialog(TripHistory_Activity.this, date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }


    private View.OnClickListener cancle_btn_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            dialog.dismiss();
        }
    };

    private void initiatePopupWindowfieltervalue() {
        try {
            dialog = new Dialog(TripHistory_Activity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.fielter_popup);

            cross = (ImageButton) dialog.findViewById(R.id.cross);
            cross.setOnClickListener(cancle_btn_click_listener);
            mfromdate = (EditText) dialog.findViewById(R.id.from_date);
            mtodate = (EditText) dialog.findViewById(R.id.to_date);
            btn_fielter = (Button) dialog.findViewById(R.id.btn_filter);
            setfromdateonview();
            settodateonview();

            textheader = (TextView) dialog.findViewById(R.id.popup_text);
            Typeface tf = Typeface.createFromAsset(this.getAssets(),
                    "Montserrat-Regular.ttf");
            mfromdate.setTypeface(tf);
            mtodate.setTypeface(tf);
            btn_fielter.setTypeface(tf);
            textheader.setTypeface(tf);
            btn_fielter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mfromdate.setError(null);
                    mtodate.setError(null);
                    fromdate = ed_datefrom;
                    todate = ed_dateto;
                    monthList.clear();
                    tripHistoryList.clear();

                    //   updateList(fromdate, todate);

                    Allbeans allbeans = new Allbeans();
                    allbeans.setFromdate(fromdate);
                    allbeans.setTodate(todate);
                    new TripHistoryTask(allbeans).execute();
                    dialog.dismiss();

                }
            });
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void updateList(String fromDate, String toDate) {
//        monthList.clear();
//        tripHistoryList.clear();
//
//        if (!fromDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("")) {
//            ArrayList<Allbeans> trips = db.getTripHistoryFilter(fromDate, toDate);
//            for (int i = 0; i < trips.size(); i++) {
//                if (!monthList.contains(trips.get(i).getTripmonth())) {
//                    monthList.add(trips.get(i).getTripmonth());
//                    tripHistoryList.put(trips.get(i).getTripmonth(), db.getTripHistoryByMonthWithDate(trips.get(i).getTripmonth(), fromDate, toDate));
//                }
//            }
//        } else {
//            ArrayList<Allbeans> trips = db.getTripHistory();
//            for (int i = 0; i < trips.size(); i++) {
//                if (!monthList.contains(trips.get(i).getTripmonth())) {
//                    monthList.add(trips.get(i).getTripmonth());
//                    tripHistoryList.put(trips.get(i).getTripmonth(), db.getTripHistoryByMonth(trips.get(i).getTripmonth()));
//                }
//            }
//        }
//        tripHistoryAdapter.updateResult(monthList, tripHistoryList);
//
//    }
}
