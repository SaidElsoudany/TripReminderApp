package com.elsoudany.said.tripreminderapp.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elsoudany.said.tripreminderapp.retrofit.MapsApi;
import com.elsoudany.said.tripreminderapp.R;
import com.elsoudany.said.tripreminderapp.retrofit.RetrofitInstance;
import com.elsoudany.said.tripreminderapp.models.MapResponse;
import com.elsoudany.said.tripreminderapp.room.Trip;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        holder.tripStatus.setText(list.get(position).status);
        MapsApi mapsApi = RetrofitInstance.getRetrofitInstance().create(MapsApi.class);
        Call<MapResponse> call = mapsApi.getDirections(list.get(position).startPoint,list.get(position).endPoint);
        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {

                if(response.body().routes.size() != 0) {
                    String encodedPath = response.body().routes.get(0).overview_polyline.points;
                    List<LatLng> decodedPolyLine = PolyUtil.decode(encodedPath);
                    Glide.with(context).load("https://maps.googleapis.com/maps/api/staticmap?markers=size:mid%7Ccolor:red%7C\""
                            + decodedPolyLine.get(decodedPolyLine.size()-1).latitude
                            + "," + decodedPolyLine.get(decodedPolyLine.size()-1).longitude
                            + "\"|\"" + decodedPolyLine.get(0).latitude
                            + "," + decodedPolyLine.get(0).longitude
                            + "\"&size=800x400&path=color:0x212121|weight:5%7Cenc:"
                            + encodedPath
                            + "&key=AIzaSyCdXqSieoMfWeS3GunOh0FKQzKJnsCWIGM")
                            .placeholder(R.drawable.placeholder)
                            .into(holder.mapImageView);
                }
            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tripName;
        TextView startPoint;
        TextView endPoint;
        TextView tripStatus;
        ImageView delete;
        ImageView mapImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tripName);
            startPoint = itemView.findViewById(R.id.startPoint);
            endPoint = itemView.findViewById(R.id.endPoint);
            delete = itemView.findViewById(R.id.delete);
            tripStatus=itemView.findViewById(R.id.tripStatus);
            mapImageView = itemView.findViewById(R.id.mapImage);

        }
    }
}

