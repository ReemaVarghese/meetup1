package com.meetup.meetupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpcheck extends AppCompatActivity {

    TextView t1;
    EditText e1;
    Button b1;
    ProgressBar p1;

    String eo;
    String ro;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpcheck);

        t1=findViewById(R.id.txtchange);
        e1=findViewById(R.id.txtotp);
        b1=findViewById(R.id.verifyotp);
        p1=findViewById(R.id.progressbarofotpauth);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(otpcheck.this,MainActivity.class);
                startActivity(intent);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p1.setVisibility(View.VISIBLE);
                eo=e1.getText().toString();
                if(eo.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "enter the otp", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(eo.length()==6){
                        p1.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(otpcheck.this,insert.class);
                        startActivity(intent);
                        finish();
                    }
                    //p1.setVisibility(View.VISIBLE);
                    //ro=getIntent().getStringExtra("otp");
                    //PhoneAuthCredential credential=PhoneAuthProvider.getCredential(ro,eo);
                    //signIn(credential);
                }

            }
        });

    }

   /* private void signIn(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    p1.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(otpcheck.this,insert.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }*/

}