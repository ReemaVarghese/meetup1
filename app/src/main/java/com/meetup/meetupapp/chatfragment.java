package com.meetup.meetupapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class chatfragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
        ImageView mimageviewofuser;

    FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> chatAdapter;


    RecyclerView mrecyclerview;
    private FirebaseUser firebaseUser1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chatfragment, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mrecyclerview = v.findViewById(R.id.recyclerview);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

       /* final String[] uint = new String[1];
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userprofile muser=snapshot.getValue(userprofile.class);
                uint[0] =muser.getInserest();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        if(uint[0].isEmpty())
        {
            uint[0] ="football";
        }
*/

        //Query q1 = firebaseFirestore.collection("users").whereEqualTo("uid", firebaseAuth.getUid());
         Query q1=firebaseFirestore.collection("users").whereNotEqualTo("uid",firebaseAuth.getUid());
       //Query q2 = firebaseFirestore.collection("users").whereEqualTo("interest", uint[0]);
        //Query q3 = firebaseFirestore.collection("users").whereEqualTo("interest", "cricket");


        FirestoreRecyclerOptions<firebasemodel> a1=new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(q1, firebasemodel.class).build();
        /*FirestoreRecyclerOptions<firebasemodel> a2=new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(q2, firebasemodel.class).build();
        FirestoreRecyclerOptions<firebasemodel> a3=new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(q3, firebasemodel.class).build();
        FirestoreRecyclerOptions<firebasemodel> allusername;*/

      /*  if(a1.equals(a3))
        {
            allusername=a1;
        }
        else
        {
           allusername=a1;
        }*/


        chatAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(a1) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {
                noteViewHolder.particularusername.setText(firebasemodel.getName());
                String uri = firebasemodel.getImage();
                Picasso.get().load(uri).into(mimageviewofuser);

                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), specificchat.class);
                        intent.putExtra("name", firebasemodel.getName());
                        intent.putExtra("receiveruid", firebasemodel.getUid());
                        intent.putExtra("imageuri", firebasemodel.getImage());
                        startActivity(intent);
                    }
                });
            }


            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout, parent, false);
                return new NoteViewHolder(view);
            }
        };
        mrecyclerview.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerview.setLayoutManager(linearLayoutManager);
        mrecyclerview.setAdapter(chatAdapter);


        return v;
    }




    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView particularusername;
        private TextView statusofuser;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            particularusername = itemView.findViewById(R.id.nameofuser);
            mimageviewofuser = itemView.findViewById(R.id.imageviewofuser);


        }

    }
    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatAdapter!=null)
        {
            chatAdapter.stopListening();
        }
    }
}

