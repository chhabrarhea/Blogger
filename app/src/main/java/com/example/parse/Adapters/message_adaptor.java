package com.example.parse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.parse.R;
import com.example.parse.model.message_demo;
import com.parse.ParseUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class message_adaptor extends BaseAdapter {
    private Context context;
    private ArrayList<message_demo> m;
    private TextView line;
    private CircleImageView message_dp;
    public message_adaptor(ArrayList<message_demo> ex,Context c)
    {
        m=ex;
        context=c;
    }
    @Override
    public int getCount() {
        return m.size();
    }

    @Override
    public Object getItem(int position) {
        return m.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.message_layout,parent,false);
        line=convertView.findViewById(R.id.message);
        message_dp=convertView.findViewById(R.id.message_dp);
        if(m.get(position).getSender()== ParseUser.getCurrentUser().getUsername())
        {
            line.setText("ME: "+m.get(position).getContent());
        }
        else
        {
            line.setText(m.get(position).getSender()+": "+m.get(position).getContent());
        }
        if(m.get(position).getBitmap()!=null)
        {
            message_dp.setImageBitmap(m.get(position).getBitmap());
        }

        return convertView;
    }

}
