package com.example.parse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

import static java.util.Objects.requireNonNull;

public class createPost extends AppCompatActivity implements View.OnClickListener{
TextView caption;
ImageView picture;
    Bitmap bitmap;
    int action;
    FloatingActionButton b;
    public void getPhoto()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
    public void capturePhoto()
    {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 1888);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null)
        {
            Intent intent=new Intent(createPost.this,my_profile.class);
            startActivity(intent);
        }

        if ((requestCode==1 || requestCode==1888) && resultCode == RESULT_OK && data != null) {

            try {
                if(requestCode==1){

                    bitmap  = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());}
                else {
                    bitmap = (Bitmap) requireNonNull(data.getExtras()).get("data");
                }
               picture.setImageBitmap(bitmap);


                }

             catch (Exception e) {
                e.printStackTrace();
            }

    }}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Intent intent=getIntent();
        action=  intent.getIntExtra("action",-1);
        Log.i("action",action+"");
        picture=findViewById(R.id.picture);
        caption=findViewById(R.id.caption);
        if(action==3 || action==4)
        {
            b=findViewById(R.id.save);
            b.animate().translationX(-400).setDuration(0);
            caption.setAlpha(0);
            caption.setEnabled(false);

        }
        if(action==1 || action==3)
            if (ContextCompat.checkSelfPermission(createPost.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                getPhoto();
            else Dexter.withContext(this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            getPhoto();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            // check for permanent denial of permission
                            if (response.isPermanentlyDenied()) {
                                AlertDialog.Builder ad = new AlertDialog.Builder(createPost.this);
                                ad.setTitle("Permission Denied")
                                        .setMessage("Enable External Storage Access in settings")
                                        .setIcon(R.drawable.ic_launcher_background)
                                        .setNegativeButton("Cancel", null)
                                        .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                        Uri.parse("package:" + getPackageName()));
                                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }).show();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

            else if(action==2 || action==4)
        {
            if(ContextCompat.checkSelfPermission( createPost.this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)
            {
                capturePhoto();
            }
            else
                Dexter.withContext(this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                capturePhoto();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if(response.isPermanentlyDenied()){
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.parse("package:" + getPackageName()));
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    // navigate user to app settings
                                }

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
        }




    }
    public void onSave(View view)
    {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            ParseFile file = new ParseFile("image.png", byteArray);
            if(action==1 || action==2){
            ParseObject object = new ParseObject("Image");
            object.put("image", file);
            object.put("caption",caption.getText().toString());
            object.put("username", ParseUser.getCurrentUser().getUsername());
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText( createPost.this, "Image has been shared!", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(createPost.this,my_profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {
                        Toast.makeText(createPost.this, "There has been an issue uploading the image :(", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else  if(action==3 || action==4){
                ParseUser user=ParseUser.getCurrentUser();
                user.put("Dp",file);

                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null)
                                Toast.makeText(createPost.this, "Photo updated!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(createPost.this, "Photo could not be updated :(", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        Intent intent=new Intent(createPost.this,my_profile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.relate)
        {
            InputMethodManager in=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }


}






