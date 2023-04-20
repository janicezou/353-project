package com.example.mycoursesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    String[] courseNumbers;
    String[] ratings;
    String[] comments;
    String[] timestamps;

    String[] commentID;
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx, String[] courseNum, String[] ratings, String[] comments, String[] timestamp, String[] commentID){
        this.context = ctx;
        this.courseNumbers = courseNum;
        this.ratings = ratings;
        this.comments = comments;
        this.timestamps = timestamp;
        this.commentID = commentID;
        inflater = (LayoutInflater.from(ctx));
    }

    @Override
    public int getCount() {
        return comments.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.comment_list_card, null);
        TextView courseNum = (TextView) view.findViewById(R.id.comment_course_num);
        TextView timestamp = (TextView) view.findViewById(R.id.comment_timestamp);
        TextView rating = (TextView) view.findViewById(R.id.comment_rating);
        TextView text = (TextView) view.findViewById(R.id.comment_text);
        Button btn_edit = (Button) view.findViewById(R.id.edit_comment);
        Button btn_delete = (Button) view.findViewById(R.id.delete_comment);

        courseNum.setText("Course Number: " + courseNumbers[i]);
        timestamp.setText("Timestamp: " + timestamps[i]);
        rating.setText("Rating: " + ratings[i]);
        text.setText("Comment: " + comments[i]);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute( () -> {
                        try {
                            // assumes that there is a server running on the AVD's host on port 3000
                            // and that it has a /login endpoint
                            String url_string = "http://10.0.2.2:3000/deleteCommentAndroid/";
                            url_string += courseNumbers[i]+"/"+commentID[i];

                            URL url = new URL(url_string);

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();

                            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    // this waits for up to 2 seconds
                    // it's a bit of a hack because it's not truly asynchronous
                    // but it should be okay for our purposes (and is a lot easier)
                    executor.awaitTermination(2, TimeUnit.SECONDS);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, One_comment.class);
                intent.putExtra("comment_id", commentID[i]);
                intent.putExtra("rating", ratings[i]);
                intent.putExtra("comments", comments[i]);
                intent.putExtra("courseNumber", courseNumbers[i]);
                context.startActivity(intent);
            }
        });



        return view;
    }
}
