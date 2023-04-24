package com.example.mycoursesapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class EditPasswordActivity extends AppCompatActivity {
    private TextView currentPasswordTextView;
    private EditText newPasswordEditText;
    private Button updatePasswordButton;

    protected String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        email = getIntent().getStringExtra("email");

        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = newPasswordEditText.getText().toString();
                updatePassword(email, newPassword);
            }
        });
    }

    protected String message;
    public void updatePassword(String email, String newPassword) {
        TextView tv = findViewById(R.id.statusField);
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    // assumes that there is a server running on the AVD's host on port 3000

                    URL url = new URL("http://10.0.2.2:3000/editPassword");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.connect();

                    String requestBody = "email=" + email + "&newPassword=" + newPassword;

                    // Write the request body to the output stream
                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(requestBody.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    Scanner in = new Scanner(conn.getInputStream());
                    String response = in.nextLine();
                    JSONObject json = new JSONObject(response);
                    message = json.getString("message");

                    if (conn.getResponseCode() == 200) {
                        // edit successful, go to profile page
                        Intent intent = new Intent(this, ProfileActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        // login failed, show error message
                        tv.setText(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    message = "Edit Password failed";
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

    public void onBackButtonClick(View v) {
        // create an intent to start the Profile Activity
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("email", email);
        startActivity(i);
        // direct to profile page
    }

}

