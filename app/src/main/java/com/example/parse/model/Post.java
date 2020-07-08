package com.example.parse.model;

import android.graphics.Bitmap;

public class Post {
    private Bitmap b;


   public Post(Bitmap u)
    {
        this.b=u;

    }

    public Bitmap getBitmap(){
        return  b;}


}
