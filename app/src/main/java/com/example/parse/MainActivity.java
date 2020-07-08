package com.example.parse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,loginFragment.ContentListener,signupFragment.ContentListener  {
    ArrayList<String> usernames;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Blogger");
        relativeLayout=findViewById(R.id.relative);
        relativeLayout.setOnClickListener(this);
    if(ParseUser.getCurrentUser()!=null)
    {
       Intent intent=new Intent(MainActivity.this,following_list.class);
       finish();
       startActivity(intent);
    }
    TabLayout tab=findViewById(R.id.tab);
    ViewPager viewPager=findViewById(R.id.viewPager);
        viewPager.setAdapter(new Demo(getSupportFragmentManager()));
        tab.setupWithViewPager(viewPager);

        usernames=new ArrayList<>();
        ParseQuery<ParseUser> pq =new ParseQuery<ParseUser>("user");
        pq.whereNotEqualTo("username",null);
        try {
            List<ParseUser> users= pq.find();
            for(ParseUser user:users )
                usernames.add(user.getUsername());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @Override
    public void input(String name, String password) {
        if(name!=null && !name.equals("") && password!=null && !password.equals("")){
        ParseUser.logInInBackground(name, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e==null && user!=null){
                    Log.i("log in","success");
                    Toast.makeText(MainActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                    move();
                }
                else
                {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Username/Password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });}
        else
            Toast.makeText(MainActivity.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void input(String name, String password, String email) {

        if(usernames.contains(name))
        {
            Toast.makeText(this, "Username taken", Toast.LENGTH_SHORT).show();
        }

        if(name!=null && !name.equals("") && password!=null && !password.equals("") && email!=null && !email.equals(""))
        {
            ParseUser user=new ParseUser();
            ParseObject obj=new ParseObject("isFollowing");
            obj.put("username",name);
            obj.saveInBackground();
            user.setUsername(name);
            user.setPassword(password);
            user.setEmail(email);

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null)
                    {
                        Toast.makeText(MainActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                        move();
                    }
                    else
                    {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error Occured.Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    class Demo extends FragmentStatePagerAdapter {


        String [] data={"Login" ,"Signup"};

        public Demo(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            if(position==0)
                fragment=new loginFragment();
            else if(position==1)
                fragment=new signupFragment();
            return fragment;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return  data[position];
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.relative)
        {
            InputMethodManager in=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }

    }



    public void move()
    {
        finish();
        Intent i=new Intent(MainActivity.this, following_list.class);
        startActivity(i);
    }
}
