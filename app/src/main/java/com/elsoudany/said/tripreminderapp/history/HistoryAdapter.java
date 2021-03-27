package com.elsoudany.said.tripreminderapp.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elsoudany.said.tripreminderapp.R;
import com.elsoudany.said.tripreminderapp.room.Trip;

import java.util.List;

class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    Context context;
    List<Trip> list;

    public HistoryAdapter(Context context, List<Trip> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recy_history_item,parent,false);
        HistoryAdapter.ViewHolder viewHolder = new HistoryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {

        holder.tripName.setText(list.get(position).tripName);
        holder.startPoint.setText(list.get(position).startPoint);
        holder.endPoint.setText(list.get(position).endPoint);
        holder.date.setText(list.get(position).date);
        holder.time.setText(list.get(position).time);
        holder.tripStatus.setText(list.get(position).status);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tripName;
        TextView startPoint;
        TextView endPoint;
        TextView time;
        TextView date;
        TextView tripStatus;
        ImageView delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tripName);
            startPoint = itemView.findViewById(R.id.startPoint);
            endPoint = itemView.findViewById(R.id.endPoint);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            delete = itemView.findViewById(R.id.delete);
            tripStatus=itemView.findViewById(R.id.tripStatus);

        }
    }
}

