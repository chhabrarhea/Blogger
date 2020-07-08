package com.example.parse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import com.example.parse.Adapters.message_adaptor;
import com.example.parse.model.message_demo;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class message extends AppCompatActivity implements View.OnClickListener {
ArrayList<message_demo> messages;
message_adaptor ad;
ListView listView;
String user1;
EditText con;
String user2;
Bitmap b1;
Bitmap b2;
Toolbar t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.myActionBar));
        Intent intent=getIntent();
        t=findViewById(R.id.messageToolbar);
        setSupportActionBar(t);
        user1= ParseUser.getCurrentUser().getUsername();
        user2=intent.getStringExtra("reciever");
        setTitle(user2);
        messages=new ArrayList<>();
        ParseQuery<ParseObject> p1=new ParseQuery<ParseObject>("message");
        ad=new message_adaptor(messages,message.this);
        listView=findViewById(R.id.messageList);
        listView.setAdapter(ad);
        con=findViewById(R.id.newMessage);
        con.setOnClickListener(this);

        //get Dp
        ParseFile file1=(ParseFile)ParseUser.getCurrentUser().get("Dp");
        byte [] data;
        try {
            if(file1!=null && file1.getData()!=null){
                data=file1.getData();
                b1= BitmapFactory.decodeByteArray(data,0,data.length);}
            else
                b1=null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseQuery<ParseUser> q=ParseUser.getQuery();
        q.whereEqualTo("username",user2);
        try {
            ParseUser user= (ParseUser) q.getFirst();
            ParseFile file2=(ParseFile) user.get("Dp");
            if(file2!=null && file2.getData()!=null)
            {
                data = file2.getData();
                    b2 = BitmapFactory.decodeByteArray(data, 0, data.length);
                }
            else
                b2=null;
        } catch (ParseException e) {
            e.printStackTrace();



        }


        p1.whereEqualTo("sender",user1);
        p1.whereEqualTo("reciever",user2);
        ParseQuery<ParseObject> p2=new ParseQuery<ParseObject>("message");
        p2.whereEqualTo("sender",user2);
        p2.whereEqualTo("reciever",user1);
        List<ParseQuery<ParseObject>> queries=new ArrayList<ParseQuery<ParseObject>>();
        queries.add(p1);
        queries.add(p2);
        ParseQuery<ParseObject> p3=ParseQuery.or(queries);
        p3.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0){
             for(ParseObject obj:objects)
             {
                 if(obj.get("sender").equals(user1))
                 {
                     message_demo a=new message_demo(obj.get("message").toString(),user1,b1);
                     messages.add(a);
                     ad.notifyDataSetChanged();
                 }
                 else{
                     message_demo a=new message_demo(obj.get("message").toString(),user2,b2);
                     messages.add(a);
                     ad.notifyDataSetChanged();

                 }

             }}
                else if(e!=null){ Log.i("error","generating messages");
                    e.printStackTrace();}


            }
        });

    }
    public void sent(View view)
    {

        message_demo a=new message_demo(con.getText().toString(),user1,b1);
        ParseObject obj=new ParseObject("message");
        obj.put("message",con.getText().toString());
        obj.put("sender",user1);
        obj.put("reciever",user2);
        obj.saveInBackground();
        messages.add(a);
        ad.notifyDataSetChanged();


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.newMessage);
        v.bringToFront();

    }
}