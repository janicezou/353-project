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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onToLogInButtonClick(View v) {
        // create an intent to start the Signup Activity
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        // direct to sign up page
    }

    public void onToSignUpButtonClick(View v) {
        // create an intent to start the Signup Activity
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        // direct to sign up page
    }

    protected String message;

    public void onConnectButtonClick(Button bv) {

        TextView tv = findViewById(R.id.statusField);

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                        try {
                            // assumes that there is a server running on the AVD's host on port 3000
                            // and that it has a /test endpoint that returns a JSON object with
                            // a field called "message"

                            URL url = new URL("http://10.0.2.2:3000/test");

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();

                            Scanner in = new Scanner(url.openStream());
                            String response = in.nextLine();

                            JSONObject jo = new JSONObject(response);

                            // need to set the instance variable in the Activity object
                            // because we cannot directly access the TextView from here
                            message = jo.getString("message");

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            message = e.toString();
                        }
                    }
            );

            executor.awaitTermination(2, TimeUnit.SECONDS);

            bv.setBackgroundColor(Color.GREEN);
            bv.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.checkbox_on_background, 0, 0, 0);
            tv.setText(message);
        }
        catch (Exception e) {
            e.printStackTrace();
            tv.setText(e.toString());
        }
    }
}