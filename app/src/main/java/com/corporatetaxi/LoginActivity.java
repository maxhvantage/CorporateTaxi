package com.corporatetaxi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.corporatetaxi.gcmclasses.Config;
import com.corporatetaxi.utils.AppConstants;
import com.google.android.gcm.GCMRegistrar;
import com.corporatetaxi.utils.AppPreferences;
import com.corporatetaxi.utils.ServiceHandler;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eyon on 11/25/2015.
 */
public class LoginActivity extends AppCompatActivity {
    Button btn_loginsend;
    EditText medit_email, medit_paasword;
    public static String email, pasword;
    ProgressDialog asynDialog;
    public static String name = "";
    public static String imei = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_loginsend = (Button) findViewById(R.id.btn_login);
        medit_email = (EditText) findViewById(R.id.email_login);
        medit_paasword = (EditText) findViewById(R.id.password_login);


        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Montserrat-Regular.ttf");
        btn_loginsend.setTypeface(tf);
        medit_email.setTypeface(tf);
        medit_paasword.setTypeface(tf);


//        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        upArrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        btn_loginsend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Intent intent = new Intent(LoginActivity.this, DrawerMainActivity.class);
//                startActivity(intent);
                medit_email.setError(null);
                medit_paasword.setError(null);

                if (medit_email.length() > 0) {
                    email = medit_email.getText().toString();
                    if (medit_paasword.length() > 0) {
                        pasword = medit_paasword.getText().toString();
                        new Login(LoginActivity.this).execute(
                                medit_email.getText().toString(), medit_paasword
                                        .getText().toString());
                    } else if (pasword.length() == 0) {
                        medit_paasword.setError(getResources().getString(
                                R.string.error_field_required));
                    } else if (email.length() == 0) {
                        medit_email.setError(getResources().getString(
                                R.string.error_field_required));
                    }
                }
            }
        });

        getDeviceId();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.corporatetaxi/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.corporatetaxi/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    class Login extends AsyncTask<String, Void, String> {

        String status = "", result = "", loginId = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            asynDialog = new ProgressDialog(LoginActivity.this);
            asynDialog.setTitle("Login");
            asynDialog.setMessage(getResources().getString(R.string.progressmsg));
            asynDialog.show();
            super.onPreExecute();
        }

        Context context;

        public Login(Context context) {
            this.context = context;

        }

        @Override
        protected String doInBackground(String... params) {
            ServiceHandler serviceHandler = new ServiceHandler();
            List<NameValuePair> values = new ArrayList<>();
            values.add(new BasicNameValuePair("email", params[0]));
            values.add(new BasicNameValuePair("password", params[1]));
            values.add(new BasicNameValuePair("device_id", getDeviceId()));
            values.add(new BasicNameValuePair("latitude", AppPreferences.getLatutude(LoginActivity.this)));
            values.add(new BasicNameValuePair("longitude", AppPreferences.getLongitude(LoginActivity.this)));


            Log.d("login_value", values.toString());
            String login_json = serviceHandler.makeServiceCall(
                    AppConstants.LOGIN, ServiceHandler.POST, values);
            if (login_json != null) {
                Log.d("login_json", login_json.toString());
                try {
                    JSONObject jsonObject = new JSONObject(login_json);
                    status = jsonObject.getString("status");
                    result = jsonObject.getString("result");


                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        loginId = jsonObject2.getString("id");
                        AppPreferences
                                .setCustomerId(context, Integer.parseInt(jsonObject2.getString("id")));
                        AppPreferences
                                .setId(context, jsonObject2.getString("email"));
                        Log.d("email------", AppPreferences.getId(LoginActivity.this));
                        AppPreferences
                                .setUsername(context, jsonObject2.getString("username"));
                        Log.d("name---------", AppPreferences.getUsername(LoginActivity.this));
                        AppPreferences
                                .setMobileuser(context, jsonObject2.getString("contact_no"));

                        AppPreferences
                                .setCompanyname(context, jsonObject2.getString("companyName"));
                        Log.d("company---------", AppPreferences.getCompanyname(LoginActivity.this));
                        AppPreferences
                                .setAccounttype(context, jsonObject2.getString("account_type"));
                        Log.d("AccountType---------", AppPreferences.getAccounttype(LoginActivity.this));
                        AppPreferences
                                .setUseramount(context, jsonObject2.getString("amount"));
                        Log.d("amountUser---", AppPreferences.getUseramount(LoginActivity.this));



                        Log.d("loginId", loginId);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            medit_paasword.setText("");
           // finish();
            asynDialog.dismiss();


            Log.d("data from server", status + "   " + loginId);
            if (status.equals("200") && !loginId.equals("")) {
                Intent mintent_home = new Intent(LoginActivity.this,
                        DrawerMainActivity.class);
                startActivity(mintent_home);
                 finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.id_paasword_no), Snackbar.LENGTH_LONG)
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }
}
