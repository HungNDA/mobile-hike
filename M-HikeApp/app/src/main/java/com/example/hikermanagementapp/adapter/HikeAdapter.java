package com.example.hikermanagementapp.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikermanagementapp.activity.EditActivity;
import com.example.hikermanagementapp.model.Hike;
import com.example.hikermanagementapp.R;

import java.util.ArrayList;
import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.ViewHolder> {

    private List<Hike> hikes;

    public HikeAdapter(List<Hike> hikes) {
        this.hikes = new ArrayList<>(hikes);
    }

    public void UpdateList(List<Hike> hikes) {
        this.hikes = new ArrayList<>(hikes);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hike hike = hikes.get(position);
        holder.hikeNameTextView.setText("" + hike.getHikeName());
        holder.locationTextView.setText("" + hike.getLocation());
        holder.dateTextView.setText("" + hike.getDate());
        holder.timeTextView.setText("" + hike.getTime());
        holder.daysTextView.setText("" + hike.getNumberOfDays());
        holder.lengthTextView.setText("" + hike.getLengthText());
        holder.descriptionTextView.setText("Description: " + hike.getDescription());
        int parkingStatus = hike.getParking();
        holder.parkingTextView.setText("" + convertParkingStatus(parkingStatus));
        holder.difficultyTextView.setText("" + hike.getDifficulty());
        holder.gearTextView.setText("Gear: " + hike.getRequiredGear());

        holder.hikeId.setText(String.valueOf(hike.getId()));
    }

    @Override
    public int getItemCount() {
        return hikes.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView hikeNameTextView, locationTextView, dateTextView, timeTextView,
                daysTextView, lengthTextView, descriptionTextView, parkingTextView,
                difficultyTextView, gearTextView, hikeId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hikeNameTextView = itemView.findViewById(R.id.viewHike);
            locationTextView = itemView.findViewById(R.id.viewLocation);
            dateTextView = itemView.findViewById(R.id.editDate);
            timeTextView = itemView.findViewById(R.id.viewTime);
            daysTextView = itemView.findViewById(R.id.viewNumber);
            lengthTextView = itemView.findViewById(R.id.viewLength);
            descriptionTextView = itemView.findViewById(R.id.viewDescription);
            parkingTextView = itemView.findViewById(R.id.viewParking);
            difficultyTextView = itemView.findViewById(R.id.viewLevel);
            gearTextView = itemView.findViewById(R.id.viewGear);
            hikeId = itemView.findViewById(R.id.itemViewHikeId);

            itemView.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Hike clickedHike = hikes.get(adapterPosition);
                    Bundle extras = new Bundle();
                    extras.putLong("HIKE_ID", clickedHike.getId());

                    Intent intent = new Intent(v.getContext(), EditActivity.class);
                    intent.putExtras(extras);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    private String convertParkingStatus(int parkingValue) {
        if (parkingValue == 1) {
            return "Available";
        } else if (parkingValue == 0) {
            return "Not Available";
        }
        return "";
    }

}
