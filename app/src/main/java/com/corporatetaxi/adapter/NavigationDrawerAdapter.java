package com.corporatetaxi.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corporatetaxi.R;
import com.corporatetaxi.classes.NavDrawerItem;

import java.util.Collections;
import java.util.List;


/**
 * Created by Eyon 9-11-2015.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    boolean isExpanded = true;
    //DataBaseHelper db;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        // db = new DataBaseHelper(context);
    }

    public void delete(int position) {
        data.remove(position);
        // notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.imageicon.setImageResource(icons[position]);//icons[position]


    }

    Integer[] icons = {R.drawable.car_drawer, R.drawable.list_drawer, R.drawable.myac_drawer, R.drawable.colonies, R.drawable.logout_drawer};

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageicon;
        LinearLayout layout1;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            Typeface tf = Typeface.createFromAsset(context.getAssets(),
                    "Montserrat-Regular.ttf");
            title.setTypeface(tf);
            imageicon = (ImageView) itemView.findViewById(R.id.iconimage);
            layout1 = (LinearLayout) itemView.findViewById(R.id.linearLayout1);

        }
    }
}
