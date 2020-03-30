package com.magus.nachato;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.magus.nachato.model.User;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;


    FirebaseUser firebaseuser;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText  text_send;

    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_message);

        Toolbar toolbar = findViewById (R.id.toolbarMainChat);
        setSupportActionBar(toolbar);
        getSupportActionBar ().setTitle ("");
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        toolbar.setNavigationOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profile_image = findViewById (R.id.profile_image);
        username = findViewById (R.id.tvUserName);

        btn_send = findViewById (R.id.btn_send);
        text_send = findViewById (R.id.text_send);

        intent = getIntent ();
        final String userid = intent.getStringExtra ("userid");


        firebaseuser = FirebaseAuth.getInstance ().getCurrentUser ();

        btn_send.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText ().toString ();

                if (!msg.equals ("")){
                    sendMessage (firebaseuser.getUid (),userid,msg);
                } else {
                    Toast.makeText (MessageActivity.this,"el mensaje esta vacío",Toast.LENGTH_LONG).show ();
                }
            }
        });


        reference = FirebaseDatabase.getInstance ().getReference ("Users").child (userid);


        reference.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user =dataSnapshot.getValue (User.class) ;
                username.setText (user.getUserName ());

                if ( user.getImageURL ().equals ("default")){
                    profile_image.setImageResource (R.mipmap.ic_launcher_glasses_round);
                } else{
                   Glide.with(MessageActivity.this).load(user.getImageURL ()).into(profile_image) ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        }


        private void sendMessage(String sender, String receiver, String message) {

            DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();


            HashMap<String,Object> hashMap = new HashMap<> ();
            hashMap.put("sender",sender);
            hashMap.put("receiver",receiver);
            hashMap.put("message",message);

            reference.child ("Chats").push ().setValue (hashMap);
        }






}
