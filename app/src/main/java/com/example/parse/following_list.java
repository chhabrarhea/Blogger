package com.example.parse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.parse.Adapters.list_Adaptor;
import com.example.parse.model.user_info;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class  following_list extends AppCompatActivity {
    Toolbar t;
ArrayList<user_info> checkedUsers;
 public static list_Adaptor ad;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.share_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

         if(item.getItemId()==R.id.logOut)
        {
            ParseUser.logOut();
            Intent i=new Intent(following_list.this,MainActivity.class);
            startActivity(i);
        }
        else if(item.getItemId()==R.id.prof)
        {
           Intent i=new Intent(following_list.this, my_profile.class);
           startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);
        t=findViewById(R.id.following_toolbar);
        setSupportActionBar(t);
        setTitle("Following");
        checkedUsers=new ArrayList<user_info>();
        ParseQuery<ParseObject> pq=new ParseQuery<ParseObject>("isFollowing");
        pq.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        pq.addAscendingOrder("username");
        try {
           ParseObject u= pq.getFirst();
           List user=u.getList("Following");
           if(user!=null)
           for(int i=0;i<user.size();i++)             {
               user_info checkedUser=new user_info(user.get(i).toString(),false);
               checkedUsers.add(checkedUser);    }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ad= new list_Adaptor(following_list.this,checkedUsers);
        ListView lv=(ListView) findViewById(R.id.listView);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewFeed(checkedUsers.get(position).getUsername());
            }
        });
    }
    public void viewFeed(String username)
    {
        Intent i=new Intent(following_list.this, feed.class);
        i.putExtra("username",username);
        startActivity(i);
    }

    public void onFab(View view)
    {
        Intent i=new Intent(following_list.this,listOfUsers.class);
        startActivity(i);
    }
}
