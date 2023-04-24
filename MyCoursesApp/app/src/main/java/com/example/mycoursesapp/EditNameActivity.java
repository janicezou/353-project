package com.example.mycoursesapp;

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

public class EditNameActivity extends AppCompatActivity {
    private TextView currentUsernameTextView;
    private EditText newUsernameEditText;
    private Button updateUsernameButton;

    protected String email;

    protected String currentUsername;
    protected final String failedCurrentUsername = "Finding username failed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        email = getIntent().getStringExtra("email");

        currentUsernameTextView = findViewById(R.id.currentUsernameTextView);
        newUsernameEditText = findViewById(R.id.newUsernameEditText);
        updateUsernameButton = findViewById(R.id.updateUsernameButton);

        // Set the current username on the TextView
        currentUsernameTextView.setText("Current Username: " + getCurrentUsername());

        updateUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUsername = newUsernameEditText.getText().toString();
                updateUsername(email, newUsername);
            }
        });
    }

    public String getCurrentUsername() {
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
                        System.out.println("This is the current username: " + currentUsername);
                    } else {
                        // login failed, show error message
                        tv.setText(failedCurrentUsername);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    message = "Finding username failed";
                }
            });
            executor.awaitTermination(2, TimeUnit.SECONDS);

            tv.setText(failedCurrentUsername);

        } catch (Exception e) {
            e.printStackTrace();
            message = e.toString();
            tv.setText(message);
        }
        return currentUsername;
    }

    protected String message;
    public void updateUsername(String email, String newUsername) {
        TextView tv = findViewById(R.id.statusField);
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    // assumes that there is a server running on the AVD's host on port 3000

                    URL url = new URL("http://10.0.2.2:3000/editUsername");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.connect();

                    String requestBody = "email=" + email + "&newUsername=" + newUsername;

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
                    message = "Edit Username failed";
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
