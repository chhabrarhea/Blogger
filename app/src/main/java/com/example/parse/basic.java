package com.example.parse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class basic extends AppCompatActivity implements Adapter.OnItemClickListener{
    private RecyclerView recyclerView;
    String username;
    private Adapter exampleAdapter;
    public static ArrayList<Post> example_items;
    public static ArrayList<String> date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        example_items = new ArrayList<>();
         date = new ArrayList<>();
        Intent i = getIntent();
        username = i.getStringExtra("username");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");

        query.whereEqualTo("username", username);
        query.orderByDescending("createdAt");
        try {
            List<ParseObject> objects = query.find();
            for (ParseObject object : objects) {
                ParseFile file = (ParseFile) object.get("image");
                byte[] data = file.getData();
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Post newItem = new Post(bitmap);
                    example_items.add(newItem);
                }
                Date dateCreated=object.getDate("createdAt");
                String d=dateCreated.toString();
                date.add(d);


            }
            exampleAdapter=new Adapter(basic.this,example_items);
            recyclerView.setAdapter(exampleAdapter);
            exampleAdapter.setOnItemClickListener(basic.this);



        } catch (ParseException e) {
            e.printStackTrace();

        }
    }
    public void onItemClick(int position) {
        Intent i=new Intent(basic.this,Main2Activity.class);
        i.putExtra("pos",position);
        i.putExtra("username",username);
        startActivity(i);


    }}