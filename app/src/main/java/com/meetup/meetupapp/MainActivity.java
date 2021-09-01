package com.meetup.meetupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText phno;
    Button b1;
    CountryCodePicker cp;
    ProgressBar pgrs;

    String countrycode;
    String phonenumber;
    String codesent;

    FirebaseAuth firebaseAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phno = findViewById(R.id.txtnumber);
        b1 = findViewById(R.id.btnsend);
        cp = findViewById(R.id.countrycodepicker);
        pgrs = findViewById(R.id.progressbarofmain);

        firebaseAuth = FirebaseAuth.getInstance();

        countrycode = cp.getSelectedCountryCodeWithPlus();

        cp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode = cp.getSelectedCountryCodeWithPlus();
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String no;
                no = phno.getText().toString();
                if (no.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter the number", Toast.LENGTH_SHORT).show();
                } else if (no.length() < 10) {
                    Toast.makeText(getApplicationContext(), "phone number should be more than 10 digits", Toast.LENGTH_SHORT).show();
                } else {
                    pgrs.setVisibility(View.VISIBLE);
                    phonenumber = countrycode + no;

                    PhoneAuthOptions options=PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phonenumber)
                            .setTimeout(60L,TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(mcallback)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);

                }
            }
        });

        mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(), "OTP has been sent", Toast.LENGTH_SHORT).show();
                pgrs.setVisibility(View.INVISIBLE);
                codesent=s;
                Intent intent=new Intent(MainActivity.this,otpcheck.class);
                intent.putExtra("otp",codesent);
                startActivity(intent);
            }
        };

    }

        @Override
        protected void onStart() {
            super.onStart();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Intent intent = new Intent(MainActivity.this, chat.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
}

