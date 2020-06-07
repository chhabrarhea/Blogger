package com.example.parse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener,View.OnClickListener  {
        TextView username;
        TextView password;
        Button login;
        Button sign;
        int l=0,m=0;
        TextView email;
        ArrayList<String> usernames;
    public void move()
        {
            Intent i=new Intent(MainActivity.this,listOfUsers.class);
            startActivity(i);
        }

    public void userLogin()
    {
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
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
        });
    }
    public void userSignup()
    {
        ParseUser user=new ParseUser();
        String em=email.getText().toString();
        if(username.getText()!=null && password.getText()!=null)
        {
            if(usernames.contains(username.getText()))
            {
                Toast.makeText(this, "Username already taken!", Toast.LENGTH_SHORT).show();
            }
            else
            {
            user.setUsername(username.getText().toString());
            user.setPassword(password.getText().toString());
            user.setEmail(em);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null)
                        Toast.makeText(MainActivity.this, "Signed Up!", Toast.LENGTH_LONG).show();
                    else{
                        Log.i("heyy","loserrrr");
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "There  was an error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            email.animate().alpha(0);
            l=0;
            m=0;
            sign.setText("SignUp");
            login.setText("Login");
            email.setText("");
            password.setText("");
            username.setText("");
        }}
        else{
            Toast.makeText(MainActivity.this, "Invalid Username/Password/Email", Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Instagram");
        if(ParseUser.getCurrentUser()==null){
         username=(TextView) findViewById(R.id.name);
         password=(TextView) findViewById(R.id.pass);
         email=(TextView) findViewById(R.id.email);


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



            login=(Button) findViewById(R.id.login);
        sign=(Button) findViewById(R.id.sign);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(l==0){
                    userLogin();

                    }
                else {
                    userSignup();
                }

            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m==0)
                {  login.setText("SignUp");
              sign.setText("Login");
              l=1;
              email.animate().alpha(1);
                m=1;}
                else
                {
                  email.animate().alpha(0);
                  m=0;
                  l=0;
                    sign.setText("SignUp");
                    login.setText("Login");
                    email.setText("");
                    password.setText("");
                    username.setText("");

                }

            }
        });
        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN ){
                        if(l==0)
                        userLogin();
                        else if(m==1)
                            userSignup();
                }
                return false;
            }
        });}
else{
    finish();
    move();}

        ParseAnalytics.trackAppOpenedInBackground(getIntent());


    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.relative || v.getId()==R.id.logo)
        {
            InputMethodManager in=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
}
