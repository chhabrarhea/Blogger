package com.example.parse.model;

import android.graphics.Bitmap;

public class message_demo {
    private String content;
    private String sender;
    private Bitmap bitmap;

   public message_demo(String c,String b,Bitmap bit)
    {
        content=c;
        sender=b;
        bitmap=bit;
    }
    public message_demo(String c,String b)
    {
        content=c;
        sender=b;
        bitmap=null;
    }

    public String getSender() {
        return sender;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getContent() {
        return content;
    }
}
