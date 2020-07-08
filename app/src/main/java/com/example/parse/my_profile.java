package com.example.parse;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.parse.Adapters.feed_Adaptor;
import com.example.parse.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class my_profile extends AppCompatActivity implements feed_Adaptor.OnItemClickListener {
    private RecyclerView recyclerView;
    String username;
    private feed_Adaptor exampleAdapter;
    public static ArrayList<Post> example_items;
    public static ArrayList<String> caption;
    Toolbar t;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = new MenuInflater(my_profile.this);
        inflater.inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int action;
        switch (item.getItemId()) {
            case R.id.cap:
                Intent i = new Intent(my_profile.this, createPost.class);
                 action=2;
                Log.i("action","cap");
                 i.putExtra("action",action);
                 startActivity(i);
                 break;
            case R.id.up:
                Log.i("action","up");
                Intent in = new Intent(my_profile.this, createPost.class);
                action=1;
                in.putExtra("action",action);
                startActivity(in);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile );
        t=findViewById(R.id.myProfile_toolbar);
        setSupportActionBar(t);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        example_items = new ArrayList<>();
        caption = new ArrayList<>();
        Intent i = getIntent();
        username = ParseUser.getCurrentUser().getUsername();

        CircleImageView civ=findViewById(R.id.dp);
        try{
       {
              ParseFile file = (ParseFile)ParseUser.getCurrentUser().get("Dp");
              if(file!=null){
              byte[] data = file.getData();
              if (data != null) {
                  Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                  civ.setImageBitmap(bitmap);}
          }}
       }catch (Exception e){
           e.printStackTrace();
       }

        setTitle("My profile");

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
                String c;
                if(object.get("caption")!=null){
                 c=object.get("caption").toString();}
                else
                    c="";
                caption.add(c);


            }

            getSupportActionBar().setSubtitle(objects.size()+" posts");
            exampleAdapter=new feed_Adaptor(my_profile.this,example_items);
            recyclerView.setAdapter(exampleAdapter);
            exampleAdapter.setOnItemClickListener(my_profile.this);



        } catch (ParseException e) {
            e.printStackTrace();

        }
    }
    public void onItemClick(int position) {
        Intent i=new Intent(my_profile.this, clicked_image.class);
        i.putExtra("pos",position);
        i.putExtra("username",username);
        startActivity(i);
    }



    public void changeDp(View view)
{

    AlertDialog.Builder ald=new AlertDialog.Builder(this);
    ald.setTitle("Change Profile Photo");
    ald.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
         Intent i=new Intent(my_profile.this,createPost.class);
         i.putExtra("action",3);
         startActivity(i);
        }
    }).setNeutralButton("Capture", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent i=new Intent(my_profile.this,createPost.class);
            i.putExtra("action",4);
            startActivity(i);
        }

    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }).show();
}}

