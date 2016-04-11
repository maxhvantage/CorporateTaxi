package com.corporatetaxi.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.corporatetaxi.R;
import com.corporatetaxi.beans.Allbeans;
import com.corporatetaxi.model.ColoniesListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MMFA-YOGESH on 01/14/2016.
 */
public class ColoniesListAdapter extends BaseAdapter {

    public List<ColoniesListModel> data;
    private Activity activity;

    private static LayoutInflater inflater = null;

    public ColoniesListAdapter(Activity a, List<ColoniesListModel> d) {
        this.activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateResults(List<ColoniesListModel> d) {
        data = d;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = null;
        convertView = null;

        if (convertView == null) {
            vi = inflater.inflate(
                    R.layout.custom_colonies_list, null);
            final ViewHolder holder = new ViewHolder();
            try {
                holder.source_name = (TextView) vi
                        .findViewById(R.id.source_name);
                holder.destination_name = (TextView) vi
                        .findViewById(R.id.destination_name);
                holder.source_add = (TextView) vi
                        .findViewById(R.id.source_add);
                holder.destination_add = (TextView) vi
                        .findViewById(R.id.destination_add);
                holder.fare = (TextView) vi
                        .findViewById(R.id.fare);
                holder.distance = (TextView) vi
                        .findViewById(R.id.distance);
                holder.taxiId = (TextView) vi
                        .findViewById(R.id.taxi_id);

                holder.idhead = (TextView) vi.findViewById(R.id.taxiheadiid);
                holder.sourcenamehead = (TextView) vi.findViewById(R.id.sorcehead);
                holder.desinamehead = (TextView) vi.findViewById(R.id.destinationhead);
                holder.sourceadhead = (TextView) vi.findViewById(R.id.sourceaddresshead);
                holder.destiheadadd = (TextView) vi.findViewById(R.id.destinationaddhead);
                holder.farehead = (TextView) vi.findViewById(R.id.farehead);
                holder.amounthead = (TextView) vi.findViewById(R.id.distancehead);

                Typeface tf = Typeface.createFromAsset(activity.getAssets(),
                        "Montserrat-Regular.ttf");
                holder.source_name.setTypeface(tf);
                holder.destination_name.setTypeface(tf);
                holder.source_add.setTypeface(tf);
                holder.destination_add.setTypeface(tf);
                holder.fare.setTypeface(tf);
                holder.distance.setTypeface(tf);
                holder.taxiId.setTypeface(tf);


                holder.idhead.setTypeface(tf);
                holder.sourcenamehead.setTypeface(tf);
                holder.desinamehead.setTypeface(tf);
                holder.sourceadhead.setTypeface(tf);
                holder.destiheadadd.setTypeface(tf);
                holder.farehead.setTypeface(tf);
                holder.amounthead.setTypeface(tf);


                vi.setTag(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (data.size() <= 0) {

        } else {
            final ViewHolder holder = (ViewHolder) vi.getTag();
            ColoniesListModel colony;
            colony = data.get(position);

            holder.source_name.setText(colony.getSourceName());
            holder.destination_name.setText(colony.getDestinationName());
            holder.source_add.setText(colony.getSourceAddress());
            holder.destination_add.setText(colony.getDestinationAddress());
            holder.fare.setText(colony.getFare());
            holder.distance.setText(colony.getDistance());
            holder.taxiId.setText("Taxi id" + " " + colony.getTaxiCompanyId());
        }

        return vi;
    }


    public static class ViewHolder {
        public TextView source_name;
        public TextView destination_name;
        public TextView source_add;
        public TextView destination_add;
        public TextView fare;
        public TextView distance;
        public TextView taxiId;

        public TextView idhead, sourcenamehead, desinamehead, sourceadhead, destiheadadd, farehead, amounthead;

    }

}
