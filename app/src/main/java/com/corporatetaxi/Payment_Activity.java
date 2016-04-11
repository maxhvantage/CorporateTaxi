package com.corporatetaxi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.corporatetaxi.beans.Allbeans;
import com.corporatetaxi.utils.AppConstants;
import com.corporatetaxi.utils.AppPreferences;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
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
import java.util.ArrayList;

/**
 * Created by Eyon on 12/17/2015.
 */
public class Payment_Activity extends AppCompatActivity {
    TextView textheader, txtheadcompany, txtheadcompanytwo, txtheaddestination, txtheadpay, mtaxicompany, mtxtmycompany, mtxtdestination, matxtfare;
  public static Button mbtnpaycreadit, mbtncash, panicconfirm, btn_confirm;
    FloatingActionsMenu fab_menu;
    String comment, panictaxirequest, canceltaxirequest;
    Dialog dialog;
    RadioButton rd1, rd2, rd3;
    ImageButton cross;
    public static String drivermobile;
    public static String drivername;
    public static String drivercompanyname = "";
    public static String drivertexinumber = "";
    public static String drivertaxiname = "";
    public static String driverimage = "";
    public static String tripamount = "";
    public static String tripdestination = "";
    public static String corporateusercompany = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentmode);
        Typeface tf = Typeface.createFromAsset(Payment_Activity.this.getAssets(),
                "Montserrat-Regular.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        String title = getString(R.string.action_payment);
        getSupportActionBar().setTitle(title);
        AppPreferences.setApprequestTaxiScreen(Payment_Activity.this, false);


        fab_menu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        FloatingActionButton fab_panic = (FloatingActionButton) findViewById(R.id.fab_panic);
        FloatingActionButton fab_cancel = (FloatingActionButton) findViewById(R.id.fab_cancel);
        txtheadcompany = (TextView) findViewById(R.id.mycompanyhead);
        txtheadcompanytwo = (TextView) findViewById(R.id.taxicompanyhead);
        txtheaddestination = (TextView) findViewById(R.id.destinationhead);
        txtheadpay = (TextView) findViewById(R.id.payhead);
        mtxtmycompany = (TextView) findViewById(R.id.mycompany_text);
        mtaxicompany = (TextView) findViewById(R.id.taxicompany_text);
        mtxtdestination = (TextView) findViewById(R.id.desti_text);
        matxtfare = (TextView) findViewById(R.id.payamount_txt);
        mbtnpaycreadit = (Button) findViewById(R.id.btn_pay);
        mbtncash = (Button) findViewById(R.id.btn_paycash);


        txtheadcompany.setTypeface(tf);
        txtheadcompanytwo.setTypeface(tf);
        txtheaddestination.setTypeface(tf);
        txtheadpay.setTypeface(tf);
        mtaxicompany.setTypeface(tf);
        mtxtmycompany.setTypeface(tf);
        mtxtdestination.setTypeface(tf);
        matxtfare.setTypeface(tf);
        mbtnpaycreadit.setTypeface(tf);
        mbtncash.setTypeface(tf);


        Intent intent = getIntent();

        drivercompanyname = intent.getStringExtra("driver_companyname");
        tripamount = intent.getStringExtra("trip_amount");
        tripdestination = intent.getStringExtra("trip_destination");
        corporateusercompany = intent.getStringExtra("user_company");
        driverimage = intent.getStringExtra("driver_image");
        drivername = intent.getStringExtra("drver_name");
        drivermobile = intent.getStringExtra("driver_mobile");
        drivertaxiname = intent.getStringExtra("driver_taxinumber");
        drivertexinumber = intent.getStringExtra("driver_taxiname");

       /* mtxtmycompany.setText(corporateusercompany);
        mtaxicompany.setText(drivercompanyname);
        mtxtdestination.setText(tripdestination);
        matxtfare.setText(tripamount);
*/

        String data = intent.getStringExtra("data");
        Log.d("datavalue", data + "");
        if (data != null) {

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
                drivermobile = jsonObject.getString("mobile");
                drivername = jsonObject.getString("username");
                drivercompanyname = jsonObject.getString("taxicompany");
                drivertaxiname = jsonObject.getString("vehicalname");
                drivertexinumber = jsonObject.getString("vehicle_number");
                corporateusercompany = jsonObject.getString("corporatecompany");
                tripamount = jsonObject.getString("trip_amount");
                driverimage = jsonObject.getString("driverImage");
                tripdestination = jsonObject.getString("destination");


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        fab_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindowcanceltaxi();

            }
        });
        fab_panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindowpanictaxi();
            }
        });
        mbtnpaycreadit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PaymentcreitAsynch().execute();
            }
        });
        mbtncash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PaymentcashAsynch().execute();

            }
        });
        mtxtmycompany.setText(corporateusercompany);
        mtaxicompany.setText(drivercompanyname);
        mtxtdestination.setText(tripdestination);
        matxtfare.setText(tripamount);

    }


    private View.OnClickListener cancle_btn_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            dialog.dismiss();
        }
    };

    private void initiatePopupWindowpanictaxi() {
        try {
            dialog = new Dialog(Payment_Activity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.panic_popup);
            cross = (ImageButton) dialog.findViewById(R.id.cross);
            cross.setOnClickListener(cancle_btn_click_listener);
            rd1 = (RadioButton) dialog.findViewById(R.id.radioButton1);
            rd2 = (RadioButton) dialog.findViewById(R.id.radioButton2);
            rd3 = (RadioButton) dialog.findViewById(R.id.radioButton3);
            panicconfirm = (Button) dialog.findViewById(R.id.btn_savechange);
            TextView txt = (TextView) dialog.findViewById(R.id.textView);
            TextView textheader = (TextView) dialog.findViewById(R.id.popup_text);
            Typeface tf = Typeface.createFromAsset(this.getAssets(),
                    "Montserrat-Regular.ttf");
            rd1.setTypeface(tf);
            rd2.setTypeface(tf);
            rd3.setTypeface(tf);
            panicconfirm.setTypeface(tf);
            txt.setTypeface(tf);
            textheader.setTypeface(tf);
            panicconfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (rd1.isChecked()) {
                        panictaxirequest = getResources().getString(R.string.prompt_rate_exp_one);

                    } else if (rd2.isChecked()) {
                        panictaxirequest = getResources().getString(R.string.prompt_rate_exp_two);

                    } else if (rd3.isChecked()) {
                        panictaxirequest = getResources().getString(R.string.prompt_rate_exp_three);

                    }


                    Allbeans allbeans = new Allbeans();

                    allbeans.setPanictaxirequest(panictaxirequest);

                    new PanicTaxiAsynch(allbeans).execute();


                }
            });
            dialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class PanicTaxiAsynch extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;
        Allbeans allbeans;
        private JSONObject jsonObj;
        private int status = 0;

        public PanicTaxiAsynch(Allbeans allbeans) {
            this.allbeans = allbeans;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Payment_Activity.this);
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
                System.out.println("PANIC Taxi Value : ------------ "
                        + AppConstants.PANIC);
                HttpPost httppost = new HttpPost(AppConstants.PANIC);
                jsonObj = new JSONObject();
                jsonObj.put("uid", AppPreferences.getCustomerId(Payment_Activity.this));
                jsonObj.put("trip_id", AppPreferences.getTripId(Payment_Activity.this));
                jsonObj.put("panictaxirequest", allbeans.getPanictaxirequest());
                jsonObj.put("account_type", "99");


                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObj);

                Log.d("json value:---------", jsonArray.toString());


                StringEntity se = null;

                try {
                    se = new StringEntity(jsonArray.toString(), "UTF-8");

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
                //Toast.makeText(getApplicationContext(), "Your panic request sent successfully", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        }

    }


    private void initiatePopupWindowcanceltaxi() {
        try {
            dialog = new Dialog(Payment_Activity.this);
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
            mProgressDialog = new ProgressDialog(Payment_Activity.this);
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
                jsonObj.put("uid", AppPreferences.getCustomerId(Payment_Activity.this));
                jsonObj.put("trip_id", AppPreferences.getTripId(Payment_Activity.this));
                jsonObj.put("canceltaxirequest", allbeans.getCanceltaxirequest());
                jsonObj.put("account_type", "99");
                jsonObj.put("latitude", AppPreferences.getLatutude(Payment_Activity.this));
                jsonObj.put("longitude", AppPreferences.getLongitude(Payment_Activity.this));


                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObj);

                Log.d("json value:---------", jsonArray.toString());


                StringEntity se = null;

                try {
                    se = new StringEntity(jsonArray.toString(), "UTF-8");

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
                Intent intent = new Intent(Payment_Activity.this, DrawerMainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
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
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private class PaymentcreitAsynch extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;
        private JSONObject jsonObj;
        ArrayList<Integer> catogariesid;
        private int status = 0;

        public PaymentcreitAsynch() {
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Payment_Activity.this);
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
                        + AppConstants.PAYMENTCHECK);
                HttpPost httppost = new HttpPost(AppConstants.PAYMENTCHECK);
                jsonObj = new JSONObject();
                jsonObj.put("uid", AppPreferences.getCustomerId(Payment_Activity.this));
                jsonObj.put("tripId", AppPreferences.getTripId(Payment_Activity.this));
                jsonObj.put("paymentMode", "credit");
                jsonObj.put("account_type", "99");
                jsonObj.put("latitude", AppPreferences.getLatutude(Payment_Activity.this));
                jsonObj.put("longitude", AppPreferences.getLongitude(Payment_Activity.this));


                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObj);

                Log.d("json value:---------", jsonArray.toString());


                StringEntity se = null;

                try {
                    se = new StringEntity(jsonArray.toString(), "UTF-8");

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
                Intent intent1 = new Intent(Payment_Activity.this, RateExperience_Activity.class);
                intent1.putExtra("driver_name", drivername);
                intent1.putExtra("driver_mobile", drivermobile);
                intent1.putExtra("driver_company", drivercompanyname);
                intent1.putExtra("driver_taxiname", drivertaxiname);
                intent1.putExtra("driver_taxinumber", drivertexinumber);
                intent1.putExtra("driver_image", driverimage);
                startActivity(intent1);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        }

    }

    private class PaymentcashAsynch extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;
        private JSONObject jsonObj;
        ArrayList<Integer> catogariesid;
        private int status = 0;

        public PaymentcashAsynch() {
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Payment_Activity.this);
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
                        + AppConstants.PAYMENTCHECK);
                HttpPost httppost = new HttpPost(AppConstants.PAYMENTCHECK);
                jsonObj = new JSONObject();
                jsonObj.put("uid", AppPreferences.getCustomerId(Payment_Activity.this));
                jsonObj.put("tripId", AppPreferences.getTripId(Payment_Activity.this));
                jsonObj.put("paymentMode", "cash");
                jsonObj.put("account_type", "99");
                jsonObj.put("latitude", AppPreferences.getLatutude(Payment_Activity.this));
                jsonObj.put("longitude", AppPreferences.getLongitude(Payment_Activity.this));


                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObj);

                Log.d("json value:---------", jsonArray.toString());


                StringEntity se = null;

                try {
                    se = new StringEntity(jsonArray.toString(), "UTF-8");

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
                Intent intent1 = new Intent(Payment_Activity.this, RateExperience_Activity.class);
                intent1.putExtra("driver_name", drivername);
                intent1.putExtra("driver_mobile", drivermobile);
                intent1.putExtra("driver_company", drivercompanyname);
                intent1.putExtra("driver_taxiname", drivertaxiname);
                intent1.putExtra("driver_taxinumber", drivertexinumber);
                intent1.putExtra("driver_image", driverimage);
                startActivity(intent1);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        }

    }
}