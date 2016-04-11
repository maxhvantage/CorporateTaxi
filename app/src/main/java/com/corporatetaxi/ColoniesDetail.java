package com.corporatetaxi;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.corporatetaxi.model.ColoniesListModel;

public class ColoniesDetail extends AppCompatActivity {
    TextView source_name, destination_name, source_add, destination_add, total_fare, total_distance, source_city, source_postal, source_state, desti_city, desti_postal, desti_state, desti_description;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13;
    int serverColoniesId, taxiCompanyId;
    String sourceName, destinationName, sourceAddress,
            destinationAddress, sourceCity, sourceState, sourcePostelcode, destinationCity,
            destinationState, destinationPostelcode, fare, distance, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colonies_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getString(R.string.title_activity_colonies_detail);
        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle(title);
        ///back arrow ////////////
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorblack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        Bundle bundle = getIntent().getExtras();
        serverColoniesId = bundle.getInt("serverColoniesId");
        taxiCompanyId = bundle.getInt("taxiCompanyId");
        sourceName = bundle.getString("sourceName");
        destinationName = bundle.getString("destinationName");
        sourceAddress = bundle.getString("sourceAddress");
        destinationAddress = bundle.getString("destinationAddress");
        sourceCity = bundle.getString("sourceCity");
        sourceState = bundle.getString("sourceState");
        sourcePostelcode = bundle.getString("sourcePostelcode");
        destinationCity = bundle.getString("destinationCity");
        destinationState = bundle.getString("destinationState");
        destinationPostelcode = bundle.getString("destinationPostelcode");
        fare = bundle.getString("fare");
        distance = bundle.getString("distance");
        description = bundle.getString("description");


        tv1 = (TextView) findViewById(R.id.source_name_text);
        tv2 = (TextView) findViewById(R.id.source_add_text);
        tv3 = (TextView) findViewById(R.id.source_citytext);
        tv4 = (TextView) findViewById(R.id.source_postaltext);
        tv5 = (TextView) findViewById(R.id.source_statetext);
        tv6 = (TextView) findViewById(R.id.destination_name_text);
        tv7 = (TextView) findViewById(R.id.destination_add_text);
        tv8 = (TextView) findViewById(R.id.desti_citytext);
        tv9 = (TextView) findViewById(R.id.desti_postaltext);
        tv10 = (TextView) findViewById(R.id.desti_statetext);
        tv11 = (TextView) findViewById(R.id.fare_text);
        tv12 = (TextView) findViewById(R.id.distance_text);
        tv13 = (TextView) findViewById(R.id.description_text);

        source_name = (TextView) findViewById(R.id.source_name);
        destination_name = (TextView) findViewById(R.id.destination_name);
        source_add = (TextView) findViewById(R.id.source_add);
        destination_add = (TextView) findViewById(R.id.destination_add);
        total_fare = (TextView) findViewById(R.id.fare);
        total_distance = (TextView) findViewById(R.id.distance);
        source_city = (TextView) findViewById(R.id.source_city);
        source_postal = (TextView) findViewById(R.id.source_postal);
        source_state = (TextView) findViewById(R.id.source_state);
        desti_city = (TextView) findViewById(R.id.dest_city);
        desti_postal = (TextView) findViewById(R.id.desti_postal);
        desti_state = (TextView) findViewById(R.id.desti_statetext);
        desti_description = (TextView) findViewById(R.id.description);


        source_name.setText(sourceName);
        destination_name.setText(destinationName);
        source_add.setText(sourceAddress);
        destination_add.setText(destinationAddress);
        total_fare.setText(fare);
        total_distance.setText(distance);
        source_city.setText(sourceCity);
        source_postal.setText(sourcePostelcode);
        source_state.setText(sourceState);
        desti_city.setText(destinationCity);
        desti_postal.setText(destinationPostelcode);
        desti_state.setText(destinationState);
        desti_description.setText(description);

        Typeface tf = Typeface.createFromAsset(this.getAssets(),
                "Montserrat-Regular.ttf");
        source_name.setTypeface(tf);
        destination_name.setTypeface(tf);
        source_add.setTypeface(tf);
        destination_add.setTypeface(tf);
        total_fare.setTypeface(tf);
        total_distance.setTypeface(tf);
        source_city.setTypeface(tf);
        source_postal.setTypeface(tf);
        source_state.setTypeface(tf);
        desti_city.setTypeface(tf);
        desti_postal.setTypeface(tf);
        desti_state.setTypeface(tf);
        desti_description.setTypeface(tf);


        tv1.setTypeface(tf);
        tv2.setTypeface(tf);
        tv3.setTypeface(tf);
        tv4.setTypeface(tf);
        tv5.setTypeface(tf);
        tv6.setTypeface(tf);
        tv7.setTypeface(tf);
        tv8.setTypeface(tf);
        tv9.setTypeface(tf);
        tv10.setTypeface(tf);
        tv11.setTypeface(tf);
        tv12.setTypeface(tf);
        tv13.setTypeface(tf);

        destination_name.setTypeface(tf);
        source_add.setTypeface(tf);
        destination_add.setTypeface(tf);
        total_fare.setTypeface(tf);
        total_distance.setTypeface(tf);
        source_city.setTypeface(tf);
        source_postal.setTypeface(tf);
        source_state.setTypeface(tf);
        desti_city.setTypeface(tf);
        desti_postal.setTypeface(tf);
        desti_state.setTypeface(tf);
        desti_description.setTypeface(tf);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
//            Intent intent = new Intent(ColoniesDetail.this, ColoniesList.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            finish();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
