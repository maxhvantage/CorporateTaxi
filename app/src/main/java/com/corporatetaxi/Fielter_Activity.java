package com.corporatetaxi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Fielter_Activity extends AppCompatActivity {
    EditText mfromdate, mtodate;
    Button btn_fielter;
    static final int DATE_DIALOG_ID = 999;
    Calendar mCalendar = Calendar.getInstance();
    String ed_datefrom = "";
    String ed_dateto = "";
    String fromdate, todate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fielter_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getString(R.string.title_activity_fielter_);
        getSupportActionBar().setTitle(title);
        mfromdate = (EditText) findViewById(R.id.from_date);
        mtodate = (EditText) findViewById(R.id.to_date);
        btn_fielter = (Button) findViewById(R.id.btn_filter);
        setfromdateonview();
        settodateonview();
        btn_fielter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextintent = new Intent(Fielter_Activity.this, TripHistory_Activity.class);
                Toast.makeText(getApplicationContext(), "Filter request sent successfully", Toast.LENGTH_LONG).show();
                startActivity(nextintent);

//                mfromdate.setError(null);
//                mtodate.setError(null);
//                fromdate = ed_datefrom;
//                todate = ed_dateto;
//                Allbeans allbeans = new Allbeans();
//                allbeans.setFromdate(fromdate);
//                allbeans.setTodate(todate);
//                new FilterAsynchtask(allbeans).execute();


            }
        });


    }


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
                new DatePickerDialog(Fielter_Activity.this, date, mCalendar
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
                new DatePickerDialog(Fielter_Activity.this, date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

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
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class FilterAsynchtask extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;
        Allbeans allbeans;
        private JSONObject jsonObj;
        ArrayList<Integer> catogariesid;
        private int status = 0;

        public FilterAsynchtask(Allbeans allbeans) {
            this.allbeans = allbeans;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Fielter_Activity.this);
            mProgressDialog.setMessage("Please wait...");
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
                System.out.println("Fielter value : ------------ "
                        + AppConstants.FIELTER);
                HttpPost httppost = new HttpPost(AppConstants.FIELTER);
                jsonObj = new JSONObject();
                jsonObj.put("fromdate", allbeans.getFromdate());
                jsonObj.put("todate", allbeans.getTodate());
                jsonObj.put("latitute", AppPreferences.getLatutude(Fielter_Activity.this));
                jsonObj.put("longitude", AppPreferences.getLongitude(Fielter_Activity.this));
                jsonObj.put("customer_id", AppPreferences.getCustomerId(Fielter_Activity.this));

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObj);

                Log.d("json value:---------", jsonArray.toString());


                StringEntity se = null;

                try {
                    se = new StringEntity(jsonArray.toString());

                    se.setContentEncoding(new BasicHeader(
                            HTTP.CONTENT_ENCODING, "UTF-8"));
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
                System.out.println("JSON RESPONSE IS : ------------------" + jsonString);
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
                Toast.makeText(getApplicationContext(), "Filter request sent successfully", Toast.LENGTH_LONG).show();
                Intent nextintent = new Intent(Fielter_Activity.this, TripHistory_Activity.class);
                startActivity(nextintent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Check network connection", Toast.LENGTH_LONG).show();

            }
        }

    }
}
