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

public class CoursesBaseAdapter extends BaseAdapter {

    Context context;
    String[] courseNumbers, courseNames, courseProfs, courseDepts, courseSchools, courseRatings;
    LayoutInflater inflater;

    public CoursesBaseAdapter(Context ctx, String[] nums, String[] names, String[] profs, String[] depts, String[] schools, String[] ratings){
        this.context = ctx;
        this.courseNumbers = nums;
        this.courseNames = names;
        this.courseProfs = profs;
        this.courseDepts = depts;
        this.courseSchools = schools;
        this.courseRatings = ratings;
        inflater = (LayoutInflater.from(ctx));
    }

    @Override
    public int getCount() {
        return courseNumbers.length;
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
        view = inflater.inflate(R.layout.course_list_card, null);
        TextView numTV = (TextView) view.findViewById(R.id.num);
        TextView nameTV = (TextView) view.findViewById(R.id.name);
        TextView profTV = (TextView) view.findViewById(R.id.prof);
        TextView deptTV = (TextView) view.findViewById(R.id.dept);
        TextView schoolTV = (TextView) view.findViewById(R.id.school);
        TextView ratingTV = (TextView) view.findViewById(R.id.rating);
        Button commentBTN = (Button) view.findViewById(R.id.view_comments);

        numTV.setText(courseNumbers[i]);
        nameTV.setText("Name: " + courseNames[i]);
        profTV.setText("Instructor: " + courseProfs[i]);
        deptTV.setText("Department: " + courseDepts[i]);
        schoolTV.setText("School: " + courseSchools[i]);
        ratingTV.setText("Rating: " + courseRatings[i]);


        commentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, One_comment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
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
