package com.example.moviebox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {

    EditText editTextLogInUserName;
    EditText editTextLogInPass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // For email

    private CustomDatabaseHelper customDatabaseHelper;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editTextLogInUserName = findViewById(R.id.editTextLogInUserName);
        editTextLogInPass = findViewById(R.id.editTextLogInPass);
        Button buttonLogInSubmit = findViewById(R.id.buttonLogInSubmit);

        customDatabaseHelper = new CustomDatabaseHelper(this);
        userInfo = new UserInfo();

        buttonLogInSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction();
            }
        });

    }

    public void loginAction()
    {
        if(editTextLogInUserName.getText().toString().trim().isEmpty())
        {
            Toast.makeText(LogInActivity.this, "Please enter email.", Toast.LENGTH_SHORT).show();
        }
        else if(!editTextLogInUserName.getText().toString().trim().matches(emailPattern))
        {
            Toast.makeText(LogInActivity.this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
        }
        else if(editTextLogInPass.getText().toString().isEmpty())
        {
            Toast.makeText(LogInActivity.this, "Please enter password.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (customDatabaseHelper.checkUser(editTextLogInUserName.getText().toString().trim()
                    , editTextLogInPass.getText().toString().trim())) {

                UserInfo userData = customDatabaseHelper.getUser(editTextLogInUserName.getText().toString().trim());

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("mySP",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userName",userData.getUserName());
                editor.putString("userEmail", userData.getUserEmail());
                editor.putString("loginMethod","normal");
                editor.commit();

                Intent intent = new Intent(LogInActivity.this, ProfileActivity.class);
                startActivity(intent);


            } else {
                // Toast to show success message that record is wrong
                Toast.makeText(this, "Username or password incorrect.", Toast.LENGTH_SHORT).show();
            }
        }


    }



}
