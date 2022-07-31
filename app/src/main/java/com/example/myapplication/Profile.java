package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {

    BottomNavigationView navigationView;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        navigationView = findViewById(R.id.bottom_nav1);

        navigationView.setSelectedItemId(R.id.profile_nav);
        mAuth= FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Employees");



        navigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), UserProfile.class));
                        overridePendingTransition(0,0);
                        finish();

                    case R.id.profile_nav:

                        return;

                    case R.id.logout:
                        mAuth.signOut();
                        startActivity(new Intent(Profile.this, Worker_Login.class));
                        finish();
                        break;
                }
            }
        });
    }
}