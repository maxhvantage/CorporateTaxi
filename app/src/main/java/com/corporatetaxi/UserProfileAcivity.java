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
import android.widget.ImageView;
import android.widget.Toast;

import com.corporatetaxi.beans.Allbeans;
import com.corporatetaxi.utils.AppConstants;
import com.corporatetaxi.utils.AppPreferences;
import com.nostra13.universalimageloader.core.ImageLoader;

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
public class UserProfileAcivity extends AppCompatActivity {
    Button mbtn_changepass, mbtn_savechange;
    EditText meditname, meditphone, meditemail;
    String editname, editphone, editemail;

    ImageView user_image;
    private static int SELECT_PICTURE = 1;
    private static String selectedImagePath1 = "";
    public static String encoded_img1;
    ImageView product_image1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getString(R.string.title_activity_myprofile);
        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle(title);
        Typeface tf = Typeface.createFromAsset(UserProfileAcivity.this.getAssets(),
                "Montserrat-Regular.ttf");
        mbtn_changepass = (Button) findViewById(R.id.btn_changepass);
        mbtn_savechange = (Button) findViewById(R.id.btn_savechange);
        meditname = (EditText) findViewById(R.id.name_tv);
        meditphone = (EditText) findViewById(R.id.phone_tv);
        meditemail = (EditText) findViewById(R.id.email_tv);
        meditname.setText(AppPreferences.getUsername(UserProfileAcivity.this));
        meditemail.setText(AppPreferences.getId(UserProfileAcivity.this));
        meditphone.setText(AppPreferences.getMobileuser(UserProfileAcivity.this));

        mbtn_changepass.setTypeface(tf);
        mbtn_savechange.setTypeface(tf);
        meditname.setTypeface(tf);
        meditphone.setTypeface(tf);
        meditemail.setTypeface(tf);




        mbtn_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileAcivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        mbtn_savechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meditname.setError(null);
                meditphone.setError(null);
                meditemail.setError(null);
                editname = meditname.getText().toString().trim();
                editphone = meditphone.getText().toString().trim();
                editemail = meditemail.getText().toString().trim();


                Allbeans allbeans = new Allbeans();
                allbeans.setEditname(editname);
                allbeans.setEditphone(editphone);
                allbeans.setEditemail(editemail);
                allbeans.setImage(encoded_img1);
                new UpdateprofileAsynch(allbeans).execute();


            }
        });
        ///back arrow ////////////
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorblack), PorterDuff.Mode.SRC_ATOP);
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
            Intent intent = new Intent(UserProfileAcivity.this, DrawerMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private class UpdateprofileAsynch extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;
        Allbeans allbeans;
        private JSONObject jsonObj;
        ArrayList<Integer> catogariesid;
        private int status = 0;
        String user_profile_pic;
        String loginId = "";

        public UpdateprofileAsynch(Allbeans allbeans) {
            this.allbeans = allbeans;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(UserProfileAcivity.this);
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
                System.out.println("update userprofile value : ------------ "
                        + AppConstants.UPDATEPROFILE);
                HttpPost httppost = new HttpPost(AppConstants.UPDATEPROFILE);
                jsonObj = new JSONObject();
                jsonObj.put("uid", AppPreferences.getCustomerId(UserProfileAcivity.this));
                jsonObj.put("editname", allbeans.getEditname());
                jsonObj.put("editmobile", allbeans.getEditphone());
                jsonObj.put("editemail", allbeans.getEditemail());
                jsonObj.put("account_type", "99");

                jsonObj.put("latitute", AppPreferences.getLatutude(UserProfileAcivity.this));
                jsonObj.put("longitude", AppPreferences.getLongitude(UserProfileAcivity.this));

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObj);

                Log.d("json value:---------", jsonArray.toString());

//                httppost.setHeader("Accept", "application/json");
//                httppost.setHeader("Content-type", "application/json");
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
                        Log.d("email------", AppPreferences.getId(UserProfileAcivity.this));
                        AppPreferences
                                .setUsername(getApplicationContext(), jsonObject.getString("name"));
                        Log.d("name---------", AppPreferences.getUsername(UserProfileAcivity.this));
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
                AppPreferences.setUserprofilepic(UserProfileAcivity.this, user_profile_pic);
                Intent nextintent = new Intent(UserProfileAcivity.this, DrawerMainActivity.class);
                startActivity(nextintent);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Check Your Network Connection", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        }

    }

}
