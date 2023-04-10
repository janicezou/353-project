package com.example.mycoursesapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ViewComments extends AppCompatActivity {
    String email;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = getIntent().getStringExtra("email");
        setContentView(R.layout.activity_check_all_comment);
        listView = (ListView) findViewById(R.id.listComments);

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    Scanner in = null;
                    // assumes that there is a server running on the AVD's host on port 3000
                    // and that it has a /login endpoint
                    String url_string = "http://10.0.2.2:3000/seeAllMyComments/" + email;
                    URL url = new URL(url_string);

                    String[] comments;
                    String[] ratings;
                    String[] timestampes;
                    String[] courseNumbers;

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    in = new Scanner(url.openStream());

                    while (in.hasNext()) {
                        String line = in.nextLine();

                        JSONParser parser = new JSONParser();

                        // then, parse the data and create a JSON Array for it
                        Object dataobj = parser.parse(line);

                        JSONArray data = (JSONArray) dataobj;
                        /**
                         * handle the case when JSONArray is empty
                         */
                        if (data.size() == 0) {
                            courseNumbers = new String[1];
                            courseNumbers[0] = "Temp course";
                            ratings = new String[1];
                            ratings[0] = "Temp ratings";
                            comments = new String[1];
                            comments[0] = "Temp comment";
                            timestampes = new String[1];
                            timestampes[0] = "Temp timestampes";
                            CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), courseNumbers, ratings, comments, timestampes);
                            listView.setAdapter(customBaseAdapter);
                        } else {
                            courseNumbers = new String[data.size()];
                            ratings = new String[data.size()];
                            comments = new String[data.size()];
                            timestampes = new String[data.size()];
                            int index = 0;
                            for (Object datus : data) {
                                JSONObject datusobj = (JSONObject) datus;
                                String cn = (String) datusobj.get("courseNum");
                                String ts = (String) datusobj.get("timestamp");
                                String r = (String) datusobj.get("rating");
                                String t = (String) datusobj.get("comment");
                                courseNumbers[index] = cn;
                                ratings[index] = r;
                                comments[index] = t;
                                timestampes[index] = ts;
                                index++;
                            }
                            CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), courseNumbers, ratings, comments, timestampes);
                            listView.setAdapter(customBaseAdapter);
                        }
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
