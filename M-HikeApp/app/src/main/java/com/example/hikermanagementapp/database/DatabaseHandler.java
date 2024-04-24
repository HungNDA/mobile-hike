package com.example.hikermanagementapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.example.hikermanagementapp.model.ObservationModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ObservationDatabase";
    private static final String TABLE_OBSERVATIONS = "observations";
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_PROFILE_IMAGE = "profile_image";
    private static final String CREATE_TABLE_OBSERVATIONS = "CREATE TABLE " + TABLE_OBSERVATIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DATE + " TEXT,"
            + KEY_TIME + " TEXT,"
            + KEY_COMMENT + " TEXT,"
            + KEY_PROFILE_IMAGE + " BLOB"
            + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_OBSERVATIONS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);
        onCreate(db);
    }

    // Add a new observation
    public void addObservation(ObservationModel observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, observation.getDate());
        values.put(KEY_TIME, observation.getTime());
        values.put(KEY_COMMENT, observation.getComment());

        byte[] profileImageBytes = DatabaseUtils.getBytes(observation.getProfileImage());
        values.put(KEY_PROFILE_IMAGE, profileImageBytes);

        // Inserting Row
        db.insert(TABLE_OBSERVATIONS, null, values);
        db.close(); // Closing database connection
    }

    // Get all observations
    public List<ObservationModel> getAllObservations() {
        List<ObservationModel> observationList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_OBSERVATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(selectQuery, null);

            // Check if the cursor is not null and has rows
            if (cursor != null && cursor.moveToFirst()) {
                int dateIndex = cursor.getColumnIndex(KEY_DATE);
                int timeIndex = cursor.getColumnIndex(KEY_TIME);
                int commentIndex = cursor.getColumnIndex(KEY_COMMENT);
                int imageIndex = cursor.getColumnIndex(KEY_PROFILE_IMAGE);
                int idIndex = cursor.getColumnIndex(KEY_ID);

                // Loop through rows and add to the list
                do {
                    // Convert byte array to Bitmap
                    byte[] profileImageBytes = cursor.getBlob(imageIndex);
                    Bitmap profileImage = DatabaseUtils.getImage(profileImageBytes);

                    ObservationModel observation = new ObservationModel(
                            cursor.getString(dateIndex),
                            cursor.getString(timeIndex),
                            cursor.getString(commentIndex),
                            profileImage);
                    observation.setId(cursor.getLong(idIndex));
                    observationList.add(observation);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return observationList;
    }

    public ObservationModel getObservationById(long observationId) {
        ObservationModel observation = null;
        String selectQuery = "SELECT * FROM " + TABLE_OBSERVATIONS + " WHERE " + KEY_ID + " = " + observationId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(selectQuery, null);

            // Check if the cursor is not null and has rows
            if (cursor != null && cursor.moveToFirst()) {
                int dateIndex = cursor.getColumnIndex(KEY_DATE);
                int timeIndex = cursor.getColumnIndex(KEY_TIME);
                int commentIndex = cursor.getColumnIndex(KEY_COMMENT);
                int imageIndex = cursor.getColumnIndex(KEY_PROFILE_IMAGE);
                int idIndex = cursor.getColumnIndex(KEY_ID);

                // Loop through rows and add to the list
                do {
                    // Convert byte array to Bitmap
                    byte[] profileImageBytes = cursor.getBlob(imageIndex);
                    Bitmap profileImage = DatabaseUtils.getImage(profileImageBytes);

                    observation = new ObservationModel(
                            cursor.getString(dateIndex),
                            cursor.getString(timeIndex),
                            cursor.getString(commentIndex),
                            profileImage);
                    observation.setId(cursor.getLong(idIndex));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return observation;
    }

    // Edit observation
    public void editObservation(long observationId, String newDate, String newTime, String newComment, Bitmap profileImage, EditObservationCallBack callBack) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, newDate);
        values.put(KEY_TIME, newTime);
        values.put(KEY_COMMENT, newComment);

        // Convert Bitmap to byte array
        byte[] profileImageBytes = DatabaseUtils.getBytes(profileImage);
        values.put(KEY_PROFILE_IMAGE, profileImageBytes);

        int rowsAffected = db.update(TABLE_OBSERVATIONS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(observationId)});

        if (rowsAffected > 0) {
            callBack.onEditObservationSuccess();
        } else {
            callBack.onEditObservationFailure("No rows were affected during the update.");
        }

        db.close();
    }

    public interface EditObservationCallBack {
        void onEditObservationSuccess();
        void onEditObservationFailure(String errorMessage);
    }

    // Delete observation
    public void deleteObservation(long observationId, DeleteObservationCallBack callBack) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_OBSERVATIONS, KEY_ID + " = ?",
                new String[]{String.valueOf(observationId)});

        if (rowsAffected > 0) {
            callBack.onDeleteObservationSuccess();
        } else {
            callBack.onDeleteObservationFailure("No rows were affected during the delete operation.");
        }

        db.close();
    }

    public interface DeleteObservationCallBack {
        void onDeleteObservationSuccess();

        void onDeleteObservationFailure(String errorMessage);
    }
}
