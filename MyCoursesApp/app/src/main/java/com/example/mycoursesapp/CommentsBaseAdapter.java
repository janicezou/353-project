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

public class CommentsBaseAdapter extends BaseAdapter {

    Context context;
    String[] courseNumbers;
    String[] ratings;
    String[] comments;
    String[] timestamps;

    String[] commentID;

    LayoutInflater inflater;

    public CommentsBaseAdapter(Context ctx, String[] courseNum, String[] ratings, String[] comments, String[] timestamp, String[] commentID){
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
        view = inflater.inflate(R.layout.course_comment_list_card, null);
        TextView courseNum = (TextView) view.findViewById(R.id.comment_course_num);
        TextView timestamp = (TextView) view.findViewById(R.id.comment_timestamp);
        TextView rating = (TextView) view.findViewById(R.id.comment_rating);
        TextView text = (TextView) view.findViewById(R.id.comment_text);

        courseNum.setText("Course Number: " + courseNumbers[i]);
        timestamp.setText("Timestamp: " + timestamps[i]);
        rating.setText("Rating: " + ratings[i]);
        text.setText("Comment: " + comments[i]);

             return view;
    }
}
