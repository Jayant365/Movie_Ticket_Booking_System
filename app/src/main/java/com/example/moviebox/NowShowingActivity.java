package com.example.moviebox;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NowShowingActivity extends AppCompatActivity {

    SQLiteDatabase db;
    RecyclerView recyclerView;
    MyCustomAdapter adapter;
    int movieType;
    List<Movies> moviesList = new ArrayList<>();
    List<String> movieName = new ArrayList<>();
    List<Integer> movieImg = new ArrayList<>();
    List<String> movieIDList = new ArrayList<>();

    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_showing);

        Bundle bundle = getIntent().getExtras();

        //Check if there is a movie name
        if (bundle != null)
        {
            //0 for now showing and 1 for upcoming
            movieType = bundle.getInt("movieType");
        }

        titleTextView = findViewById(R.id.txtViewtitle);

        int startPoint;
        int endPoint;

        //Change title according to ProfileActivity button click
        if (movieType == 0) {
            titleTextView.setText("Now Showing");
            startPoint = 1;
            endPoint = 7;
        } else {
            titleTextView.setText("Upcoming");
            startPoint = 7;
            endPoint = 17;
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        createDB();
        createTables();

        addMovie();

        List<String[]> MyMovie = ReadCSV();
        for(int i = startPoint; i < endPoint; i++ ){
            String id = MyMovie.get(i)[0];
            String name = MyMovie.get(i)[1];
            String releaseDate = MyMovie.get(i)[2];
            String director = MyMovie.get(i)[3];
            String cast = MyMovie.get(i)[4];
            String duration= MyMovie.get(i)[5];
            String description = MyMovie.get(i)[6];
            String trailer = MyMovie.get(i)[7];

            addMovieInfo(id,name,releaseDate,director,cast,duration,description,trailer);
        }

         List<String> movName = browseMovieNameAccordingToNowShowingOrUpcoming();
         adapter = new MyCustomAdapter(this,movName,movieImg, movieIDList,movieType);
         recyclerView.setAdapter(adapter);
    }

   private void addMovie(){
        movieImg.add(R.drawable.terminator_dark_fate);
        movieImg.add(R.drawable.good_liar);
        movieImg.add(R.drawable.frozen_2);
        movieImg.add(R.drawable.zombieland_2);
        movieImg.add(R.drawable.midway);
        movieImg.add(R.drawable.charlie_angel);
        movieImg.add(R.drawable.chaoswalking);
        movieImg.add(R.drawable.grudge);
        movieImg.add(R.drawable.underwater);
        movieImg.add(R.drawable.badboysforlife);
        movieImg.add(R.drawable.dolittle);
        movieImg.add(R.drawable.thegentlemen);
        movieImg.add(R.drawable.theturning);
        movieImg.add(R.drawable.bloodhsot);
        movieImg.add(R.drawable.badtrip);
        movieImg.add(R.drawable.photograph);
    }

    private List<String[]> ReadCSV() // Create method to read from file
    {
        List<String[]> resulutList = new ArrayList<>();
        //Input Stream
        InputStream inputStream = getResources().openRawResource(R.raw.now_showing); // From where it reads
        BufferedReader reader =new BufferedReader(new InputStreamReader((inputStream)));

        try
        {

            String csvLine; // one line in file

            while((csvLine = reader.readLine()) != null)
            {
                //GEt movie name from csv if it is on index 1
  //              Movies movieObj = new Movies(csvLine.split(",")[1] ,R.drawable.charlie_angel);

    //            moviesList.add(movieObj);
                String[] row = csvLine.split(",");
                resulutList.add(row);
            }
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Error reading from file" + ex);
        }
        finally {
            try {
                inputStream.close(); //close the reading from file
            }
            catch (IOException ex)
            {
                throw new RuntimeException("Error while closing Input Stream" + ex);
            }
        }
        return resulutList;
    }


    //Database
    private void createDB() {
        try {
            db = openOrCreateDatabase("Movies.db", Context.MODE_PRIVATE, null);
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }catch(Exception ex){
            Log.i("Movie App: " , ex.getMessage());
        }
    }

    private void createTables(){
        try{
            String dropMoviesTableCmd = "DROP TABLE IF EXISTS movie;";

            //primary key MovieId,MovieName,ReleaseDate,Director,Cast,Duration,Description,Trailer
            String createMovieTableCmd = "CREATE TABLE movie " +
                    "(MovieId TEXT PRIMARY KEY, MovieName TEXT, ReleaseDate DATE, Director TEXT," +
                    " MovieCast TEXT, Duration TIME, Description TEXT, Trailer TEXT);";

            db.execSQL(dropMoviesTableCmd);
            db.execSQL(createMovieTableCmd);
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

        }catch(Exception ex){
            Log.i("Movie App: " , "Error creating tables");
        }
    }

    private void addMovieInfo(String id,String name,String releaseDate,
                              String director, String cast, String duration,
                              String description, String trailer){
        long result;
        ContentValues val = new ContentValues();
        val.put("MovieId" , id);
        val.put("MovieName", name);
        val.put("ReleaseDate", releaseDate);
        val.put("Director", director);
        val.put("MovieCast", cast);
        val.put("Duration", duration);
        val.put("Description", description);
        val.put("Trailer", trailer);
        result = db.insert("movie",null, val);
        if(result != -1){
            Log.i("Movie App: ","Inserted movie with id: " + id);
        }else{
            Log.i("Movie App: ", "ERROR Inserting movie with id: " + id);
        }
    }

    private List<String> browseMovieNameAccordingToNowShowingOrUpcoming(){
        List<String> Name = new ArrayList<>();

        String queryStr;

        try{
            queryStr = "SELECT * FROM movie;";
            Cursor cursor = db.rawQuery(queryStr, null);

            if(cursor != null){
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    String eachName = cursor.getString(1);
                    String eachID = cursor.getString(0);

                    Name.add(eachName);
                    movieIDList.add(eachID);

                    cursor.moveToNext();
                }
                cursor.close();
                db.close();
            }
        }catch (Exception ex){
            Log.i("Movie App : ",ex.getMessage());
        }
        return Name;
    }

}
