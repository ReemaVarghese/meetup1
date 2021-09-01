package com.meetup.meetupapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class insert extends AppCompatActivity {

    private CardView mgetuserimage;
    private ImageView mgetuserimageinimageview;
    private static int PICK_IMAGE=123;
    private Uri imagepath;

    private RadioGroup rinterest;
    private RadioButton rcri,rfoot;

    private EditText e1;
    private Button b1;

    private FirebaseAuth firebaseAuth;
    private String name;
    private String radiovalue;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private String ImageUriAcessToken;

    private FirebaseFirestore firebaseFirestore;

    ProgressBar mprogressbarofsetprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=firebaseFirestore.getInstance();

        e1=findViewById(R.id.txtname);
        mgetuserimage=findViewById(R.id.getuserimage);

        rcri=findViewById(R.id.rcricket);
        rfoot=findViewById(R.id.rfootball);
        mgetuserimageinimageview=findViewById(R.id.imageview);
        b1=findViewById(R.id.btnsave);
        mprogressbarofsetprofile=findViewById(R.id.progressbarofsetProfile);


        mgetuserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=e1.getText().toString();
                rinterest=findViewById(R.id.rinterest);
                String radiovalue = ((RadioButton)findViewById(rinterest.getCheckedRadioButtonId())).getText().toString();
                if(name.isEmpty() && radiovalue.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
                else if(imagepath==null)
                {
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mprogressbarofsetprofile.setVisibility(View.VISIBLE);
                    sendDataForNewUser();
                    mprogressbarofsetprofile.setVisibility(View.INVISIBLE);
                    Intent intent= new Intent(insert.this,chat.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    private void sendDataForNewUser() {
        sendDataToRealTimeDatabase();

    }

    private void sendDataToRealTimeDatabase() {
        name=e1.getText().toString().trim();
        rinterest=findViewById(R.id.rinterest);
        radiovalue = ((RadioButton)findViewById(rinterest.getCheckedRadioButtonId())).getText().toString();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());

        userprofile mup=new userprofile(name,radiovalue,firebaseAuth.getUid());
        databaseReference.setValue(mup);
        Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
        sendImagetoStorage();
    }

    private void sendImagetoStorage() {
        StorageReference imageref=storageReference.child("Images").child(firebaseAuth.getUid()).child("profile pic");

        Bitmap bitmap=null;
        try{
            bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data=byteArrayOutputStream.toByteArray();

        UploadTask uploadTask=imageref.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       ImageUriAcessToken=uri.toString();
                       Toast.makeText(getApplicationContext(), "URI got Success", Toast.LENGTH_SHORT).show();
                       sendDatatocloudFirestore();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getApplicationContext(), "URI got failed", Toast.LENGTH_SHORT).show();
                   }
               });
                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Image not Uploaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendDatatocloudFirestore() {
        DocumentReference documentReference=firebaseFirestore.collection("users").document(firebaseAuth.getUid());
        Map<String,Object> userdata=new HashMap<>();
        userdata.put("name",name);
        userdata.put("interest",radiovalue);
        userdata.put("image",ImageUriAcessToken);
        userdata.put("uid",firebaseAuth.getUid());

        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "cloud firestore send", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==PICK_IMAGE && requestCode==RESULT_OK)
        {
            imagepath=data.getData();
            mgetuserimageinimageview.setImageURI(imagepath);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

}