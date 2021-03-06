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

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

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
        ro=getIntent().getStringExtra("otp");
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
                   /* if(eo.length()==6){
                        p1.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(otpcheck.this,insert.class);
                        startActivity(intent);
                        finish();
                    }*/
                    if(ro!=null)
                    {
                        p1.setVisibility(View.VISIBLE);
                        PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(ro,eo);

                        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    p1.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(otpcheck.this,insert.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    //finish();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "enter correct OTP", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                    }



                    //signIn(credential);
                }

            }
        });

    }

   /* private void signIn(PhoneAuthCredential credential) {


    }*/

}