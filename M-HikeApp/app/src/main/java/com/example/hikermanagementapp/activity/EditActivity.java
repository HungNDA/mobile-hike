package com.example.hikermanagementapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Spinner;

import com.example.hikermanagementapp.R;
import com.example.hikermanagementapp.database.DatabaseHelper;
import com.example.hikermanagementapp.model.Hike;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    private EditText editHike, editLocation, editDate, editTime, editNumber, editLength, editDescription, editGear;
    private RadioButton yesRadioButton, noRadioButton;
    private Spinner difficultySpinner;
    private Button updateButton, deleteButton;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton dateFab, timeFab;
    private long hikeId;
    private String[] difficultyLevels = {"easy", "normal", "hard"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editHike = findViewById(R.id.editHike);
        editLocation = findViewById(R.id.editLocation);
        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);
        editNumber = findViewById(R.id.editNumber);
        editLength = findViewById(R.id.editLength);
        editDescription = findViewById(R.id.editDescription);
        editGear = findViewById(R.id.editGear);
        yesRadioButton = findViewById(R.id.yesRadioButton);
        noRadioButton = findViewById(R.id.noRadioButton);
        difficultySpinner = findViewById(R.id.difficultySpinner);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        dateFab = findViewById(R.id.floatingActionButton);
        timeFab = findViewById(R.id.floatingActionButton2);

        databaseHelper = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hikeId = extras.getLong("HIKE_ID");
            Hike hike = databaseHelper.getHikeById(hikeId);

            if (hike != null) {
                editHike.setText(hike.getHikeName());
                editLocation.setText(hike.getLocation());
                editDate.setText(hike.getDate());
                editTime.setText(hike.getTime());
                editNumber.setText(String.valueOf(hike.getNumberOfDays()));
                editLength.setText(hike.getLengthText());

                editDescription.setText(hike.getDescription());
                editGear.setText(hike.getRequiredGear());
                if ("1".equals(hike.getParking())) {
                    yesRadioButton.setChecked(true);
                } else {
                    noRadioButton.setChecked(true);
                }

                ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficultyLevels);
                difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                difficultySpinner.setAdapter(difficultyAdapter);

                if (hike.getDifficulty() != null) {
                    int spinnerPosition = difficultyAdapter.getPosition(hike.getDifficulty());
                    difficultySpinner.setSelection(spinnerPosition);
                }
            }
        }
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updatedHikeName = editHike.getText().toString();
                String updatedLocation = editLocation.getText().toString();
                String updatedDate = editDate.getText().toString();
                String updatedTime = editTime.getText().toString();
                int updatedNumberOfDays = Integer.parseInt(editNumber.getText().toString());
                String updatedLength = editLength.getText().toString();
                String updatedDescription = editDescription.getText().toString();
                String updatedGear = editGear.getText().toString();
                int updatedParking = yesRadioButton.isChecked() ? 1 : 0;
                String updatedDifficulty = difficultySpinner.getSelectedItem().toString();


                // Update information in the database
                databaseHelper.editHike(hikeId, updatedHikeName, updatedLocation, updatedDate,
                        updatedTime, updatedNumberOfDays, updatedLength, updatedParking, updatedDifficulty,
                        updatedDescription, updatedGear, new DatabaseHelper.EditHikeCallback() {
                            @Override
                            public void onEditHikeSuccess() {
                                showAlert("Success", "Hike information has been updated successfully.");
                            }

                            @Override
                            public void onEditHikeFailure(String errorMessage) {
                                showAlert("Error", "Failed to update hike information. " + errorMessage);
                            }
                        });
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDeleted = databaseHelper.deleteHike(hikeId);
                if (isDeleted) {
                    showAlert("Success", "Hike information has been deleted successfully.");
                } else {
                    showAlert("Error", "Failed to delete hike information. Please try again.");
                }
            }
        });

        dateFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        timeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
    }
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        editDate.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + minute;
                        editTime.setText(selectedTime);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }


}


