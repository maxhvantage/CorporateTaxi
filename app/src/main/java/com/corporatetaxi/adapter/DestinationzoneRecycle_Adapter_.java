package com.corporatetaxi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.corporatetaxi.DestinationSelection_Activity;
import com.corporatetaxi.R;
import com.corporatetaxi.beans.Allbeans;

import java.util.ArrayList;

/**
 * Created by Eyon on 12/17/2015.
 */
public class DestinationzoneRecycle_Adapter_ extends RecyclerView.Adapter<DestinationzoneRecycle_Adapter_.ViewHolder> {
    private Allbeans[] allbeanses;
    // ArrayList<Allbeans> objects;

    public DestinationzoneRecycle_Adapter_(Allbeans[] allbeanses) {
        this.allbeanses = allbeanses;
    }

    public DestinationzoneRecycle_Adapter_(DestinationSelection_Activity destinationSelection_activity, ArrayList<Allbeans> al) {
        this.allbeanses = allbeanses;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public DestinationzoneRecycle_Adapter_.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customdestination, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.tv1.setText(allbeanses[position].getTripname());
        viewHolder.tv2.setText(allbeanses[position].getTripprice());


    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv1, tv2, tv3;
        public CheckBox chk1;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tv1 = (TextView) itemLayoutView.findViewById(R.id.trip_name);
            tv2 = (TextView) itemLayoutView.findViewById(R.id.trip_amount);
            tv3 = (TextView) itemLayoutView.findViewById(R.id.trip_name);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return allbeanses.length;
    }
}