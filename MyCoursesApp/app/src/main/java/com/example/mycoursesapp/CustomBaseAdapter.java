package com.example.mycoursesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    String[] courseNumers;
    String[] ratings;
    String[] comments;
    String[] timestamps;
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx, String[] courseNum, String[] ratings, String[] comments, String[] timestamp){
        this.context = ctx;
        this.courseNumers = courseNum;
        this.ratings = ratings;
        this.comments = comments;
        this.timestamps = timestamp;
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

        courseNum.setText("Course Number: " + courseNumers[i]);
        timestamp.setText("Timestamp: " + timestamps[i]);
        rating.setText("Rating: " + ratings[i]);
        text.setText("Comment: " + comments[i]);
        return view;
    }
}
