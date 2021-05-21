package com.example.moviebox;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResigisterActivity extends AppCompatActivity {

    String namePattern = "[a-zA-Z ]+"; // For name
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // For email

    private CustomDatabaseHelper customDatabaseHelper;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resigister);

        final EditText editTextNameReg = findViewById(R.id.editTextNameReg);
        final EditText editTextRegUsername = findViewById(R.id.editTextRegUsername);
        final EditText editTextRegPass = findViewById(R.id.editTextRegPass);
        final EditText editTextRegConfirmPass = findViewById(R.id.editTextRegConfirmPass);
        Button btnSubmitReg = findViewById(R.id.btnSubmitReg);

        final String name = editTextRegUsername.getText().toString();


        customDatabaseHelper = new CustomDatabaseHelper(this);

        //Blank constructor called here. No arguments passed. Arguments will be passed later.
        userInfo = new UserInfo();

        //Validations for all the fields while registration as a new user

        btnSubmitReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextNameReg.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(ResigisterActivity.this, "Please enter name.", Toast.LENGTH_SHORT).show();
                }
                else if (!editTextNameReg.getText().toString().trim().matches(namePattern))
                {
                    Toast.makeText(ResigisterActivity.this, "Only alphabets allowed in name", Toast.LENGTH_SHORT).show();
                }
                else if(editTextRegUsername.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(ResigisterActivity.this, "Please enter email.", Toast.LENGTH_SHORT).show();
                }
                else if(!editTextRegUsername.getText().toString().trim().matches(emailPattern))
                {
                    Toast.makeText(ResigisterActivity.this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                }
                else if(editTextRegPass.getText().toString().isEmpty())
                {
                    Toast.makeText(ResigisterActivity.this, "Please enter password.", Toast.LENGTH_SHORT).show();
                }
                else if(editTextRegPass.getText().toString().length() <= 7)
                {
                    Toast.makeText(ResigisterActivity.this, "Password should be 8 char long.", Toast.LENGTH_SHORT).show();
                }
                else if(editTextRegConfirmPass.getText().toString().isEmpty())
                {
                    Toast.makeText(ResigisterActivity.this, "Please enter confirm password.", Toast.LENGTH_SHORT).show();
                }
                else if(editTextRegConfirmPass.getText().toString().length() <= 7)
                {
                    Toast.makeText(ResigisterActivity.this, "Confirm password should be 8 char long.", Toast.LENGTH_SHORT).show();
                }
                else if(!editTextRegConfirmPass.getText().toString().equals(editTextRegPass.getText().toString()))
                {
                    Toast.makeText(ResigisterActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (!customDatabaseHelper.checkUser(editTextRegUsername.getText().toString().trim())) {

                        userInfo.setUserName(editTextNameReg.getText().toString().trim());
                        userInfo.setUserEmail(editTextRegUsername.getText().toString().trim());
                        userInfo.setUserPassword(editTextRegPass.getText().toString().trim());

                        customDatabaseHelper.addUser(userInfo);

                        // Toast to show success message that record saved successfully
                        Toast.makeText(ResigisterActivity.this, "Successfully registered..", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResigisterActivity.this, ProfileActivity.class);
                        startActivity(intent);

                    } else {
                        //Toast to show error message that record already exists
                        Toast.makeText(ResigisterActivity.this, "Email already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }



        });
    }

}