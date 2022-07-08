package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;

import android.os.Bundle;

import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TextView verify_text;
    Button verify_account;
    NavigationView navigationView;
    FrameLayout frameLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseAuth mAuth;
    String uid;
    FirebaseUser user;
    BottomNavigationView bottomNavigationView;
    DatabaseReference databaseReference;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth= FirebaseAuth.getInstance();
        uid= user.getUid();
        verify_text = findViewById(R.id.verify_message);
        verify_account = findViewById(R.id.verify_account);
        frameLayout = findViewById(R.id.fragment_container);


        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_nav);
          actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_open, R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


        user = mAuth.getCurrentUser();

        if (!user.isEmailVerified())
        {

            verify_text.setVisibility(View.VISIBLE);
            verify_account.setVisibility(View.VISIBLE);
            navigationView.setVisibility(View.GONE);
            frameLayout.setVisibility(View.GONE);



            verify_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(MainActivity.this, "Verification Email has been sent to you", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(MainActivity.this, "Failed to send a verification email "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

              switch (item.getItemId()){
                  case R.id.nav_home:
                      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                              new HomeFragment()).commit();

                      break;

              }
                return true;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Help_and_Feedback:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HelpFragment()).commit();

                        break;

                    case R.id.about_nav:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new AboutFragment()).commit();
                        break;

                    case R.id.nav_settings:
                        startActivity(new Intent(getApplicationContext(), Preference.class));
                        break;

                    case R.id.nav_share:

                        Intent myIntent = new Intent(Intent.ACTION_SEND);
                        myIntent.setType("text/plain");
                        String shareBody = "Your body here";
                        String sharesub = "Your Subject here";
                        myIntent.putExtra(Intent.EXTRA_SUBJECT, sharesub);
                        myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(myIntent, "Share Via"));
                        break;

                    case R.id.nav_login:
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
    }

}