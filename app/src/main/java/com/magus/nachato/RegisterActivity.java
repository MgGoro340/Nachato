package com.magus.nachato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.magus.nachato.commons.SharedPreferencesManager;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etEmail;
    EditText etPassword;
    Button btnRegistrar;

    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etUsername = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        auth = FirebaseAuth.getInstance();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mando a registrar
                Register(etUsername.getText().toString(),etEmail.getText().toString(),etPassword.getText().toString());
            }
        });



    }


    public void Register (final String userName, String userEmail, String password) {


        auth.createUserWithEmailAndPassword(userEmail,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid  = firebaseUser.getUid();
                            String userEmail = firebaseUser.getEmail();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("userName",userName);
                            hashMap.put("imageURL","default");

                            SharedPreferencesManager.setSomeStringValue("userName",userName);
                            SharedPreferencesManager.setSomeStringValue("userEmail",userEmail);

                            reference.setValue(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent inten = new Intent(RegisterActivity.this,MainChatActivity.class);
                                            inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(inten);
                                            finish();


                                        }
                                    });

                        } else {
                            Toast.makeText(RegisterActivity.this, "no se pudo registrar nada" , Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
