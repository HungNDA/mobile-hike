package com.example.hikermanagementapp.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikermanagementapp.R;
import com.example.hikermanagementapp.activity.EditActivity;
import com.example.hikermanagementapp.activity.EditObservationActivity;
import com.example.hikermanagementapp.model.Hike;
import com.example.hikermanagementapp.model.ObservationModel;

import java.util.ArrayList;
import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ViewHolder> {
    private List<ObservationModel> observationList;

    public ObservationAdapter(List<ObservationModel> observationList) {
        this.observationList = new ArrayList<>(observationList);
    }

    public void updateObservationList(List<ObservationModel> observationList) {
        this.observationList = new ArrayList<>(observationList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateViewObservation, timeViewObservation, commentViewObservation, observationId;
        public ImageView imageViewProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            dateViewObservation = itemView.findViewById(R.id.dateViewObservation);
            timeViewObservation = itemView.findViewById(R.id.timeViewObservation);
            commentViewObservation = itemView.findViewById(R.id.commentViewObservation);
            imageViewProfile = itemView.findViewById(R.id.imageView);
            observationId = itemView.findViewById(R.id.itemViewObservationId);

            itemView.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    ObservationModel clickedObservation = observationList.get(adapterPosition);
                    Bundle extras = new Bundle();
                    extras.putLong("OBSERVATION_ID", clickedObservation.getId());

                    Intent intent = new Intent(v.getContext(), EditObservationActivity.class);
                    intent.putExtras(extras);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_view_observation, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ObservationModel observation = observationList.get(position);
        holder.dateViewObservation.setText(observation.getDate());
        holder.timeViewObservation.setText(observation.getTime());
        holder.commentViewObservation.setText(observation.getComment());
        holder.observationId.setText(String.valueOf(observation.getId()));

        Bitmap profileImage = observation.getProfileImage();
        if (profileImage != null) {
            holder.imageViewProfile.setImageBitmap(profileImage);
        } else {
            holder.imageViewProfile.setImageResource(R.drawable.image1);
        }
    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }
}
