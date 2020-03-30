package com.magus.nachato;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.magus.nachato.Fragments.ChatsFragment;
import com.magus.nachato.Fragments.UsersFragment;
import com.magus.nachato.commons.SharedPreferencesManager;
import com.magus.nachato.model.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainChatActivity extends AppCompatActivity {


     CircleImageView profile_image;
     TextView tvUserName;
    Toolbar toolbar;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);


        toolbar = findViewById (R.id.toolbarMainChat);
        setSupportActionBar (toolbar);



        profile_image = findViewById (R.id.profile_image);
        tvUserName = findViewById(R.id.tvUserName);


        firebaseUser = FirebaseAuth.getInstance ().getCurrentUser ();
        reference = FirebaseDatabase.getInstance ().getReference ("Users").child (firebaseUser.getUid ());

        final String userName =SharedPreferencesManager.getSomeStringValue ("userName");


        reference.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue (User.class);


                tvUserName.setText(user.getUserName ());

                if (user.getImageURL ().equals ("default")) {
                    profile_image.setImageResource (R.mipmap.ic_launcher_glasses_round);
                } else {
                    Glide.with (MainChatActivity.this).load(user.getImageURL ()).into (profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TabLayout tabLayout = findViewById (R.id.tab_layout);
        ViewPager viewPager = findViewById (R.id.view_pager);
        //viewPager.setLayoutTransition (new LayoutTransition ().);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter (getSupportFragmentManager ());

        viewPagerAdapter.addFragment (new ChatsFragment (),"Nachats");
        viewPagerAdapter.addFragment (new UsersFragment (),"Users");

         viewPager.setAdapter (viewPagerAdapter);
         tabLayout.setupWithViewPager (viewPager);

        //tvUserName.setText(userName);
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




    class ViewPagerAdapter extends FragmentPagerAdapter {

       private ArrayList<Fragment> fragments;
       private ArrayList<String>  titles;


       ViewPagerAdapter (FragmentManager fm) {
           super (fm);
           this.fragments = new ArrayList<> ();
           this.titles = new ArrayList<> ();
       }


//        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
//            super (fm, behavior);
//        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size ();
        }


       public void addFragment( Fragment fragment, String title) {

            fragments.add(fragment);
            titles.add(title);

       }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get (position);
        }
    }


}
