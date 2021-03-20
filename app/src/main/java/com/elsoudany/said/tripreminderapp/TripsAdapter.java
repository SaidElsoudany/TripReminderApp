package com.elsoudany.said.tripreminderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elsoudany.said.tripreminderapp.room.Trip;

import java.util.List;
import java.util.zip.Inflater;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    Context context;
    List<Trip> list;

    public TripsAdapter(Context context, List<Trip> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trip_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.startingPointField.setText(list.get(position).startPoint);
        holder.endPointField.setText(list.get(position).endPoint);
        holder.dateField.setText(list.get(position).date);
        holder.timeField.setText(list.get(position).time);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView startingPointField;
        public TextView endPointField;
        public TextView dateField;
        public TextView timeField;
        public View convertView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            convertView = itemView;
            startingPointField = convertView.findViewById(R.id.startPointField);
            endPointField = convertView.findViewById(R.id.endPointField);
            dateField = convertView.findViewById(R.id.dateField);
            timeField = convertView.findViewById(R.id.timeField);
        }
    }
}
