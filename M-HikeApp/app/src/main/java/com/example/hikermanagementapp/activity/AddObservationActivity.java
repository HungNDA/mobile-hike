package com.example.hikermanagementapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
//import com.github.dhaval2404.imagepicker.ImagePicker;
import com.example.hikermanagementapp.R;
import com.example.hikermanagementapp.database.DatabaseHandler;
import com.example.hikermanagementapp.model.ObservationModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class AddObservationActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView imageView;
    private EditText dateObservation, timeObservation, commentObservation;
    private Button buttonSave, buttonView, buttonCapture;
    private FloatingActionButton dateFab, timeFab;
    private DatabaseHandler databaseHandler;
    private int observationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        imageView = findViewById(R.id.imageView);
        dateObservation = findViewById(R.id.dateObservation);
        timeObservation = findViewById(R.id.timeObservation);
        commentObservation = findViewById(R.id.commentObservation);
        buttonSave = findViewById(R.id.saveObservation);
        buttonView = findViewById(R.id.viewObservation);
        dateFab = findViewById(R.id.floatingActionButton3);
        timeFab = findViewById(R.id.floatingActionButton4);
        ImageButton buttonCapture = findViewById(R.id.buttonCapture);

        databaseHandler =  new DatabaseHandler(AddObservationActivity.this);

        // save obsevation
        buttonSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(AddObservationActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to save?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String date = dateObservation.getText().toString().trim();
                        String time = timeObservation.getText().toString().trim();
                        String comment = commentObservation.getText().toString().trim();
                        if (date.isEmpty() || time.isEmpty() || comment.isEmpty()) {
                            Toast.makeText(AddObservationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Bitmap profileImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                        ObservationModel newObservation =  new ObservationModel(date, time, comment, profileImage);
                        databaseHandler.addObservation(newObservation);
                        Toast.makeText(AddObservationActivity.this, "Contact saved successfully", Toast.LENGTH_SHORT).show();
                        dateObservation.getText().clear();
                        timeObservation.getText().clear();
                        commentObservation.getText().clear();
                        imageView.setImageResource(R.drawable.person);
                }
        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No, do nothing
                    }
                });

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //view obsevation
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ObservationModel> observations = databaseHandler.getAllObservations();
                Intent intent = new Intent(AddObservationActivity.this, ViewObservationActivity.class);
                startActivity(intent);
            }
        });
        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
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
                        dateObservation.setText(selectedDate);
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
                        timeObservation.setText(selectedTime);
                    }
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
            // Lấy ảnh từ thư viện
            Uri selectedImageUri = data.getData();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            // Lấy ảnh từ camera
            Bundle extras = data.getExtras();
            if (extras != null && extras.containsKey("data")) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
        }
    }

}
