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

public class OneComment extends AppCompatActivity {

    protected String courseNumber, courseName, courseProf, courseDept, courseSchool, courseRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_one_course);
        courseNumber = getIntent().getStringExtra("courseNumber");
        courseName = getIntent().getStringExtra("courseName");
        courseProf = getIntent().getStringExtra("courseProf");
        courseDept = getIntent().getStringExtra("courseDept");
        courseSchool = getIntent().getStringExtra("courseSchool");
        courseRating = getIntent().getStringExtra("courseRating");
        TextView nameTV = findViewById(R.id.name);
        TextView numTV = findViewById(R.id.number);
        TextView profTV = findViewById(R.id.instructor);
        TextView deptTV = findViewById(R.id.department);
        TextView schoolTV = findViewById(R.id.school);
        TextView ratingTV = findViewById(R.id.rating);
        numTV.setText(courseNumber);
        nameTV.setText("Name: " + courseName);
        profTV.setText("Instructor: " + courseProf);
        deptTV.setText("Department: " + courseDept);
        schoolTV.setText("School: " + courseSchool);
        ratingTV.setText("Rating: " + courseRating);
    }


    public void onViewCommentButtonClick(View v) {
        // create an intent to start the Main Activity
        Intent i = new Intent(this, LoginPageActivity.class);
        i.putExtra("number", courseNumber);
        startActivity(i);
        // direct to main page
    }
}
