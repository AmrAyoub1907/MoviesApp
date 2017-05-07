package com.amrayoub.moviesapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables

    // Database Version
    private static final int DATABASE_VERSION = 7;

    // Database Name
    private static final String DATABASE_NAME = "MovieApp_db";

    // Contacts table name
    private static final String TABLE_NAME = "MoviesTable";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_RATE = "rate";
    private static final String KEY_REVIEWS = "reviews";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_PHOTO = "photo";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " TEXT PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_RATE + " TEXT, "
                + KEY_DATE + " TEXT, "
                + KEY_OVERVIEW + " TEXT, "
                + KEY_REVIEWS + " TEXT, "
                + KEY_PHOTO + " TEXT"
                +")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addContact(MovieInfo contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, contact.getMovieID());
        values.put(KEY_NAME, contact.getMovieName());
        values.put(KEY_RATE, contact.getMovieRate());
        values.put(KEY_DATE, contact.getMovieReleaseDate());
        values.put(KEY_OVERVIEW, contact.getMovieOverview());
        values.put(KEY_REVIEWS, contact.getMovieReviews());
        values.put(KEY_PHOTO, contact.getMovieImageUrl().substring(31,contact.getMovieImageUrl().length()));
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    MovieInfo getContact(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_NAME, KEY_DATE,KEY_OVERVIEW,KEY_RATE,KEY_REVIEWS,KEY_PHOTO}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MovieInfo contact = new MovieInfo(cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
        // return contact
        return contact;
    }

    // Getting All Contacts
    public ArrayList<MovieInfo> getAllContacts() {
        ArrayList<MovieInfo> contactList = new ArrayList<MovieInfo>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MovieInfo contact = new MovieInfo();
                contact.setMovieID(cursor.getString(0));
                contact.setMovieName(cursor.getString(1));
                contact.setMovieRate(cursor.getString(2));
                contact.setMovieReleaseDate(cursor.getString(3));
                contact.setMovieOverview(cursor.getString(4));
                contact.setMovieReviews(cursor.getString(5));
                contact.setMovieImageUrl(cursor.getString(6));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateContact(MovieInfo contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getMovieName());
        values.put(KEY_RATE, contact.getMovieRate());
        values.put(KEY_DATE, contact.getMovieReleaseDate());
        values.put(KEY_OVERVIEW, contact.getMovieOverview());
        values.put(KEY_REVIEWS, contact.getMovieReviews());
        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getMovieID()) });
    }

    // Deleting single contact
    public void deleteContact(MovieInfo contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getMovieID()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount(String fieldValue) {
        String countQuery = "SELECT * FROM " + TABLE_NAME + " where " + KEY_ID + " like " + fieldValue;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        int x =cursor.getCount();
        //cursor.close();
        // return count
        return x;
    }

}