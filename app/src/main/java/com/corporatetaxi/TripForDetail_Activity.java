package com.corporatetaxi;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.corporatetaxi.beans.Allbeans;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Eyon on 11/9/2015.
 */
public class TripForDetail_Activity extends AppCompatActivity {
    GoogleMap map;
    Button mbtn_delete;
    RatingBar ratingBar;
    TextView meditcomment;
    TextView timehead, mtime, sourceaddresshead, msource, destinationhead,
            mdestinationaddress, datehead, mdate,  mname, mcompanyname,  mmobilenumber,  mtaxinum,
            ratehead, staushead, mstaus, mamount, amounttext;
    Allbeans tripHistory;
    ImageView driverimage;
    CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripfordetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getString(R.string.title_activity_tripdetail);
        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle(title);
       // mbtn_delete = (Button) findViewById(R.id.btn_deletedetail);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        meditcomment = (TextView) findViewById(R.id.edit_comment);
        timehead = (TextView) findViewById(R.id.time_text1);
        mtime = (TextView) findViewById(R.id.time_text);
        sourceaddresshead = (TextView) findViewById(R.id.source_text);
        msource = (TextView) findViewById(R.id.source_text1);
        destinationhead = (TextView) findViewById(R.id.dest_text);
        mdestinationaddress = (TextView) findViewById(R.id.destiaddresss);
        datehead = (TextView) findViewById(R.id.text_date);
        mdate = (TextView) findViewById(R.id.date_txt);
        staushead = (TextView) findViewById(R.id.text_status);
        mstaus = (TextView) findViewById(R.id.status_txt);
        cardView = (CardView) findViewById(R.id.carddriver);
        mname = (TextView) findViewById(R.id.name_text);



        mcompanyname = (TextView) findViewById(R.id.companyname);

        mmobilenumber = (TextView) findViewById(R.id.mobile_text);
        mtaxinum = (TextView) findViewById(R.id.taxinumber);
        ratehead = (TextView) findViewById(R.id.ratetext);
        mamount = (TextView) findViewById(R.id.text_amount);
        amounttext = (TextView) findViewById(R.id.amount_txt);
        driverimage=(ImageView)findViewById(R.id.driver_image);

        Drawable stars = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(stars, getResources().getColor(R.color.colorbutton));

        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Montserrat-Regular.ttf");
      //  mbtn_delete.setTypeface(tf);
        meditcomment.setTypeface(tf);

        timehead.setTypeface(tf);
        mtime.setTypeface(tf);
        sourceaddresshead.setTypeface(tf);
        msource.setTypeface(tf);
        destinationhead.setTypeface(tf);
        mdestinationaddress.setTypeface(tf);
        datehead.setTypeface(tf);
        mdate.setTypeface(tf);
        staushead.setTypeface(tf);
        mstaus.setTypeface(tf);
        mname.setTypeface(tf);
        mcompanyname.setTypeface(tf);
        mmobilenumber.setTypeface(tf);
        mtaxinum.setTypeface(tf);
        ratehead.setTypeface(tf);
        mamount.setTypeface(tf);
        amounttext.setTypeface(tf);
        /////back arrow ////////////
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorblack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        tripHistory = getIntent().getParcelableExtra("tripHistory");

        mname.setText(tripHistory.getDrivername());
        mstaus.setText(tripHistory.getTripstatus());
        msource.setText(tripHistory.getSourcerequest());
        mdestinationaddress.setText(tripHistory.getDestinationresuest());
        mcompanyname.setText(tripHistory.getCompanyname());
        mmobilenumber.setText(tripHistory.getDrivermobilenumber());
        meditcomment.setText(tripHistory.getComment().toString());
        mdate.setText(tripHistory.getTripdate());
        mtime.setText(tripHistory.getTriptime());
        ratingBar.setRating(tripHistory.getRatingvalue());
        mtaxinum.setText(tripHistory.getTaxinumber());
        amounttext.setText(tripHistory.getTripprice());
        if (meditcomment.length()==0){
            meditcomment.setVisibility(View.GONE);
        }
        if(mname.length()==0&& mcompanyname.length()==0&& mtaxinum.length()==0&& mmobilenumber.length()==0){
            cardView.setVisibility(View.GONE);

        }
        if (tripHistory.getDriverimage().equalsIgnoreCase("")){
            driverimage.setImageResource(R.drawable.ic_action_user);
        }else {
            Picasso.with(getApplicationContext()).load(tripHistory.getDriverimage())
                    .error(R.drawable.ic_action_user)
                    .resize(200, 200)
                    .into(driverimage);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = null;
        try {
            date = formatter.parse(tripHistory.getTripdate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
        String setDate = sdf.format(date.getTime());
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm");
        String setTime = sdf1.format(date.getTime());

        mdate.setText(setDate);
        mtime.setText(setTime);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng sourceLocation = new LatLng(tripHistory.getSourcelatitute(), tripHistory.getSourcelongitude());
        LatLng destinationLocation = new LatLng(tripHistory.getDestinationlatitude(), tripHistory.getDestinationlongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(sourceLocation));

        map.moveCamera(CameraUpdateFactory.newLatLng(destinationLocation));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
        MarkerOptions marker = new MarkerOptions().position(sourceLocation).title("Source");
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        map.addMarker(marker);

        MarkerOptions marker2 = new MarkerOptions().position(destinationLocation).title("Destination");
        marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        map.addMarker(marker2);

        ///////draw polyline//////////
        map.addPolyline((new PolylineOptions().add(sourceLocation, destinationLocation)).width(5).color(Color.BLUE).geodesic(true));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;

        }


        return super.onOptionsItemSelected(item);
    }


}
