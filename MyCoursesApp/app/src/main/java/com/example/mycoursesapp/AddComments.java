package com.example.mycoursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AddComments extends AppCompatActivity {
    protected String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        email = getIntent().getStringExtra("email");
    }

    protected String message;

    public void backAddCommentAddButtonClick(View v){
        Intent intent = new Intent(this, LoginPageActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
//        Intent intent = new Intent(this, LoginPageActivity.class);
//        intent.putExtra("email", email);
//        startActivity(intent);
//        finish();
    }

    public void onAddCommentAddButtonClick(View v) {
        EditText edit_course_number = findViewById(R.id.course_number);
        EditText edit_rating = findViewById(R.id.rating);
        EditText edit_comment = findViewById(R.id.comment_text);
        TextView tv = findViewById(R.id.comment_statusField);
        String course_number = edit_course_number.getText().toString();
        String rating = edit_rating.getText().toString();
        String comment = edit_comment.getText().toString();
        if(course_number.length() <= 0 || rating.length() <= 0 || comment.length() <= 0){
           message = "comment underspecified";
           tv.setText(message);
           return;
        }
        int temp = -1;
        try {
            temp = Integer.parseInt(rating);
        } catch(NumberFormatException e) {
            message = "rating must be an integer";
            tv.setText(message);
            return;
        }
        if (temp < 0 || temp > 5){
            message = "rating must be between 0 and 5";
            tv.setText(message);
            return;
        }else {
            try {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    try {
                        // assumes that there is a server running on the AVD's host on port 3000
                        // and that it has a /login endpoint
                        String url_string = "http://10.0.2.2:3000/addComment/";
                        url_string += course_number;
                        url_string += "?email=" + email + "&rating=" + rating + "&text=" + comment;

                        URL url = new URL(url_string);

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.connect();

                        int responseCode = conn.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            Scanner in = new Scanner(conn.getInputStream());
                            message = in.nextLine();
//                             tv.setText(message);
                            // add comment success go back to login page acitivity
                            Intent intent = new Intent(this, LoginPageActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
//                            return;
                        } else if(responseCode == 404){
                            message = "course or rating is invalid";
                           tv.setText(message);
                        } else {
                            conn.disconnect();
                            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                        }
                        conn.disconnect();

                    } catch (Exception e) {
                        e.printStackTrace();
//                        message = e.toString();
                    }
                });

                // this waits for up to 2 seconds
                // it's a bit of a hack because it's not truly asynchronous
                // but it should be okay for our purposes (and is a lot easier)
                executor.awaitTermination(2, TimeUnit.SECONDS);

            } catch (Exception e) {
                e.printStackTrace();
//                message = e.toString();
            }
        }
        tv.setText(message);
    }
}
