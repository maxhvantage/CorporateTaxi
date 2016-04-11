package com.corporatetaxi.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.corporatetaxi.R;
import com.corporatetaxi.beans.Allbeans;

import java.util.ArrayList;

/**
 * Created by Eyon on 12/16/2015.
 */
public class DestinationListAdapter extends ArrayAdapter<Allbeans> {

    Context context;
    ArrayList<Allbeans> objects;
    int row;

    public DestinationListAdapter(Context context, int row,
                                  ArrayList<Allbeans> objects) {
        super(context, row, objects);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.row = row;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        ViewHolder holder;
        if (v == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            v = inflater.inflate(this.row, parent, false);
            holder = new ViewHolder();
            holder.tv1 = (TextView) v.findViewById(R.id.trip_name);
            holder.tv2 = (TextView) v.findViewById(R.id.trip_amount);
            holder.tv3 = (TextView) v.findViewById(R.id.textdesh);


            Typeface tf = Typeface.createFromAsset(context.getAssets(),
                    "Montserrat-Regular.ttf");
            holder.tv1.setTypeface(tf);
            holder.tv2.setTypeface(tf);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        Allbeans bean = objects.get(position);
        holder.tv1.setText(bean.getPriviousdestinationaddress());
        holder.tv2.setText(String.valueOf(bean.getDestinationamount()));
        // holder.tv2.setText(bean.getTripprice());

        return v;
    }

    class ViewHolder {
        TextView tv1, tv2, tv3;

    }

}
