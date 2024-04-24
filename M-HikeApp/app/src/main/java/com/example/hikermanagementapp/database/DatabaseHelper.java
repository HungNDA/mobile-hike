package com.example.hikermanagementapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hikermanagementapp.model.Hike;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hike_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_HIKES = "hikes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HIKE_NAME = "hike_name";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_NUMBER_OF_DAYS = "number_of_days";
    private static final String COLUMN_NUMBER_LENGTH = "length";
    private static final String COLUMN_PARKING = "parking";
    private static final String COLUMN_DIFFICULTY = "difficulty";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_REQUIRED_GEAR = "required_gear";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_HIKES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HIKE_NAME + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_NUMBER_OF_DAYS + " INTEGER, " +
                COLUMN_NUMBER_LENGTH + " TEXT, " +
                COLUMN_PARKING + " INTERGER, " +
                COLUMN_DIFFICULTY + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_REQUIRED_GEAR + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý khi có sự thay đổi phiên bản cơ sở dữ liệu (nếu cần)
    }

    public long addHike(String name, String location, String date, String time, int number, String length, int parking, String difficult, String description, String gear) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HIKE_NAME, name);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_NUMBER_OF_DAYS, number);
        values.put(COLUMN_NUMBER_LENGTH, length);
        values.put(COLUMN_PARKING, parking);
        values.put(COLUMN_DIFFICULTY, difficult);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_REQUIRED_GEAR, gear);

        long newRowId = db.insert(TABLE_HIKES, null, values);
        db.close();
        return newRowId;
    }

    public List<Hike> getAllHikes() {
        List<Hike> hikes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID, COLUMN_HIKE_NAME, COLUMN_LOCATION,
                COLUMN_DATE, COLUMN_TIME, COLUMN_NUMBER_OF_DAYS, COLUMN_NUMBER_LENGTH,
                COLUMN_DESCRIPTION, COLUMN_PARKING, COLUMN_DIFFICULTY,
                COLUMN_REQUIRED_GEAR
        };
        Cursor cursor = db.query(TABLE_HIKES, projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            long hikeId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String hikeName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_NAME));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
            int numberOfDays = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_OF_DAYS));
            String lengthText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_LENGTH));
            int parking = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PARKING));
            String difficulty = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY));
            String requiredGear = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUIRED_GEAR));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));

            Hike hike = new Hike(hikeId, hikeName, location, date, time, numberOfDays, description, lengthText, parking, difficulty, requiredGear);
            hikes.add(hike);
        }

        cursor.close();
        db.close();
        return hikes;
    }

    public Hike getHikeById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID, COLUMN_HIKE_NAME, COLUMN_LOCATION,
                COLUMN_DATE, COLUMN_TIME, COLUMN_NUMBER_OF_DAYS, COLUMN_NUMBER_LENGTH,
                COLUMN_DESCRIPTION, COLUMN_PARKING, COLUMN_DIFFICULTY,
                COLUMN_REQUIRED_GEAR
        };

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(TABLE_HIKES, projection, selection, selectionArgs, null, null, null);

        Hike hike = null;

        while (cursor.moveToNext()) {
            long hikeId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String hikeName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_NAME));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
            int numberOfDays = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_OF_DAYS));
            String lengthText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_LENGTH));
            int parking = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PARKING));
            String difficulty = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY));
            String requiredGear = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUIRED_GEAR));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));

            hike = new Hike(hikeId, hikeName, location, date, time, numberOfDays, description, lengthText, parking, difficulty, requiredGear);
        }

        cursor.close();
        db.close();
        return hike;
    }

    public void editHike(long hikeId, String name, String location, String date, String time, int number, String length, int parking, String difficult, String description, String gear, EditHikeCallback callback) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HIKE_NAME, name);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_NUMBER_OF_DAYS, number);
        values.put(COLUMN_NUMBER_LENGTH, length);
        values.put(COLUMN_PARKING, parking);
        values.put(COLUMN_DIFFICULTY, difficult);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_REQUIRED_GEAR, gear);

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(hikeId)};

        try {
            int rowsUpdated = db.update(TABLE_HIKES, values, selection, selectionArgs);
            db.close();

            if (rowsUpdated > 0) {
                callback.onEditHikeSuccess();
            } else {
                callback.onEditHikeFailure("No rows updated");
            }
        } catch (Exception e) {
            callback.onEditHikeFailure("Error updating hike: " + e.getMessage());
        }
    }

    // Define an interface for the callback
    public interface EditHikeCallback {
        void onEditHikeSuccess();
        void onEditHikeFailure(String errorMessage);
    }

    public boolean deleteHike(long hikeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(hikeId)};
        try {
            int rowsDeleted = db.delete(TABLE_HIKES, selection, selectionArgs);
            db.close();
            if (rowsDeleted > 0) {
                Log.d("TAG", "Hike deleted successfully.");
                return true;
            } else {
                Log.d("TAG", "No hike deleted.");
                return false;
            }
        } catch (Exception e) {
            Log.e("TAG", "Error deleting hike: " + e.getMessage());
            return false;
        }
    }
}
