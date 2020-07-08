package com.example.parse.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.example.parse.R;
import com.example.parse.model.user_info;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class list_Adaptor extends BaseAdapter{
    private Context context;
    private ArrayList<user_info> users;
    private CheckedTextView name;
    private CircleImageView image;

    public list_Adaptor(Context c,ArrayList<user_info> example)
    {
        context=c;
        users=example;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=LayoutInflater.from(context).inflate(R.layout.list,parent,false);
            name=convertView.findViewById(R.id.username_list);
         name.setText(users.get(position).getUsername()+"         ");
         image=convertView.findViewById(R.id.profile_image);
         if(users.get(position).getDp()!=null)
         image.setImageBitmap(users.get(position).getDp());
         else
             image.setImageResource(R.drawable.default_dp);
         if(users.get(position).getChecked()){
             name.setChecked(true);
             name.setCheckMarkTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));}
          else
         {
             name.setChecked(false);
             name.setCheckMarkTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.checkBox)));
         }

        return convertView;

    }

}
