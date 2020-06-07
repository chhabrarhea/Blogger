package com.example.parse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
private Post clickedPost;
private String d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent i=getIntent();
        int position=i.getIntExtra("pos",-1);
        d=basic.date.get(position);
        clickedPost=basic.example_items.get(position);
        ImageView imageView=(ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(clickedPost.getBitmap());
        TextView tt=(TextView) findViewById(R.id.dd);
        tt.setText("Posted On: "+d);

    }
}
