package com.example.mycoursesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity {

    protected String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.statusField);

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    URL url = new URL("http://10.0.2.2:3000/test");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();

                    JSONObject jo = new JSONObject(response);

                    message = jo.getString("message");

                }
                catch (Exception e) {
                    e.printStackTrace();
                    message = e.toString();
                }
            }
            );

            executor.awaitTermination(2, TimeUnit.SECONDS);
            tv.setText(message);
        }
        catch (Exception e) {
            e.printStackTrace();
            tv.setText(e.toString());
        }
    }

    public void onToLogInButtonClick(View v) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        // direct to login page
    }

    public void onToSignUpButtonClick(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        // direct to sign up page
    }
}