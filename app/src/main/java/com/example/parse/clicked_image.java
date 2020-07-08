package com.example.parse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parse.model.Post;
import com.parse.ParseUser;

public class clicked_image extends AppCompatActivity {
private Post clickedPost;
private String d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_image);
        Intent i=getIntent();
        int position=i.getIntExtra("pos",-1);
        String username=i.getStringExtra("username");
        Toolbar t=findViewById(R.id.clickedImageToolbar);
        setSupportActionBar(t);
        setTitle(username);
        if(username.equals(ParseUser.getCurrentUser().getUsername())){
            d=my_profile.caption.get(position);
            clickedPost=my_profile.example_items.get(position);
        }
        else{
            d=feed.caption.get(position);
            clickedPost= feed.example_items.get(position);}
        TextView cap=findViewById(R.id.dd);
        cap.setText(d);
        ImageView imageView=(ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(clickedPost.getBitmap());

    }
}
