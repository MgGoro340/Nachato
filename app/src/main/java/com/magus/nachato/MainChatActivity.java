package com.magus.nachato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.magus.nachato.commons.SharedPreferencesManager;

public class MainChatActivity extends AppCompatActivity {


     TextView tvUserName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);


        tvUserName = findViewById(R.id.tvUserName);
        String userName =SharedPreferencesManager.getSomeStringValue ("userName");

        tvUserName.setText(userName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId ()){
            case R.id.logout:
                FirebaseAuth.getInstance ().signOut ();
                Intent inten  = new Intent(MainChatActivity.this , MainActivity.class);
                startActivity (inten);
                finish ();
                return true;
        }

        return false;
    }
}
