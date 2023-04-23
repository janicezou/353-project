package com.example.mycoursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        email = getIntent().getStringExtra("email");
    }

    public void onToEditNameButtonClick(View v) {
        Intent i = new Intent(this, EditNameActivity.class);
        i.putExtra("email", email);
        startActivity(i);
    }

    public void onToEditPasswordButtonClick(View v) {
        Intent i = new Intent(this, EditPasswordActivity.class);
        i.putExtra("email", email);
        startActivity(i);
    }

    public void onBackButtonClick(View v) {
        // create an intent to start the Main Activity
        Intent i = new Intent(this, LoginPageActivity.class);
        startActivity(i);
        // direct to main page
    }

}
