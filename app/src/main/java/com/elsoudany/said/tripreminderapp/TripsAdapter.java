package com.elsoudany.said.tripreminderapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.elsoudany.said.tripreminderapp.AddNotes.AddNoteActivity;
import com.elsoudany.said.tripreminderapp.room.AppDatabase;
import com.elsoudany.said.tripreminderapp.room.Trip;
import com.elsoudany.said.tripreminderapp.room.TripDAO;
import com.google.firebase.auth.FirebaseAuth;


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
        holder.nameField.setText(list.get(position).tripName);
        holder.startingPointField.setText(list.get(position).startPoint);
        holder.endPointField.setText(list.get(position).endPoint);
        holder.dateField.setText(list.get(position).date);
        holder.timeField.setText(list.get(position).time);
        holder.startBtn.setOnClickListener(view -> {
            list.get(position).status = "started";
            Trip trip = list.get(position);
            list.remove(position);
            this.notifyItemRemoved(position);
            new Thread(){
                @Override
                public void run() {
                    AppDatabase db = Room.databaseBuilder(context,AppDatabase.class,"DataBase-name").build();
                    TripDAO tripDAO = db.tripDAO();
                    tripDAO.insertAll(trip);

                }
            }.start();
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + trip.endPoint));
            context.startActivity(intent);
        });
        holder.cancelBtn.setOnClickListener(view -> {
            list.get(position).status = "cancelled";
            Trip trip = list.get(position);
            list.remove(position);
            this.notifyItemRemoved(position);
            new Thread(){
                @Override
                public void run() {
                    AppDatabase db = Room.databaseBuilder(context,AppDatabase.class,"DataBase-name").build();
                    TripDAO tripDAO = db.tripDAO();
                    tripDAO.insertAll(trip);

                }
            }.start();



        });
        holder.btnAddNotes.setOnClickListener(view -> {
            Intent intent =new Intent(context, AddNoteActivity.class);
            context.startActivity(intent);


        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameField;
        public TextView startingPointField;
        public TextView endPointField;
        public TextView dateField;
        public TextView timeField;
        public View convertView;
        public Button startBtn;
        public Button cancelBtn;
        public ImageView btnAddNotes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            convertView = itemView;
            startBtn = convertView.findViewById(R.id.startBtn);
            cancelBtn = convertView.findViewById(R.id.cancelBtn);
            nameField = convertView.findViewById(R.id.trip_name);
            startingPointField = convertView.findViewById(R.id.startPointField);
            endPointField = convertView.findViewById(R.id.endPointField);
            dateField = convertView.findViewById(R.id.dateField);
            timeField = convertView.findViewById(R.id.timeField);
            btnAddNotes=convertView.findViewById(R.id.btn_addNotes);
        }
    }
}
