package com.example.mycoursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.naming.directory.SearchControls;

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
        intent.putExtra("email", email);
        startActivity(intent);
    }

}
