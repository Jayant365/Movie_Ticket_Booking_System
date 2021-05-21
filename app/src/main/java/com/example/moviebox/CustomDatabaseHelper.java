package com.example.moviebox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CustomDatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Movies.db";

    // User table name
    private static final String TABLE_USER = "UserRegData";
    private static final String TABLE_SEAT = "Seat";


    // User Table Columns names
    //Reg rows
    private static final String COLUMN_USER_ID = "userID";
    private static final String COLUMN_USER_NAME = "userName";
    private static final String COLUMN_USER_EMAIL = "userEmail";
    private static final String COLUMN_USER_PASSWORD = "userPassword";
    //Seat row
    private static final String COLUMN_SEAT_ID = "seatID";
    private static final String COLUMN_MOVIE_NAME = "movieName";
    private static final String COLUMN_SEAT_NUMBER = "seatNumber";
    private static final String COLUMN_MOVIE_DATE = "movieDate";


    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    private String CREATE_SEAT_TABLE = "CREATE TABLE " + TABLE_SEAT + "("
            + COLUMN_SEAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MOVIE_NAME + " TEXT,"
        + COLUMN_SEAT_NUMBER + " TEXT," + COLUMN_MOVIE_DATE + " TEXT" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_SEAT_TABLE = "DROP TABLE IF EXISTS " + TABLE_SEAT;


    public CustomDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create Table method
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_SEAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist for new updated SQLite
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_SEAT_TABLE);

        // Create tables again
        onCreate(db);
    }

    public void addUser(UserInfo userObject) {

        //get instance of db to write in db
        SQLiteDatabase db = this.getWritableDatabase();

        //values to put in db
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, userObject.getUserName());
        values.put(COLUMN_USER_EMAIL, userObject.getUserEmail());
        values.put(COLUMN_USER_PASSWORD, userObject.getUserPassword());

        // Inserting Row
        long result = db.insert(TABLE_USER, null, values);

        if(result != -1){
            Log.i("Movie App: ","Inserted user with id: " + userObject.getUserName());
        }else{
            Log.i("Movie App: ", "ERROR Inserting user with id: " + userObject.getUserName());
        }
        db.close();
    }

    //This method will check if user exists , return true or false
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public void bookTicketForSeatAndDate(SeatInfo seatInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MOVIE_NAME, seatInfo.movieName);
        values.put(COLUMN_SEAT_NUMBER, seatInfo.seatNumber);
        values.put(COLUMN_MOVIE_DATE, seatInfo.movieDate);


        // Inserting Row
        long result = db.insert(TABLE_SEAT, null, values);

        if(result != -1){
            Log.i("Movie App: ","Booked ticket with number: " + seatInfo.seatNumber);
        }else{
            Log.i("Movie App: ", "ERROR booking ticket with number: " + seatInfo.seatNumber);
        }
        db.close();
    }

    //To get all booked tickets
    public List<SeatInfo> getAllTicketsForMovie(String movie, String date) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_SEAT_ID,
                COLUMN_MOVIE_NAME,
                COLUMN_SEAT_NUMBER,
                COLUMN_MOVIE_DATE
        };
        // sorting orders
        String sortOrder =
                COLUMN_SEAT_ID + " ASC";

        // selection criteria
        String selection = COLUMN_MOVIE_NAME + " = ?" + " AND " + COLUMN_MOVIE_DATE + " = ?";

        // selection arguments
        String[] selectionArgs = {movie,date};

        //Seat info list
        List<SeatInfo> seatList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_SEAT, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list if it has values
        if (cursor.moveToFirst()) {
            do {
                SeatInfo seatData = new SeatInfo();
                seatData.setSeatNumber(cursor.getString(cursor.getColumnIndex(COLUMN_SEAT_NUMBER)));
                seatData.setMovieName(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_NAME)));
                seatData.setMovieDate(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_DATE)));

                // Adding user record to list
                seatList.add(seatData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return seatList;
    }

    //This method will return all the user data
    public UserInfo getUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL
        };
        SQLiteDatabase db = this.getReadableDatabase();

        UserInfo userData = new UserInfo();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        if (cursor.moveToFirst())
        {
                userData.setUserName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                userData.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
        }
        cursor.close();
        db.close();

        return userData;

    }


}
