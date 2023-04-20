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

public class One_comment extends AppCompatActivity {

    protected String courseNum;
    protected String rating;
    protected String comment_id;
    protected String comment;

    protected String message;
    protected String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_one_comment);
        courseNum = getIntent().getStringExtra("courseNumber");
        rating = getIntent().getStringExtra("rating");
        comment_id = getIntent().getStringExtra("comment_id");
        comment = getIntent().getStringExtra("comments");
        email = getIntent().getStringExtra("email");
        EditText rating_box = findViewById(R.id.one_rating);
        EditText editComment = findViewById(R.id.one_comment_text);
        rating_box.setText(rating);
        editComment.setText(comment);
    }


    public void onEditOneCommentButtonClick(View v) {

        TextView tv = findViewById(R.id.edit_comment_status);
        TextView course_number_text = findViewById(R.id.one_course_number);
        course_number_text.setText(courseNum);
        EditText rating_box = findViewById(R.id.one_rating);
        EditText editComment = findViewById(R.id.one_comment_text);

        String new_rating = rating_box.getText().toString();
        System.out.println("***********NEW RATING:" + new_rating);
        String new_comment = editComment.getText().toString();

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    // assumes that there is a server running on the AVD's host on port 3000
                    // and that it has a /login endpoint

                    URL url = new URL("http://10.0.2.2:3000/editCommentAndroid/"+courseNum+"/"+comment_id+"?"+"text="+new_comment+"&rating="+new_rating);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(conn.getInputStream());
                    String response = in.nextLine();
                    if (conn.getResponseCode() == 200) {
                        message = "Edit success";
                        Intent i = new Intent(this, ViewComments.class);
                        i.putExtra("email", email);
                        startActivity(i);
                    } else {
                        // login failed, show error message
                        message = response;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
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
