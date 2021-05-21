package com.example.moviebox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

// client ID 1088389327698-fm6qubp1sj71l3h61aic7jce1o3rihc3.apps.googleusercontent.com
// Client Secret wq6gO7zIL3AIWcNh4MK04e6W


public class MainActivity extends AppCompatActivity {


    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.mipmap.ic_launcher_moviepic);
        getSupportActionBar().setDisplayUseLogoEnabled(false);

        TextView textViewWelcome = findViewById(R.id.textViewWelcome);

        Button btnLogIn = findViewById(R.id.btnLogIn);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logInIntent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(logInIntent);
            }
        });
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(MainActivity.this, ResigisterActivity.class);
                startActivity(regIntent);
            }
        });


        googleSignIn();

        // Google Log out button temp

    }



    //Google Methods

    public void googleSignIn()
    {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso- google sign in options
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            //Toast.makeText(this, account.getDisplayName(), Toast.LENGTH_SHORT).show();
            // Signed in successfully, show authenticated UI.

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("mySP",0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userName",account.getDisplayName());
            editor.putString("userEmail", account.getEmail());
            editor.putString("loginMethod","google");
            editor.commit();

            Intent showHomeIntent = new Intent(MainActivity.this,ProfileActivity.class);
            startActivity(showHomeIntent);




        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart(){

        SharedPreferences pref = getApplicationContext().getSharedPreferences("mySP",0);
        SharedPreferences.Editor editor = pref.edit();

        if (pref.contains("userName"))
        {
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        }

        super.onStart();
    }

}
