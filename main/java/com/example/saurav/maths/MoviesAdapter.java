package com.example.saurav.maths;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Movie> moviesList;
    private Context mContext;
    private String tag;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, tag, data;
        public LinearLayout parent_layout;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            data = view.findViewById(R.id.data);
            tag = view.findViewById(R.id.tag);
            parent_layout = view.findViewById(R.id.parent_layout);
        }
    }


    public MoviesAdapter(Context context, String tag, List<Movie> moviesList) {

        this.moviesList = moviesList;
        this.mContext = context;
        this.tag = tag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.data.setText(movie.getdata());
        holder.tag.setText(movie.gettag());

//------------- Listeners ---------------------
//        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(mContext,View_Activity.class);
//                i.putExtra("tag",tag);
//                i.putExtra("title",movie.getTitle());
//                i.putExtra("data",movie.getdata());
//                i.putExtra("id",movie.gettag());
//
//                mContext.startActivity(i);
//               // Toast.makeText(mContext,"Title : "+movie.getTitle()+"\nData : "+movie.getdata()+"\nid : "+movie.gettag(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}