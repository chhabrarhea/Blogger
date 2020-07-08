package com.example.parse;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class signupFragment extends Fragment implements View.OnKeyListener {
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    public interface ContentListener{
        public void input(String name,String password,String email);
    }
    private EditText username;
    private EditText password;
    private EditText email;
    private ContentListener listener;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username=view.findViewById(R.id.signUsername);
        password=view.findViewById(R.id.signPassword);
        email=view.findViewById(R.id.signEmail);
        Button signup=view.findViewById(R.id.signButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.input(username.getText().toString(),password.getText().toString(),email.getText().toString());
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN)
                {
                    listener.input(username.getText().toString(),password.getText().toString(),email.getText().toString());
                }
                return false;
            }
        });
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof loginFragment.ContentListener) {
            listener = (signupFragment.ContentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentSignupListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
        username.setText("");
         email.setText("");
        password.setText("");
    }

}