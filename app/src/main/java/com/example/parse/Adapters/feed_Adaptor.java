package com.example.parse.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.parse.model.Post;
import com.example.parse.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class feed_Adaptor extends RecyclerView.Adapter<feed_Adaptor.ExampleViewHolder> {
    private Context context;
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    private ArrayList<Post> posts;
    public feed_Adaptor(Context c, ArrayList<Post> exampleList) {
        context = c;
        posts = exampleList;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.feed, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
      Post   currentPost=posts.get(position);
      Bitmap b=currentPost.getBitmap();
      holder.mImageView.setImageBitmap(b);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }



    public class ExampleViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

}
