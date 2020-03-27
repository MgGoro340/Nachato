package com.magus.nachato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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

public class IngresarActivity extends AppCompatActivity {


    EditText etEmail;
    EditText etPassword;
    Button btnIngresar;

    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);



        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnIngresar = findViewById(R.id.btnIngresar);

        auth = FirebaseAuth.getInstance();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mando a registrar
                doLogin(etEmail.getText().toString(),etPassword.getText().toString());
            }
        });



    }


    public void doLogin (String eMail, String password) {

        auth.signInWithEmailAndPassword(eMail,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();


                            if (firebaseUser != null) {
                                // Name, email address, and profile photo Url
                                String userName = firebaseUser.getDisplayName ();
                                String userEmail = firebaseUser.getEmail();
                                if ( userName == null ) {
                                    userName = userEmail;
                                }
                                SharedPreferencesManager.setSomeStringValue("userName",userName);
                                SharedPreferencesManager.setSomeStringValue("userEmail",userEmail);

                            }

                            Intent inten = new Intent(IngresarActivity.this,MainChatActivity.class);
                            inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(inten);
                            finish();

                        } else {
                            Toast.makeText(IngresarActivity.this, "no se pudo logear" , Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
