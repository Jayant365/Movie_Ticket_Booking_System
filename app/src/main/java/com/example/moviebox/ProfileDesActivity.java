package com.example.moviebox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ProfileDesActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;



    TextView textViewName;
    TextView textViewEmail;

    String loginMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_des);

        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        Button buttonLogout = findViewById(R.id.buttonLogout);
        Button buttonHome = findViewById(R.id.buttonHome);

        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("mySP",0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        textViewName.setText(sharedPreferences.getString("userName",""));
        textViewEmail.setText(sharedPreferences.getString("userEmail",""));

        loginMethod = sharedPreferences.getString("loginMethod","");

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso- google sign in options
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (loginMethod.equals("google"))
                {
                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            editor.clear();
                            editor.commit();
                            startActivity(new Intent(ProfileDesActivity.this, MainActivity.class));

                        }
                    });
                }
                else
                {
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(ProfileDesActivity.this, MainActivity.class));
                }
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileDesActivity.this, MainActivity.class));
            }
        });
    }


}
