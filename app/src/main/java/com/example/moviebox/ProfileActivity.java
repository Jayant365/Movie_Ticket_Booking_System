package com.example.moviebox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button buttonNowShowing = findViewById(R.id.buttonNowShowing);
        Button buttonUpcoming = findViewById(R.id.buttonUpcoming);
        Button buttonViewProfile = findViewById(R.id.buttonViewProfile);


        buttonNowShowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(ProfileActivity.this,NowShowingActivity.class);
                intent.putExtra("movieType",0); // For the movie data in terminator activity
                startActivity(intent);
            }
        });

        buttonUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileActivity.this,NowShowingActivity.class);
                intent.putExtra("movieType",1); // For the movie data in terminator activity
                startActivity(intent);
            }
        });

        buttonViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(ProfileActivity.this, ProfileDesActivity.class);
                startActivity(profileIntent);
            }
        });
    }
}
