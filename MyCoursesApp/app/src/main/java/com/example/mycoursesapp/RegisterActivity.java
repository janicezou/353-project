package com.example.mycoursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    protected String message;

    public void onSubmitButtonClick(View v) {
        TextView tv = findViewById(R.id.statusField);

        EditText editTextUsername = findViewById(R.id.edit_text_username);
        EditText editTextEmail = findViewById(R.id.edit_text_email);
        EditText editTextPassword = findViewById(R.id.edit_text_password);
        Button submitButton = findViewById(R.id.button_submit);

        String name = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        // check if email ends with bico account
        if (!email.endsWith("haverford.edu") && !email.endsWith("brynmawr.edu")) {
            message = "Sign up is only for bico email accounts!";
            tv.setText(message);
            return;
        }

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                        try {
                            // assumes that there is a server running on the AVD's host on port 3000
                            // and that it has a /register endpoint that returns a JSON object with
                            // user data

                            URL url = new URL("http://10.0.2.2:3000/register?name="+name+"&email="+email+"&password="+password);

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();
//
//                            JSONObject jo = new JSONObject();
//                            jo.put("username", name);
//                            jo.put("email", email);
//                            jo.put("password", password);
//
//                            OutputStream os = conn.getOutputStream();
//                            os.write(jo.toString().getBytes());
//                            os.flush();

                            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                            }

                            Scanner in = new Scanner(conn.getInputStream());
                            String response = in.nextLine();
                            message = response;

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            message = e.toString();
                        }
                    }
            );

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(2, TimeUnit.SECONDS);
            tv.setText(message);

            // now we can go back to the main page
            // create an intent to start the Main Activity
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            // direct to main page
        }
        catch (Exception e) {
            // uh oh
            e.printStackTrace();
            message = e.toString();
            tv.setText(message);
        }
    }

    public void onBackButtonClick(View v) {
        // create an intent to start the Main Activity
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        // direct to main page
    }


}
