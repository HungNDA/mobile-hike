package com.example.hikermanagementapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hikermanagementapp.R;
import com.example.hikermanagementapp.database.DatabaseHandler;
import com.example.hikermanagementapp.model.ObservationModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Calendar;

public class EditObservationActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText dateObservation, commentObservation, timeObservation;
    private ImageButton buttonCapture;
    private Button updateObservation, deleteObservation;
    private FloatingActionButton dateFab, timeFab;
    private TextView titleText;
    private ImageView imageView;

    private long observationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_observation);


        // Initialize UI elements
        dateObservation = findViewById(R.id.dateObservation);
        commentObservation = findViewById(R.id.commentObservation);
        timeObservation = findViewById(R.id.timeObservation);
        buttonCapture = findViewById(R.id.buttonCapture);
        dateFab = findViewById(R.id.floatingActionButton3);
        timeFab = findViewById(R.id.floatingActionButton4);
        updateObservation = findViewById(R.id.updateObservation);
        deleteObservation = findViewById(R.id.deleteObservation);
        titleText = findViewById(R.id.titleText);
        imageView = findViewById(R.id.imageView);

        // Get observation ID from intent
        observationId = getIntent().getLongExtra("OBSERVATION_ID", -1);

        // Set click listeners
        buttonCapture.setOnClickListener(view -> openImagePicker());

        dateFab.setOnClickListener(v -> showDatePickerDialog());

        timeFab.setOnClickListener(v -> showTimePickerDialog());

        updateObservation.setOnClickListener(view -> {
            String updateDate = dateObservation.getText().toString();
            String updateTime = timeObservation.getText().toString();
            String updateComment = commentObservation.getText().toString();
            Bitmap profileImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            DatabaseHandler databaseHandler = new DatabaseHandler(EditObservationActivity.this);
            databaseHandler.editObservation(observationId, updateDate, updateTime, updateComment, profileImage, new DatabaseHandler.EditObservationCallBack() {
                @Override
                public void onEditObservationSuccess() {
                    showAlert("Success", "Observation information has been updated successfully.");
                }

                @Override
                public void onEditObservationFailure(String errorMessage) {
                    showAlert("Error", "Failed to update observation information. " + errorMessage);
                }
            });
        });

        // Handle deleteObservation button click
        deleteObservation.setOnClickListener(view -> {
            DatabaseHandler databaseHandler = new DatabaseHandler(EditObservationActivity.this);
            databaseHandler.deleteObservation(observationId, new DatabaseHandler.DeleteObservationCallBack() {
                @Override
                public void onDeleteObservationSuccess() {
                    showAlert("Success", "Observation has been deleted successfully.");
                    finish(); // Close the EditObservationActivity after deletion
                }

                @Override
                public void onDeleteObservationFailure(String errorMessage) {
                    showAlert("Error", "Failed to delete observation. " + errorMessage);
                }
            });
        });
        fetchObservationDetails();

    }
    private void fetchObservationDetails() {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        ObservationModel observation = databaseHandler.getObservationById(observationId);

        if (observation != null) {
            // Populate UI elements with existing observation details
            dateObservation.setText(observation.getDate());
            timeObservation.setText(observation.getTime());
            commentObservation.setText(observation.getComment());

            Bitmap profileImage = observation.getProfileImage();
            if (profileImage != null) {
                imageView.setImageBitmap(profileImage);
            } else {
                imageView.setImageResource(R.drawable.person); // Set a default image
            }
        }
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    dateObservation.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    String selectedTime = hourOfDay + ":" + minute1;
                    timeObservation.setText(selectedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void openImagePicker() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent capturePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (capturePhotoIntent.resolveActivity(getPackageManager()) != null) {
            Intent chooserIntent = Intent.createChooser(pickPhotoIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{capturePhotoIntent});
            startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
        } else {
            startActivityForResult(pickPhotoIntent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null && extras.containsKey("data")) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
        }
    }

    private void showAlert(String title, String message) {
    }
}
