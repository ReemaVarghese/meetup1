package com.meetup.meetupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class chat extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_launcher_foreground);
        mtoolbar.setOverflowIcon(drawable);




    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.profile:
                Intent intent=new Intent(chat.this,profileview.class);
                startActivity(intent);
                break;

            case R.id.settings:
                Toast.makeText(getApplicationContext(),"Settings is clicked",Toast.LENGTH_SHORT).show();
                break;
        }



        return  true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}