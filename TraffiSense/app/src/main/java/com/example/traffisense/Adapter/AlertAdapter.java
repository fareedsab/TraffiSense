package com.example.traffisense.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traffisense.Models.AlertModel;
import com.example.traffisense.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.viewHolder> {
    private Context context;
    private List<AlertModel> alerts;

    public AlertAdapter(Context context, List<AlertModel> alertModelList) {
        this.context = context;
        this.alerts = alertModelList;
    }
    Date date = null;
    @NonNull
    @Override
    public AlertAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.alert_item, parent, false);
        return new AlertAdapter.viewHolder(view);
    }
    public SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMMM yyyy");
    @Override
    public void onBindViewHolder(@NonNull AlertAdapter.viewHolder holder, int position) {
        AlertModel alertModel = alerts.get(position);
        holder.fire.setText(alertModel.getFire());
        holder.weopon.setText(alertModel.getWeopon());
        holder.eventtime.setText(alertModel.getAlerttime());
        holder.traffic.setText(alertModel.getTraffic());
        try {
            date=outputDateFormat.parse(alertModel.getAlertdate());
           String outputDateString = dateFormat.format(date);
            String[] items1 = outputDateString.split(" ");
            String day = items1[0];
            String dd = items1[1];
            String month = items1[2];
            holder.eventmonth.setText(month);
            holder.eventday.setText(day);
            holder.eventdate.setText(dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //date wala kaam krnaw


    }

    public void updatelist(List<AlertModel> events) {
        this.alerts = events;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView eventday, eventdate, eventmonth, eventtime, fire, traffic,weopon;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            eventdate = itemView.findViewById(R.id.date);
            eventtime = itemView.findViewById(R.id.eventtime);
            eventmonth = itemView.findViewById(R.id.month);
            eventday = itemView.findViewById(R.id.day);
            fire = itemView.findViewById(R.id.tvFireData);
            weopon = itemView.findViewById(R.id.tvWeaponData);
            traffic = itemView.findViewById(R.id.tvTrafficData);
        }
    }
}
