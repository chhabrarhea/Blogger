package com.example.parse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.parse.Adapters.list_Adaptor;
import com.example.parse.model.user_info;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class listOfUsers extends AppCompatActivity {
ListView lv;
public static ArrayList<user_info> al;
public  ArrayList<String> names;
list_Adaptor ad;
CheckedTextView b;
Toolbar t;
List<String> followed;
List<String> removed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_users);
        t=findViewById(R.id.list_toolbar);
        setSupportActionBar(t);
        setTitle("All Users");



        al=new ArrayList<user_info>();
        ad=new list_Adaptor(listOfUsers.this,al);
        lv=(ListView) findViewById(R.id.list);
        followed=new ArrayList<>();
        removed=new ArrayList<>();

        final String currentUser=ParseUser.getCurrentUser().getUsername();
         lv.setAdapter(ad);
         names=new ArrayList<>();


        //LIST OF ALL OTHER USERS
        ParseQuery<ParseUser> pq=ParseUser.getQuery();
        pq.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        pq.addAscendingOrder("username");
        try {
            List<ParseUser> users= pq.find();
            for(ParseUser user:users)
            {

                names.add(user.getUsername());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }



         //check existing followers
        followed= ParseUser.getCurrentUser().getList("followedBy");
        for(String name:names){
            if(followed!=null && followed.contains(name))
            {
                user_info user=new user_info(name,true);
                al.add(user);
            }
            else
            {
                user_info user=new user_info(name,false);
                al.add(user);
            }


        }
         lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 final CheckedTextView checkedTextView=view.findViewById(R.id.username_list);
                 if(checkedTextView.isChecked())
                 {
                     checkedTextView.setChecked(false);
                     checkedTextView.setCheckMarkTintList(ColorStateList.valueOf(getResources().getColor(R.color.checkBox)));
                     followed.remove(names.get(position));
                     removed.add(names.get(position));
                     Toast.makeText(listOfUsers.this, "Removed", Toast.LENGTH_SHORT).show();}
                  else
                 {
                     checkedTextView.setChecked(true);
                     Toast.makeText(listOfUsers.this, "Added!", Toast.LENGTH_SHORT).show();
                     checkedTextView.setCheckMarkTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                     followed.add(names.get(position));
                     removed.remove(names.get(position));}
                 }
         });
    }

    @Override
    public void onBackPressed() {
        ParseUser.getCurrentUser().put("followedBy",followed);
        for(String name:followed)
        {
            ParseQuery<ParseObject> pq=new ParseQuery<ParseObject>("isFollowing");
            pq.whereEqualTo("username",name);
            try {
                ParseObject obj=pq.getFirst();
                obj.add("Following",ParseUser.getCurrentUser().getUsername());
                obj.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for(String n:removed)
        {
            ParseQuery<ParseObject> pq=new ParseQuery<ParseObject>("isFollowing");
            pq.whereEqualTo("username",n);
            try{
                ParseObject obj=pq.getFirst();
                List<String> myFollowers=obj.getList("Following");
                myFollowers.remove(ParseUser.getCurrentUser().getUsername());
                obj.put("Following",myFollowers);
                obj.save();
            }catch (Exception e){e.printStackTrace();}
        }
        try {
            ParseUser.getCurrentUser().save();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }
}
