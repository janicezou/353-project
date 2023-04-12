package com.example.mycoursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Search extends AppCompatActivity {
    EditText name,number,department,professor;
    boolean alphabetically=false;
    Double averageRating = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);


        //get search query
        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);
        department = (EditText) findViewById(R.id.department);
        professor = (EditText) findViewById(R.id.professor);
//        AverageRating ar = new AverageRating();
//        averageRating = ar.getAverageRating(number.getText().toString());

    }

    private void displayResponse(List<JSONObject> courses) {
        String updateTxt="";
        averageRating = 0.0;
        AverageRating ar = new AverageRating();

        if(courses == null || courses.size() ==0){
            updateTxt="no results found";
        } else{
            for (JSONObject course : courses){
                String _name = null;
                String _number = null;
                Double _rating = 0.0;
                try {
                    _name = (String) course.get("name");
                    _number = (String) course.get("number");
                    _rating = ar.getAverageRating(_number);
//                    averageRating += _rating;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                updateTxt += "Course Name: "+_name+"; Course Number: "+_number+"; Rating: "+_rating+"\n";
            }
        }
//        averageRating = averageRating/courses.size();

//        updateTxt = averageRating + "\n" + updateTxt;

        TextView results = findViewById(R.id.results);
        results.setText(String.valueOf(updateTxt));
        // TextView averageRatingView = findViewById(R.id.average_rating);
        // averageRatingView.setText(String.valueOf(averageRating));

    }


    public void onSortButtonClick(View v){
        alphabetically = !alphabetically;
    }



    protected ArrayList<JSONObject> getJSON(){
        String nameS = ((EditText) findViewById(R.id.name)).getText().toString();
        String numS = ((EditText) findViewById(R.id.number)).getText().toString();
        String deptS = ((EditText) findViewById(R.id.department)).getText().toString();
        String profS = ((EditText) findViewById(R.id.professor)).getText().toString();

        ArrayList<JSONObject> courses = new ArrayList<JSONObject>();



        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {

                    // assumes that there is a server running on the AVD's host on port 3000
                    // and that it has a /login endpoint
                    String urlStr = "http://10.0.2.2:3000/searching?name="+nameS+"&number="+numS+"&department="+deptS+"&professor="+profS;
                    if(alphabetically){
                        urlStr += "&sort=name";
                    }
                    URL url = new URL(urlStr);


                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(conn.getInputStream());
                    String line = in.nextLine();
                    System.out.println(line);
                    JSONArray data = new JSONArray(line);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataObj = data.getJSONObject(i);
                        courses.add(dataObj);
                    }

                    if (conn != null) {
                        conn.disconnect();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }


    public void onSearchButtonClick(View v){
        // get json body object
        // JSONObject jsonBody = updateSearch();
        // pass json body object
        // get json response

        // parse json response into string
       ArrayList<JSONObject> courses = getJSON();
        // parse json response into list
        // update visuals
        displayResponse(courses);

        // give list to new page
        // launch new page
        // display
    }

    public void onBackButtonClick(View v){
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }
}
