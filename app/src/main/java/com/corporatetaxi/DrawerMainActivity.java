package com.corporatetaxi;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.corporatetaxi.classes.FragmentDrawer;
import com.corporatetaxi.utils.AppConstants;
import com.corporatetaxi.utils.AppPreferences;
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
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Eyon on 11/9/2015.
 */
public class DrawerMainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    Toolbar toolbar;
    private FragmentDrawer drawerFragment;
    public static DrawerMainActivity mainActivityInstance = null;

    public static boolean drawerActivity = false;

    @Override
    protected void onStop() {
        drawerActivity = false;
        Log.d("activityTest", "drawer");
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newdrawer_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout
        setSupportActionBar(toolbar); // Setting toolbar as the ActionBar with
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.BLACK);
        String title = getString(R.string.title_activity_home);
        getSupportActionBar().setTitle(title);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Log.d("currentdatetime", currentDateTimeString);

        String data = getIntent().getStringExtra("notificationData");
        if (data != null) {
            caceldialog(data);

        }
        Typeface tf = Typeface.createFromAsset(DrawerMainActivity.this.getAssets(),
                "Montserrat-Regular.ttf");

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
        mainActivityInstance = this;
//        Window window = mainActivityInstance.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.setStatusBarColor(mainActivityInstance.getResources().getColor(R.color.colorPrimaryDark));

        Fragment fragment = new MainFragment();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, fragment).commit();

        }


    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;

        String title = getString(R.string.title_activity_home);
        ;
        switch (position) {

            case 0:
                // fragment = new MainFragment();
                break;

            case 1:
                Intent intent2 = new Intent(DrawerMainActivity.this, TripHistory_Activity.class);
                startActivity(intent2);
                break;
            case 2:
                Intent intent3 = new Intent(DrawerMainActivity.this, UserProfileAcivity.class);
                startActivity(intent3);

                break;
            case 3:
                Intent intent = new Intent(DrawerMainActivity.this, ColoniesList.class);
                startActivity(intent);

                break;
            case 4:
                showAppQuitAlertDialog(getResources().getString(R.string.prompt_log_out), DrawerMainActivity.this);

                break;


        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityInstance = null;
    }

    public void showAppQuitAlertDialog(String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.alert));
        builder.setMessage(message);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("drawer activity instance is ------------- "
                        + DrawerMainActivity.this);
                if (DrawerMainActivity.this != null) {
                    if ((ConnectionDetector
                            .isConnectingToInternet(DrawerMainActivity.this))) {
                        new LogoutTask(DrawerMainActivity.this).execute();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Check network connection", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
                dialog.dismiss();
                Runtime.getRuntime().gc();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // HomeActivity.activityinstance.homeFragment();
                dialog.dismiss();
                Runtime.getRuntime().gc();
            }
        });
        AlertDialog msg = builder.create();
        msg.show();
    }

    // /////////////logoutasync////////////////////////////
    public class LogoutTask extends AsyncTask<String, Void, String> {

        Context context;
        String networkFlag = "false";
        private JSONObject jsonObj;
        private String TAG = LogoutTask.class.getSimpleName();
        private int status = 0;
        private ProgressDialog mProgressDialog;
        public ArrayList<HashMap<String, String>> data;

        public LogoutTask(Context context) {
            this.context = context;

            mProgressDialog = new ProgressDialog(DrawerMainActivity.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setTitle(getResources().getString(R.string.progressmsg));
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                HttpPost httppost = new HttpPost(AppConstants.LOGOUTAPPURL);
                JSONArray jsonArray = new JSONArray();
                StringEntity se = null;
                jsonObj = new JSONObject();
                jsonObj.put("email", AppPreferences.getId(DrawerMainActivity.this));
                jsonObj.put("account_type", "99");
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
                // Log.v("nik", jsonArray.toString());
                System.out.println("JSON sent is : " + jsonArray.toString());
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
                System.out.println("jsonString response is:-" + jsonString);
                if (jsonString != null && jsonString.length() > 0) {
                    if (jsonString.contains("status")) {
                        JSONTokener tokener = new JSONTokener(jsonString);

                        JSONObject finalResult = new JSONObject(tokener);// jsonString
                        System.out.println("JSON response is : " + finalResult);

                        if (finalResult.getString("status").equalsIgnoreCase(
                                "200")) {
                            System.out
                                    .println("--------- message 200 got ----------");
                            status = 200;

                            return jsonString;
                        } else if (finalResult.getString("status")
                                .equalsIgnoreCase("400")) {
                            Log.v(TAG, "Status error");
                            status = 400;
                            return "";
                        } else if (finalResult.getString("status")
                                .equalsIgnoreCase("500")) {
                            Log.v(TAG, "No Data Recieved in Request");
                            status = 500;
                            return "";
                        } else {
                            Log.v(TAG, "200 not recieved");
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
            try {
                mProgressDialog.dismiss();
                System.out.println("status--" + status);

                if (status == 200) {
                    System.out.println("200 dialog if");
                    AppPreferences.setCustomerId(context, 0);
                    AppPreferences.setId(context, "");
                    AppPreferences.setMobileuser(context, "");
                    AppPreferences.setUsername(context, "");
                    Intent i = new Intent(context, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                    System.out
                            .println("loginCustomerId-------------------------"
                                    + AppPreferences.getCustomerId(context));
                } else if (status == 400) {
                    System.out.println("400 dialog if");
                } else {
                    System.out.println("500 dialog if");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void caceldialog(String data) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DrawerMainActivity.this);
//builder.setTitle(getString(R.string.cancel_trip));
//builder.setIcon(R.drawable.ic_launcher);
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.dialog_heading, null);
        TextView textView = (TextView) header.findViewById(R.id.text);
        ImageView icon = (ImageView) header.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.ic_launcher);
        textView.setText(getResources().getString(R.string.cancel_trip));
        builder.setCustomTitle(header);
        builder.setCancelable(false);

        builder.setMessage(data);


        builder.setPositiveButton(getResources().getString(R.string.cancel),
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
    public void onBackPressed() {
       super.onBackPressed();
        finish();
      // backdialog();
    }


}

