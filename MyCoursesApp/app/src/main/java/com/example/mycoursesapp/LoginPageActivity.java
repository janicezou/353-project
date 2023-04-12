package com.example.mycoursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPageActivity extends AppCompatActivity {

    String email = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        email = getIntent().getStringExtra("email");
    }

    public void onAddCommentButtonClick(View v) {
        Intent intent = new Intent(this, AddComments.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public void onViewCommentButtonClick(View v){
        Intent intent = new Intent(this, ViewComments.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

   public void onToSearchButtonClick(View v){
       Intent intent = new Intent(this, Search.class);
       startActivity(intent);
   }

    public void onLogoutButtonClick(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
