package com.example.parse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class listOfUsers extends AppCompatActivity {
ListView lv;
ArrayList<String> al;
ArrayAdapter<String> ad;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.share_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
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


            Bitmap bitmap;
        if ((requestCode==1 || requestCode==1888) && resultCode == RESULT_OK && data != null) {

            try {
                if(requestCode==1){

                  bitmap  = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());}
                else
                      bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] byteArray = stream.toByteArray();

                ParseFile file = new ParseFile("image.png", byteArray);

                ParseObject object = new ParseObject("Image");

                object.put("image", file);


                object.put("username", ParseUser.getCurrentUser().getUsername());

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(listOfUsers.this, "Image has been shared!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(listOfUsers.this, "There has been an issue uploading the image :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.up)
        {
            if(ContextCompat.checkSelfPermission(listOfUsers.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                getPhoto();
            else
                Dexter.withActivity(this)
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
                                    AlertDialog.Builder ad=new AlertDialog.Builder(listOfUsers.this);
                                    ad.setTitle("Permission Denied")
                                            .setMessage("Enable External Storage Access in settings")
                                            .setIcon(R.drawable.ic_launcher_background)
                                            .setNegativeButton("Cancel",null)
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


        }
        else if(item.getItemId()==R.id.cap)
        {
            if(ContextCompat.checkSelfPermission(listOfUsers.this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)
            {
                capturePhoto();
            }
            else
                Dexter.withActivity(this)
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
        else if(item.getItemId()==R.id.logOut)
        {
            ParseUser.logOut();
            Intent i=new Intent(listOfUsers.this,MainActivity.class);
            startActivity(i);
        }
        else if(item.getItemId()==R.id.prof)
        {
            viewFeed(ParseUser.getCurrentUser().getUsername());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_users);
        setTitle("Friends:");
TextView tt=(TextView) findViewById(R.id.textView);
tt.setText(ParseUser.getCurrentUser().getUsername());
         lv=(ListView) findViewById(R.id.list);
        al=new ArrayList<>();
        ad=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,al);
        ParseQuery<ParseUser> pq=ParseUser.getQuery();
        pq.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        pq.addAscendingOrder("username");
        pq.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null && objects.size()>0)
                {
                   for(ParseUser p:objects)
                   {

                       al.add(p.getUsername());
                       ad.notifyDataSetChanged();
                   }
                   lv.setAdapter(ad);


                }
                else
                    e.printStackTrace();

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 viewFeed(al.get(position));
            }
        });

    }
    public void viewFeed(String username)
    {
        Intent i=new Intent(listOfUsers.this,basic.class);
        i.putExtra("username",username);
        startActivity(i);
    }
}
