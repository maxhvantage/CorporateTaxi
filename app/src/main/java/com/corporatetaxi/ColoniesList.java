package com.corporatetaxi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.corporatetaxi.adapter.ColoniesListAdapter;
import com.corporatetaxi.beans.Allbeans;
import com.corporatetaxi.model.ColoniesListModel;
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
import java.util.List;

public class ColoniesList extends AppCompatActivity {
    List<ColoniesListModel> coloniesList;
    ColoniesListAdapter colonyAdapter;
    ListView colonyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colonies_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coloniesList = new ArrayList<ColoniesListModel>();
        colonyListView = (ListView) findViewById(R.id.colonies_list);

        colonyAdapter = new ColoniesListAdapter(ColoniesList.this, coloniesList);
        colonyListView.setAdapter(colonyAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getString(R.string.title_activity_colonies_list);
        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle(title);
        Typeface tf = Typeface.createFromAsset(ColoniesList.this.getAssets(),
                "Montserrat-Regular.ttf");

        new ColoniesListTask(getApplicationContext()).execute();

        ///back arrow ////////////
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorblack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        colonyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ColoniesListModel colony = coloniesList.get(position);//colonyAdapter.data.get(position);
                Intent i = new Intent(ColoniesList.this,
                        ColoniesDetail.class);


                i.putExtra("serverColoniesId", colony.getServerColoniesId());
                i.putExtra("taxiCompanyId", colony.getTaxiCompanyId());
                i.putExtra("sourceName", colony.getSourceName());
                i.putExtra("destinationName", colony.getDestinationName());
                i.putExtra("sourceAddress", colony.getSourceAddress());
                i.putExtra("destinationAddress", colony.getDestinationAddress());
                i.putExtra("sourceCity", colony.getSourceCity());
                i.putExtra("sourceState", colony.getSourceState());
                i.putExtra("sourcePostelcode", colony.getSourcePostelcode());
                i.putExtra("destinationCity", colony.getDestinationCity());
                i.putExtra("destinationState", colony.getDestinationState());
                i.putExtra("destinationPostelcode", colony.getDestinationPostelcode());
                i.putExtra("fare", colony.getFare());
                i.putExtra("distance", colony.getDistance());
                i.putExtra("description", colony.getDescription());

                System.out.println("id--------------" + colony.getServerColoniesId());
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            Intent intent = new Intent(ColoniesList.this, DrawerMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private class ColoniesListTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;
        Allbeans allbeans;
        private JSONObject jsonObj;
        ArrayList<Integer> catogariesid;
        private int status = 0;
        String user_profile_pic;
        String loginId = "";
        String resultnull;
        private String TAG = ColoniesListTask.class.getSimpleName();
//        ArrayList<ColoniesListModel> colonies_list = new ArrayList<ColoniesListModel>();

        public ColoniesListTask(Context context) {
            this.allbeans = allbeans;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ColoniesList.this);
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
                System.out.println("update userprofile value : ------------ "
                        + AppConstants.UPDATEPROFILE);
                HttpPost httppost = new HttpPost(AppConstants.COLONIESLIST);
                jsonObj = new JSONObject();
                jsonObj.put("uid", AppPreferences.getCustomerId(ColoniesList.this));

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
                System.out.println("JSONString response is : " + jsonString);
                if (jsonString != null) {


                    System.out.println("json response string is -------------    " + jsonString);
                    if (jsonString.contains("result")) {

                        try {
                            JSONObject jsonObj = new JSONObject(jsonString);
                            JSONArray jsonChildArray = jsonObj.getJSONArray("result");//result

                            if (jsonObj.getString("status").equalsIgnoreCase("200")) {
                                status = 200;

                                Log.v(TAG, "after jsonChildArray" + jsonChildArray + " result array length is : " + jsonChildArray.length());
                                if (jsonChildArray != null && jsonChildArray.length() > 0) {
                                    for (int i = 0; i < jsonChildArray.length(); i++) {
                                        JSONObject jsonchildObj = jsonChildArray.getJSONObject(i);
                                        int serverColoniesId = Integer.parseInt(jsonchildObj.getString("id"));
                                        int taxiCompanyId = Integer.parseInt(jsonchildObj.getString("taxi_company_id"));
                                        String sourceName = jsonchildObj.getString("source_name");
                                        String destinationName = jsonchildObj.getString("destination_name");
                                        String sourceAddress = jsonchildObj.getString("source_address");
                                        String destinationAddress = jsonchildObj.getString("destination_address");
                                        String sourceCity = jsonchildObj.getString("source_city");
                                        String sourceState = jsonchildObj.getString("source_state");
                                        String sourcePostelcode = jsonchildObj.getString("source_postal_code");
                                        String destinationCity = jsonchildObj.getString("destination_city");
                                        String destinationState = jsonchildObj.getString("destination_state");
                                        String destinationPostelcode = jsonchildObj.getString("destination_postal_code");
                                        String fare = jsonchildObj.getString("fare");
                                        String distance = jsonchildObj.getString("km_distance");
                                        String description = jsonchildObj.getString("description");
                                        ColoniesListModel colonies = new ColoniesListModel();
                                        colonies.setServerColoniesId(serverColoniesId);
                                        colonies.setTaxiCompanyId(taxiCompanyId);
                                        colonies.setSourceName(sourceName);
                                        colonies.setDestinationName(destinationName);
                                        colonies.setSourceAddress(sourceAddress);
                                        colonies.setDestinationAddress(destinationAddress);
                                        colonies.setSourceCity(sourceCity);
                                        colonies.setSourceState(sourceState);
                                        colonies.setSourcePostelcode(sourcePostelcode);
                                        colonies.setDestinationCity(destinationCity);
                                        colonies.setDestinationAddress(destinationAddress);
                                        colonies.setDestinationPostelcode(destinationPostelcode);
                                        colonies.setFare(fare);
                                        colonies.setDescription(description);
                                        colonies.setDistance(distance);
                                        coloniesList.add(colonies);


                                    }

                                } else {
                                    resultnull = "";
                                }

                            } else if (jsonObj.getString("status").equalsIgnoreCase("404")) {
                                Log.v(TAG, "Status error");
                                status = 404;
                            } else if (jsonObj.getString("status").equalsIgnoreCase("500")) {
                                status = 500;
                                Log.v(TAG, "No Data Recieved in Request");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
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
                colonyAdapter.updateResults(coloniesList);
            }if(status==500){
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.no_data), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.network_error), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        }

    }


}
