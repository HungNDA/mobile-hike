package com.example.hikermanagementapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.hikermanagementapp.activity.AddObservationActivity;
import com.example.hikermanagementapp.activity.ViewActivity;
import com.example.hikermanagementapp.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText hikeNameEditText, locationEditText, dateEditText, timeEditText,
            numberOfDaysEditText, descriptionEditText, gearEditText;
    private RadioGroup parkingRadioGroup;
    private RadioButton yesRadioButton, noRadioButton;
    private Spinner difficultySpinner;
    private EditText lengthEditText;
    private Button saveButton, viewButton, observationButton;
    private FloatingActionButton dateFab, timeFab;

    private String[] difficultyLevels = {"easy", "normal", "hard"};
    private DatabaseHelper dbHelper;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        hikeNameEditText = findViewById(R.id.hikeNameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText= findViewById(R.id.timeEditText);
        numberOfDaysEditText = findViewById(R.id.numberOfDaysEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        lengthEditText = findViewById(R.id.lengthEditText);
        gearEditText = findViewById(R.id.gearEditText2);
        parkingRadioGroup = findViewById(R.id.parkingRadioGroup);
        yesRadioButton = findViewById(R.id.yesRadioButton);
        noRadioButton = findViewById(R.id.noRadioButton);
        difficultySpinner = findViewById(R.id.difficultySpinner);
        saveButton = findViewById(R.id.saveButton);
        viewButton = findViewById(R.id.viewButton);
        observationButton = findViewById(R.id.observationButton);
        dateFab = findViewById(R.id.floatingActionButton);
        timeFab = findViewById(R.id.floatingActionButton2);

        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficultyLevels);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);

        //save data
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to save this hike?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String hikeName = hikeNameEditText.getText().toString();
                        String location = locationEditText.getText().toString();
                        String selectedDate = dateEditText.getText().toString();
                        String selectedTime = timeEditText.getText().toString();
                        int numberOfDays = Integer.parseInt(numberOfDaysEditText.getText().toString());
                        String length = lengthEditText.getText().toString();
                        int parkingAvailable = getSelectedParkingOption();
                        String difficulty = difficultySpinner.getSelectedItem().toString();
                        String description = descriptionEditText.getText().toString();
                        String gear = gearEditText.getText().toString();

                        if (hikeName.isEmpty() || location.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty() || numberOfDays <= 0 || length.isEmpty() || parkingAvailable < 0 || difficulty.isEmpty() || description.isEmpty() || gear.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        long newRowId = dbHelper.addHike(hikeName, location, selectedDate, selectedTime, numberOfDays, length, parkingAvailable, difficulty, description, gear);

                        if (newRowId != -1) {
                            AlertDialog.Builder successBuilder = new AlertDialog.Builder(MainActivity.this);
                            successBuilder.setTitle("Success");
                            successBuilder.setMessage("Hike information has been saved successfully.");
                            successBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog successDialog = successBuilder.create();
                            successDialog.show();

                            hikeNameEditText.setText("");
                            locationEditText.setText("");
                            dateEditText.setText("");
                            timeEditText.setText("");
                            numberOfDaysEditText.setText("");
                            lengthEditText.setText("");
                            yesRadioButton.setChecked(false);
                            noRadioButton.setChecked(false);
                            descriptionEditText.setText("");
                            gearEditText.setText("");
                        } else {
                            AlertDialog.Builder errorBuilder = new AlertDialog.Builder(MainActivity.this);
                            errorBuilder.setTitle("Error");
                            errorBuilder.setMessage("Failed to save hike information. Please try again.");
                            errorBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog errorDialog = errorBuilder.create();
                            errorDialog.show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog confirmationDialog = builder.create();
                confirmationDialog.show();
            }
        });

        //view data
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một Intent để chuyển từ MainActivity sang ViewActivity
                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                // Khởi chạy Activity ViewActivity
                startActivity(intent);
            }
        });
//        observation
        observationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddObservationActivity.class);
                startActivity(intent);
            }
        });

        //Date & Time Picker
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
    //parking
    private int getSelectedParkingOption() {
        if (yesRadioButton.isChecked()) {
            return 1; // 1 represents "Yes"
        } else if (noRadioButton.isChecked()) {
            return 0; // 0 represents "No"
        } else {
            return -1; // You may want to handle this case based on your requirements
        }
    }
    private void showDatePickerDialog()     {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dateEditText.setText(selectedDate);
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
                        timeEditText.setText(selectedTime);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }
}


