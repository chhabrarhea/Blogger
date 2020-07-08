package com.example.parse.model;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import static com.parse.Parse.getApplicationContext;

public class user_info {
    private Bitmap dp;
    private String username;
    private Boolean checked;
   public user_info(Bitmap b, String name)
    {
        dp=b;
        username=name;
    }
    public user_info(String name,Boolean check)
    {
        checked=check;
        username=name;
    }
    public Boolean getChecked(){ return checked;}

    public Bitmap getDp() {

        ParseQuery<ParseUser> q=ParseUser.getQuery();
        q.whereEqualTo("username",username);
        try {
          ParseUser user= (ParseUser) q.getFirst();
          if(user.get("Dp")!=null)
          {
              ParseFile file=(ParseFile)user.get("Dp");
              byte[] data = file.getData();
              if (data != null) {
                  dp = BitmapFactory.decodeByteArray(data, 0, data.length);
          }}
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        return dp;
    }

    public String getUsername() {
        return username;
    }
}
