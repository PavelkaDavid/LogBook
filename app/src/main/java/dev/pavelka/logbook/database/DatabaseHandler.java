package dev.pavelka.logbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "logBook";
    private static final String TABLE_DRIVES = "drives";
    private static final String KEY_ID = "id";
    private static final String KEY_FROM_DATE = "fromDate";
    private static final String KEY_TO_DATE = "toDate";
    private static final String KEY_FROM = "fromText";
    private static final String KEY_TO = "toText";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_PRICE = "price";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DRIVES_TABLE = "CREATE TABLE " + TABLE_DRIVES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FROM_DATE + " TEXT,"
                + KEY_TO_DATE + " TEXT,"
                + KEY_FROM + " TEXT,"
                + KEY_TO + " TEXT,"
                + KEY_DISTANCE + " REAL,"
                + KEY_PRICE + " REAL" + ")";
        db.execSQL(CREATE_DRIVES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVES);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addDrive(Drive drive) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FROM_DATE, drive.getFromDate());
        values.put(KEY_TO_DATE, drive.getToDate());
        values.put(KEY_FROM, drive.getFrom());
        values.put(KEY_TO, drive.getTo());
        values.put(KEY_DISTANCE, drive.getDistance());
        values.put(KEY_PRICE, drive.getPrice());

        // Inserting Row
        db.insert(TABLE_DRIVES, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    public Drive getDrive(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DRIVES, new String[] { KEY_ID,
                        KEY_FROM_DATE, KEY_TO_DATE, KEY_FROM, KEY_TO, KEY_DISTANCE, KEY_PRICE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Drive drive = new Drive(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4),
                cursor.getDouble(5), cursor.getDouble(6));
        // return drive
        return drive;
    }

    // code to get all drives in a list view
    public List<Drive> getAllDrives() {
        List<Drive> driveList = new ArrayList<Drive>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DRIVES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Drive drive = new Drive();
                drive.setID(Integer.parseInt(cursor.getString(0)));
                drive.setFromDate(cursor.getString(1));
                drive.setToDate(cursor.getString(2));
                drive.setFrom(cursor.getString(3));
                drive.setTo(cursor.getString(4));
                drive.setDistance(cursor.getDouble(5));
                drive.setPrice(cursor.getDouble(6));
                // Adding drive to list
                driveList.add(drive);
            } while (cursor.moveToNext());
        }

        // return drive list
        return driveList;
    }

    // code to update the single drive
    public int updateDrive(Drive drive) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FROM_DATE, drive.getFromDate());
        values.put(KEY_TO_DATE, drive.getToDate());
        values.put(KEY_FROM, drive.getFrom());
        values.put(KEY_TO, drive.getTo());
        values.put(KEY_DISTANCE, drive.getDistance());
        values.put(KEY_PRICE, drive.getPrice());

        // updating row
        return db.update(TABLE_DRIVES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(drive.getID()) });
    }

    // Deleting single drive
    public void deleteDrive(Drive drive) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRIVES, KEY_ID + " = ?",
                new String[] { String.valueOf(drive.getID()) });
        db.close();
    }

    // Getting drives Count
    public int getDrivesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DRIVES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
