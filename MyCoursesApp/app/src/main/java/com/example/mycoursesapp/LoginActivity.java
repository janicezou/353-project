package com.example.mycoursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    protected String message;

    public void onSubmitButtonClick(View v) {

        TextView tv = findViewById(R.id.statusField);
        EditText editTextEmail = findViewById(R.id.edit_text_email);
        EditText editTextPassword = findViewById(R.id.edit_text_password);

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    // assumes that there is a server running on the AVD's host on port 3000
                    // and that it has a /login endpoint

                    URL url = new URL("http://10.0.2.2:3000/login");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.connect();

                    JSONObject jo = new JSONObject();
                    jo.put("email", email);
                    jo.put("password", password);

                    OutputStream os = conn.getOutputStream();
                    os.write(jo.toString().getBytes());
                    os.flush();

                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                    }

                    Scanner in = new Scanner(conn.getInputStream());
                    String response = in.nextLine();
                    message = response;

                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        // login successful, go to login in page
                        Intent intent = new Intent(this, LoginPageActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    message = e.toString();
                }
            });

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(2, TimeUnit.SECONDS);

            // now we can set the status in the TextView
            tv.setText(message);

        } catch (Exception e) {
            e.printStackTrace();
            message = e.toString();
            tv.setText(message);
        }
    }
}
