package com.example.mycoursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class AddComments extends AppCompatActivity {
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        email = getIntent().getStringExtra("email");
    }

    public void onAddCommentAddButtonClick(View v) {
        EditText edit_course_number = findViewById(R.id.course_number);
        EditText edit_rating = findViewById(R.id.rating);
        EditText edit_comment = findViewById(R.id.comment_text);

        String course_number = edit_course_number.getText().toString();
        String rating = edit_rating.getText().toString();
        String comment = edit_comment.getText().toString();
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    // assumes that there is a server running on the AVD's host on port 3000
                    // and that it has a /login endpoint
                    String url_string = "http://10.0.2.2:3000/addComment/";
                    url_string += course_number;
                    URL url = new URL(url_string);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.connect();

                    JSONObject jo = new JSONObject();
                    jo.put("text", comment);
                    jo.put("email", email);
                    jo.put("rating", rating);

                    OutputStream os = conn.getOutputStream();
                    os.write(jo.toString().getBytes());
                    os.flush();

                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                    }

                    Scanner in = new Scanner(conn.getInputStream());
                    String response = in.nextLine();

                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(2, TimeUnit.SECONDS);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
