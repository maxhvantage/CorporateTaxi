package com.corporatetaxi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.corporatetaxi.beans.Allbeans;
import com.corporatetaxi.gcmclasses.Config;
import com.corporatetaxi.utils.AppConstants;
import com.corporatetaxi.utils.AppPreferences;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


public class RegisterActivity extends AppCompatActivity {


    EditText mname, mEmailView, mphone, mPasswordView, mrepeatpaasword;
    Button mregister;
    public static String name, email, phone, password, repassword;
    public static String imei = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerr);

        mname = (EditText) findViewById(R.id.name);
        mEmailView = (EditText) findViewById(R.id.email);
        mphone = (EditText) findViewById(R.id.phone);

        mregister = (Button) findViewById(R.id.registerbtn);
        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Montserrat-Regular.ttf");

        mname.setTypeface(tf);
        mEmailView.setTypeface(tf);
        mphone.setTypeface(tf);

        mregister.setTypeface(tf);

        getDeviceId();

        mregister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mname.setError(null);
                mEmailView.setError(null);
                mphone.setError(null);


                name = mname.getText().toString().trim();
                email = mEmailView.getText().toString().trim();
                phone = mphone.getText().toString().trim();


                if (name.length() == 0) {
                    mname.setError(getResources().getString(
                            R.string.error_field_required));
                } else if (email.length() == 0) {
                    mEmailView.setError(getResources().getString(
                            R.string.error_field_required));
                } else if (!email.contains("@")) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                } else if (!email.contains(".")) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                } else if (phone.length() == 0) {
                    mphone.setError(getResources().getString(
                            R.string.error_field_required));
                } else if (phone.length() < 8 || phone.length() > 12) {
                    mphone.setError(getResources().getString(
                            R.string.error_field_required_length));
                } else {


                    Allbeans allbeans = new Allbeans();

                    allbeans.setName(name);
                    allbeans.setEmail(email);
                    allbeans.setPhone(phone);
                    new RegisterAsynch(allbeans).execute();
                }

            }
        });


    }

    private class RegisterAsynch extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;
        Allbeans allbeans;
        private JSONObject jsonObj;
        ArrayList<Integer> catogariesid;
        private int status = 0;
        String loginId = "";

        public RegisterAsynch(Allbeans allbeans) {
            this.allbeans = allbeans;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(RegisterActivity.this);
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
                System.out.println("registartion value : ------------ "
                        + AppConstants.REGISTRATION);
                HttpPost httppost = new HttpPost(AppConstants.REGISTRATION);
                jsonObj = new JSONObject();
                jsonObj.put("name", allbeans.getName());
                jsonObj.put("email", allbeans.getEmail());
                jsonObj.put("mobile", allbeans.getPhone());
                jsonObj.put("regId", getDeviceId());
                jsonObj.put("latitute", AppPreferences.getLatutude(RegisterActivity.this));
                jsonObj.put("longitude", AppPreferences.getLongitude(RegisterActivity.this));

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
                System.out.println("JSONString response is : " + jsonString);
                if (jsonString != null) {
                    if (jsonString.contains("result")) {
                        JSONObject jsonObj = new JSONObject(jsonString);
                        JSONObject jsonObject = jsonObj.getJSONObject("result");
                        loginId = jsonObject.getString("id");
                        AppPreferences
                                .setCustomerId(getApplicationContext(), Integer.parseInt(jsonObject.getString("id")));
                        AppPreferences
                                .setId(getApplicationContext(), jsonObject.getString("email"));
                        Log.d("email------", AppPreferences.getId(RegisterActivity.this));
                        AppPreferences
                                .setUsername(getApplicationContext(), jsonObject.getString("name"));
                        Log.d("name---------", AppPreferences.getUsername(RegisterActivity.this));
                        AppPreferences
                                .setMobileuser(getApplicationContext(), jsonObject.getString("mobile"));

                        Log.d("loginId", loginId);


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
                Toast.makeText(getApplicationContext(), "Registration successfully", Toast.LENGTH_LONG).show();
                Intent nextintent = new Intent(RegisterActivity.this, DrawerMainActivity.class);
                startActivity(nextintent);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Check your network connection", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        }

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

