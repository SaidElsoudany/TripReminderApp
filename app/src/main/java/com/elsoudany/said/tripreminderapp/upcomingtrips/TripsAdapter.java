package com.elsoudany.said.tripreminderapp.upcomingtrips;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.work.WorkManager;

import com.elsoudany.said.tripreminderapp.FloatingWidget.FloatingViewService;
import com.elsoudany.said.tripreminderapp.R;
import com.elsoudany.said.tripreminderapp.notes.AddNoteActivity;
import com.elsoudany.said.tripreminderapp.room.AppDatabase;
import com.elsoudany.said.tripreminderapp.room.Trip;
import com.elsoudany.said.tripreminderapp.room.TripDAO;


import java.util.List;


public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    private static final int EDIT_TRIP_CODE = 1234;
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
            WorkManager mWorkManger = WorkManager.getInstance(context.getApplicationContext());
            mWorkManger.cancelUniqueWork(""+list.get(position).uid);
            list.get(position).status = "started";
            Trip trip = list.get(position);
            list.remove(position);
            this.notifyItemRemoved(position);
            new Thread(){
                @Override
                synchronized public void run() {
                    AppDatabase db = Room.databaseBuilder(context,AppDatabase.class,"DataBase-name").build();
                    TripDAO tripDAO = db.tripDAO();
                    tripDAO.insertAll(trip);

                }
            }.start();
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + trip.endPoint));
            context.startActivity(intent);
            Intent floatingService= new Intent(context, FloatingViewService.class);
            floatingService.putExtra("tripUid",trip.uid);

            context.startService(floatingService);
//            context..stopSelf();
        });
        holder.cancelBtn.setOnClickListener(view -> {
            WorkManager mWorkManger = WorkManager.getInstance(context.getApplicationContext());
            mWorkManger.cancelUniqueWork(""+list.get(position).uid);
            list.get(position).status = "cancelled";
            Trip trip = list.get(position);
            list.remove(position);
            this.notifyItemRemoved(position);
            new Thread(){
                @Override
                synchronized public void run() {
                    AppDatabase db = Room.databaseBuilder(context,AppDatabase.class,"DataBase-name").build();
                    TripDAO tripDAO = db.tripDAO();
                    tripDAO.insertAll(trip);

                }
            }.start();



        });
        holder.btnAddNotes.setOnClickListener(view -> {

            Intent intent =new Intent(context, AddNoteActivity.class);
            intent.putExtra("TripUid",list.get(position).uid);
            context.startActivity(intent);


        });
        holder.editTripBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddTripActivity.class);
            intent.putExtra("tripData",list.get(position));
            intent.putExtra("editTrip",true);
            intent.putExtra("position",position);
            list.remove(list.get(position));
            notifyItemRemoved(position);
            ((AppCompatActivity)context).startActivityForResult(intent,EDIT_TRIP_CODE);
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
        public ImageView editTripBtn;
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
            btnAddNotes = convertView.findViewById(R.id.btn_addNotes);
            editTripBtn = convertView.findViewById(R.id.editTripBtn);
        }
    }
}
