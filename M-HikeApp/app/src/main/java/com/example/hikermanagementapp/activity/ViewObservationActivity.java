package com.example.hikermanagementapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.hikermanagementapp.R;
import com.example.hikermanagementapp.adapter.ObservationAdapter;
import com.example.hikermanagementapp.database.DatabaseHandler;
import com.example.hikermanagementapp.model.ObservationModel;

import java.util.List;

public class ViewObservationActivity  extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ObservationAdapter observationAdapter;
    private List<ObservationModel> observationList;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_observation_item);

        databaseHandler = new DatabaseHandler(this);
        List<ObservationModel> observations = databaseHandler.getAllObservations();

        recyclerView = findViewById(R.id.RVObservation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        observationList = databaseHandler.getAllObservations();
        observationAdapter = new ObservationAdapter(observationList);
        recyclerView.setAdapter(observationAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        reloadList();
    }

    private void reloadList() {
        observationList.clear();
        observationList.addAll(databaseHandler.getAllObservations());
        observationAdapter.updateObservationList(observationList);;
    }
}