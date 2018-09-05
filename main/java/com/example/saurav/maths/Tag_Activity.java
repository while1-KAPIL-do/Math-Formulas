package com.example.saurav.maths;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Tag_Activity extends AppCompatActivity {
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    private String s1[],s2[],s3[];
    private TextView tv_taghead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        tv_taghead = findViewById(R.id.textView_tag_head);
        recyclerView = findViewById(R.id.recycler_view);


        Bundle b =this.getIntent().getExtras();
        s1 = b.getStringArray("string1");
        s2 = b.getStringArray("string2");
        s3 = b.getStringArray("string3");
        String tag = b.getString("myTag");

        tv_taghead.setText(tag);


        mAdapter = new MoviesAdapter(Tag_Activity.this,tag,movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);




        prepareMovieData(s1,s2,s3);
    }

    private void prepareMovieData(String st1[],String st2[],String st3[]) {

        int i=0;
        while(i<st1.length) {

            movieList.add(new Movie(st1[i], st2[i], st3[i]));
            i++;
        }
        mAdapter.notifyDataSetChanged();
    }
}