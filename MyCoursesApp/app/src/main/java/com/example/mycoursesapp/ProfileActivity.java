package com.example.mycoursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProfileActivity extends AppCompatActivity {

    String email;
    protected String message;
    protected String currentUsername;
    protected final String failedCurrentUsername = "Finding username failed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        email = getIntent().getStringExtra("email");
        TextView textView = findViewById(R.id.profileUsernameTextView);
        String m = "Hello, " + getName();
        textView.setText(m);
    }

    public String getName() {
        if (currentUsername == null || currentUsername.length() == 0) setName();
        return currentUsername;
    }

    public void setName() {
        TextView tv = findViewById(R.id.statusField);
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    URL url = new URL("http://10.0.2.2:3000/getUser/" + email);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(conn.getInputStream());
                    String response = in.nextLine();
                    System.out.println("This is the response: " + response);
                    JSONObject json = new JSONObject(response);
                    message = json.getString("message");

                    if (conn.getResponseCode() == 200) {
                        currentUsername = message;
//                        System.out.println("This is the current username: " + currentUsername);
                    } else {
                        // login failed, show error message
                        tv.setText(failedCurrentUsername);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            executor.awaitTermination(2, TimeUnit.SECONDS);

//            tv.setText(failedCurrentUsername);

        } catch (Exception e) {
            e.printStackTrace();
//            message = e.toString();
//            tv.setText(message);
        }
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
