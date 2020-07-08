package com.example.parse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.parse.Adapters.feed_Adaptor;
import com.example.parse.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class feed extends AppCompatActivity implements feed_Adaptor.OnItemClickListener {
    private RecyclerView recyclerView;
    String username;
    private feed_Adaptor exampleAdapter;
    public static ArrayList<Post> example_items;
    public static ArrayList<String> caption;
    Toolbar myToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        myToolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
         
         
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        example_items = new ArrayList<>();
        caption = new ArrayList<>();
        Intent i = getIntent();
        username = i.getStringExtra("username");
        CircleImageView civ=findViewById(R.id.dp);
        ParseQuery pq=ParseUser.getQuery();
        pq.whereEqualTo("username",username);
       try{
           ParseUser user=(ParseUser)pq.getFirst();
          if(user.get("Dp")!=null)
          {
              ParseFile file = (ParseFile) user.get("Dp");
              byte[] data = file.getData();
              if (data != null) {
                  Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                  civ.setImageBitmap(bitmap);
          }}
       }catch (Exception e){
           e.printStackTrace();
       }

        setTitle(username+"'s feed");

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
            if(objects.size()==0)
            {
                Toast.makeText(this, "User has no posts! :(", Toast.LENGTH_LONG).show();
            }
            myToolbar.setSubtitle(objects.size()+" posts");
            exampleAdapter=new feed_Adaptor(feed.this,example_items);
            recyclerView.setAdapter(exampleAdapter);
            exampleAdapter.setOnItemClickListener(feed.this);





        } catch (ParseException e) {
            e.printStackTrace();

        }
    }



    public void onItemClick(int position) {
        Intent i=new Intent(feed.this, clicked_image.class);
        i.putExtra("pos",position);
        i.putExtra("username",username);
        startActivity(i);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater inflater=getMenuInflater();
         inflater.inflate(R.menu.feed_toolbar,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.activity_send:
               Intent intent=new Intent(feed.this,message.class);
               intent.putExtra("reciever",username);
               startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }



     }

