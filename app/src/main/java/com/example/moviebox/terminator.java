package com.example.moviebox;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class terminator extends AppCompatActivity {

    SQLiteDatabase db;
    static String movieID = "";
    String movieTrailerURL;
    String movieName;
    String movieRd;
    String movieDir;
    String movieCast;
    String movieDur;
    String movieDesc;
    int movieType;

    //StringBuilder outputText = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminator);

        //Get extra bundle
        Bundle bundle = getIntent().getExtras();

        //Check if there is a movie name
        if (bundle != null)
        {
            movieID = bundle.getString("movieID");
            movieType = bundle.getInt("movieType");
        }

        OpenDB();
        browseMovies(movieID);

        ImageButton Ib=(ImageButton) findViewById(R.id.imageButton2);

        TextView mDesc = findViewById(R.id.DescriptionTDF);
        TextView mDir = findViewById(R.id.directorTDF);
        TextView mDate = findViewById(R.id.dateTDF);
        TextView mCast = findViewById(R.id.castTDF);
        TextView mTime = findViewById(R.id.timeTDF);
        Button bookTicketsButton = findViewById(R.id.buttonBook);

        if (movieType == 0)
        {
            bookTicketsButton.setVisibility(View.VISIBLE);
        }
        else
        {
            bookTicketsButton.setVisibility(View.GONE);
        }

        bookTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(terminator.this, SeatActivity.class);
                intent.putExtra("movieName", movieName);
                startActivity(intent);
            }
        });


        mDesc.setText(movieDesc);
        mDir.setText(movieDir);
        mDate.setText(movieRd);
        mCast.setText(movieCast);
        mTime.setText(movieDur);


        Ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movieTrailerURL)));
            }
        });
    }

    private void OpenDB() // To open the database so that it can be retrieved
    {
        try {
            // If the file already exists it will not create it only open it
            db = openOrCreateDatabase("Movies.db", MODE_PRIVATE , null);
        }
        catch (Exception ex)
        {
            Log.e("Movie App:", "Error in opening dB");
        }
    }

    private void browseMovies(String id)
    {

        String queryStr = "SELECT * FROM movie WHERE MovieId = ?;";
        try
        {
            Log.e("", id);
            Cursor cursor = db.rawQuery(queryStr, new String[]{String.valueOf(movieID)});

            if(cursor != null) {
                cursor.moveToFirst();
                movieName = cursor.getString(1);
                movieRd = cursor.getString(2);
                movieDir = cursor.getString(3);
                movieCast = cursor.getString(4);
                movieDur = cursor.getString(5);
                movieDesc = cursor.getString(6);
                movieTrailerURL = cursor.getString(7);

                cursor.moveToNext();
            }
        }
        catch (Exception ex)
        {
            Log.e("DB Demo", ex.getMessage());
        }

    }



}
