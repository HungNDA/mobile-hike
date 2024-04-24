package com.example.hikermanagementapp.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikermanagementapp.R;
import com.example.hikermanagementapp.adapter.HikeAdapter;
import com.example.hikermanagementapp.database.DatabaseHelper;
import com.example.hikermanagementapp.model.Hike;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HikeAdapter hikeAdapter;
    private DatabaseHelper dbHelper;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hike);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        dbHelper = new DatabaseHelper(this);
        List<Hike> hikes = dbHelper.getAllHikes();

        recyclerView = findViewById(R.id.RVhike);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hikeAdapter = new HikeAdapter(hikes);
        recyclerView.setAdapter(hikeAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String text) {
        List<Hike> filteredList = new ArrayList<>();
        List<Hike> allHikes = dbHelper.getAllHikes();

        for (Hike hike : allHikes) {
            if (hike.getHikeName().toLowerCase().contains(text.toLowerCase()) ||
                    hike.getLocation().toLowerCase().contains(text.toLowerCase()) ||
                    hike.getDate().toLowerCase().contains(text.toLowerCase()) ||
                    hike.getTime().toLowerCase().contains(text.toLowerCase()) ||
                    String.valueOf(hike.getNumberOfDays()).toLowerCase().contains(text.toLowerCase()) ||
                    String.valueOf(hike.getParking()).toLowerCase().contains(text.toLowerCase()) ||
                    hike.getDifficulty().toLowerCase().contains(text.toLowerCase()) ||
                    hike.getLengthText().toLowerCase().contains(text.toLowerCase()) ||
                    hike.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                    hike.getRequiredGear().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(hike);
            }
        }

        hikeAdapter.UpdateList(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadList();
    }
    public void reloadList() {
        List<Hike> hikes = dbHelper.getAllHikes();

        hikeAdapter.UpdateList(hikes);
    }
}
