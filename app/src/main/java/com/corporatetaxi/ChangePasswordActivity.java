package com.corporatetaxi;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;

/**
 * Created by Eyon on 11/9/2015.
 */
public class ChangePasswordActivity extends AppCompatActivity {
    EditText ed_oldpaas, ed_newpaas, ed_repeatpaas;
    Button btn_changepaasword;
    String oldPassword, newPassword, repassword;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getString(R.string.title_activity_changepassword_);
        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle(title);
        ed_oldpaas = (EditText) findViewById(R.id.oldpaasword);
        ed_newpaas = (EditText) findViewById(R.id.newpaasword);
        ed_repeatpaas = (EditText) findViewById(R.id.repeatpassword);
        btn_changepaasword = (Button) findViewById(R.id.btn_change_pass);

        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Montserrat-Regular.ttf");
        ed_oldpaas.setTypeface(tf);
        ed_newpaas.setTypeface(tf);
        ed_repeatpaas.setTypeface(tf);
        btn_changepaasword.setTypeface(tf);
        btn_changepaasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ed_oldpaas.setError(null);
                ed_newpaas.setError(null);
                ed_repeatpaas.setError(null);

                oldPassword = ed_oldpaas.getText().toString().trim();
                newPassword = ed_newpaas.getText().toString().trim();
                repassword = ed_repeatpaas.getText().toString().trim();
                if (oldPassword.length() == 0) {
                    ed_oldpaas.setError(getResources().getString(
                            R.string.error_field_required));
                } else if (newPassword.length() == 0) {
                    ed_newpaas.setError(getResources().getString(
                            R.string.error_field_required));
                } else if (repassword.length() == 0) {
                    ed_newpaas.setError(getResources().getString(
                            R.string.error_field_required));
                } else if (!newPassword.equals(repassword)) {
                    ed_repeatpaas.setError(getResources().getString(
                            R.string.error_dont_match_password));

                } else {

                    Allbeans allbeans = new Allbeans();
                    allbeans.setPassword(oldPassword);
                    allbeans.setNewpassword(newPassword);

                    new ChangepaswordAsynch(allbeans).execute();
                }


            }
        });
        /////back arrow ////////////
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

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


    private class ChangepaswordAsynch extends AsyncTask<Void, Void, String> {

        Allbeans allbeans;
        private JSONObject jsonObj;
        ArrayList<Integer> catogariesid;
        private int status = 0;

        public ChangepaswordAsynch(Allbeans allbeans) {
            this.allbeans = allbeans;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ChangePasswordActivity.this);
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
                System.out.println("changepasword value : ------------ "
                        + AppConstants.CHANGEPAASWORD);
                HttpPost httppost = new HttpPost(AppConstants.CHANGEPAASWORD);
                jsonObj = new JSONObject();
                jsonObj.put("uid", AppPreferences.getCustomerId(ChangePasswordActivity.this));
                jsonObj.put("account_type", "99");
                jsonObj.put("oldPassword", allbeans.getPassword());
                jsonObj.put("newPassword", allbeans.getNewpassword());
                jsonObj.put("latitute", AppPreferences.getLatutude(ChangePasswordActivity.this));
                jsonObj.put("longitude", AppPreferences.getLongitude(ChangePasswordActivity.this));

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
                AppPreferences.setCustomerId(getApplicationContext(), 0);
                AppPreferences.setId(getApplicationContext(), "");
                AppPreferences.setMobileuser(getApplicationContext(), "");
                AppPreferences.setUsername(getApplicationContext(), "");

                Intent nextintent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                nextintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(nextintent);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        }

    }

}
