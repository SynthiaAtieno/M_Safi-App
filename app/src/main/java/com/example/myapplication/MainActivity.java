package com.example.myapplication;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        AnimationDrawable animationDrawable= (AnimationDrawable) drawerLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        //This is a comment


        navigationView = findViewById(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout , R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_home:
                        Log.i("MENU_DRAWER_ITEM", "Home item is clicked: ");
                        break;

                    case R.id.nav_search:
                        Log.i("MENU_DRAWER_ITEM", "Search item is clicked: ");
                        break;

                    case R.id.nav_profile:
                        //Log.i("MENU_DRAWER_ITEM", "Profile item is clicked: ");
                        startActivity(new Intent(MainActivity.this, Update_profile.class));
                        break;

                    case R.id.nav_settings:
                        Log.i("MENU_DRAWER_ITEM", "Setting item is clicked: ");
                        break;

                    case R.id.nav_share:
                        Log.i("MENU_DRAWER_ITEM", "Share item is clicked: ");
                        break;

                    case R.id.nav_donate:
                        Log.i("MENU_DRAWER_ITEM", "Donate item is clicked: ");
                        break;

                    case R.id.nav_login:
                        Log.i("MENU_DRAWER_ITEM", "Logout item is clicked: ");
                        break;
                }
                return true;
            }
        });

    }
}