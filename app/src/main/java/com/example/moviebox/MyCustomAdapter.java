package com.example.moviebox;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MovieViewHolder> {


    static Context context;
    List<String> mName = new ArrayList<>();
    List<Integer> mImg = new ArrayList<>();
    List<String> mIDList = new ArrayList<>();
   // List<Movies[]> movieArray = new ArrayList<>();
    int movieType;


    public MyCustomAdapter(Context context, List<String> mName, List<Integer> mImg, List<String> mIDList,int movieType) {
        this.context = context;
        this.mName = mName;
        this.mImg = mImg;
        this.mIDList = mIDList;
        this.movieType = movieType;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.layout_external,null);
        MovieViewHolder holder = new MovieViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyCustomAdapter.MovieViewHolder holder, final int i) {


       // final Movies movie = moviesList.get(i);
        //String[] eachName = getItem(i);
        holder.txtView.setText(mName.get(i));

        if (movieType == 0)
        {
            holder.imgView.setImageDrawable(context.getResources().getDrawable(mImg.get(i)));
        }
        else
        {
            holder.imgView.setImageDrawable(context.getResources().getDrawable(mImg.get(i + 6)));
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent intent = new Intent(context,terminator.class);
                        intent.putExtra("movieID",mIDList.get(i) );
                        intent.putExtra("movieType",movieType);
                        // For the movie data in terminator activity
                        context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

    // Pre defined methods for recycler view
    public static class MovieViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout parentLayout;
        ImageView imgView;
        TextView txtView;


        public MovieViewHolder(View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imageView);
            txtView = itemView.findViewById(R.id.txtViewMovieName);
            parentLayout = itemView.findViewById(R.id.layoutRelative);
        }

    }
}
