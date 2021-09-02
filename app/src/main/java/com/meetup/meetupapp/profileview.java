package com.meetup.meetupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class profileview extends AppCompatActivity {

    EditText uname;
    EditText uintr;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;

    ImageView mviewuserimageinimageview;

    StorageReference storageReference;

    private String ImageURIacessToken;

    androidx.appcompat.widget.Toolbar mtoolbarofviewprofile;
    ImageButton mbackbuttonofviewprofile;

    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileview);

        uname=findViewById(R.id.viewusername);
        uintr=findViewById(R.id.intr);
        mtoolbarofviewprofile=findViewById(R.id.toolbarofviewprofile);
        mbackbuttonofviewprofile=findViewById(R.id.backbuttonofviewprofile);
        firebaseStorage=FirebaseStorage.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        setSupportActionBar(mtoolbarofviewprofile);

        mbackbuttonofviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        storageReference=firebaseStorage.getReference();
        storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURIacessToken=uri.toString();
                Picasso.get().load(uri).into(mviewuserimageinimageview);
            }
        });

        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userprofile muser=snapshot.getValue(userprofile.class);
                uintr.setText(muser.getInserest());
                uname.setText(muser.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}