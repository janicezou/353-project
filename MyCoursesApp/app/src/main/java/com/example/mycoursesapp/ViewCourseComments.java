package com.example.mycoursesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ViewCourseComments extends AppCompatActivity {
    String courseNumber,email;
    ListView listView;
    //    @Override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseNumber = getIntent().getStringExtra("number");
        email = getIntent().getStringExtra("email");

        setContentView(R.layout.activity_check_all_comment);
        listView = (ListView) findViewById(R.id.listComments);

        TextView tv = findViewById(R.id.allcomments_statusField);

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {

                    // assumes that there is a server running on the AVD's host on port 3000
                    // and that it has a /login endpoint
                    String url_string = "http://10.0.2.2:3000/viewCourseComments/" + courseNumber;
                    URL url = new URL(url_string);


                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(conn.getInputStream());
                    String line = in.nextLine();
                    System.out.println(line);
                    JSONArray data = new JSONArray(line);
                    /**
                     * handle the case when JSONArray is empty
                     */
                    if (data.length() == 0) {
                        tv.setText("No comments");
                        System.out.println("No comments");
                    } else {
                        String[] commentID = new String[data.length()];
                        String[] courseNumbers = new String[data.length()];
                        String[] ratings = new String[data.length()];
                        String[] comments = new String[data.length()];
                        String[] timestampes = new String[data.length()];
                        for (int i = 0; i < data.length(); i++) {
                            System.out.println("Start");
                            JSONObject datusobj = data.getJSONObject(i);
                            String _id = (String) datusobj.get("id");
                            String cn = (String) datusobj.get("courseNum");
                            String ts = (String) datusobj.get("timestamp");
                            int r_int = (int) datusobj.get("rating");
                            String r = Integer.toString(r_int);
                            String t = (String) datusobj.get("comment");
                            commentID[i] = _id;
                            courseNumbers[i] = cn;
                            ratings[i] = r;
                            comments[i] = t;
                            timestampes[i] = ts;
                        }
                        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), courseNumbers, ratings, comments, timestampes, commentID, email);
                        listView.setAdapter(customBaseAdapter);
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
    }

    public void onViewCommentBackButtonClick(View v) {
        // return to course page
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }
}
