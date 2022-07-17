package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    TextInputLayout fname, mobile, email, location, desc, image;
    TextView fullname, email_address;
    CircleImageView profile;
    Button update;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;



    DrawerLayout drawerLayout;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    String user_email ;
    String user_location;
    String user_mobile ;
    String full_name ;
    NavigationView navigationView;
    String user_desc ;
    String user_image;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view1);

        contextOfApplication = getApplicationContext();

        mAuth= FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Employees");

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_open, R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserFragment()).commit();
            navigationView.setCheckedItem(R.id.user_profile);
        }

        email = findViewById(R.id.email);
        location = findViewById(R.id.location);
        mobile = findViewById(R.id.phone);
        fname = findViewById(R.id.full_name);
        desc = findViewById(R.id.description);
        update = findViewById(R.id.update_btn);
         image= findViewById(R.id.image);
         fullname = findViewById(R.id.full_name1);
         profile = findViewById(R.id.profile_image);
         email_address = findViewById(R.id.email_address);

         showAllUserData();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Help_and_Feedback:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new HelpFragment()).commit();

                        break;
                    case R.id.user_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new UserFragment()).commit();

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
                        startActivity(new Intent(UserProfile.this, Login.class));
                        finish();
                        break;

                    case R.id.profile_nav:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ProfileFragment()).commit();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }



    private void showAllUserData() {

        Intent intent = getIntent();
        user_email = intent.getStringExtra("email");
        user_location = intent.getStringExtra("location");
        user_mobile = intent.getStringExtra("mobile");
        full_name = intent.getStringExtra("fname");
        user_desc = intent.getStringExtra("description");
        user_image = intent.getStringExtra("image");
        String profileImage = intent.getStringExtra("image");

        fname.getEditText().setText(full_name);
        location.getEditText().setText(user_location);
        desc.getEditText().setText(user_desc);
        mobile.getEditText().setText(user_mobile);
        email.getEditText().setText(user_email);
        fullname.setText(full_name);
        image.getEditText().setText(user_image);
        email_address.setText(user_email);



    }
    public void update(View view)
    {
        if (isNameChange() || isEmailChanged() || isLocation()|| isDesc()|| isImage()|| isMobile())
        {
            Toast.makeText(this, "Data has been changed Successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Data is same and cannot be updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMobile() {
        if (!user_mobile.equals(mobile.getEditText().getText().toString()))
        {
            reference.child(user_mobile).child("mobile").setValue(mobile.getEditText().getText().toString());
            user_mobile = mobile.getEditText().getText().toString();
            return true;

        }else{
            return false;
        }
    }

    private boolean isImage() {
        if (!user_image.equals(image.getEditText().getText().toString()))
        {
            reference.child(user_mobile).child("image").setValue(image.getEditText().getText().toString());
            user_image = image.getEditText().getText().toString();
            return true;

        }else{
            return false;
        }
    }

    private boolean isDesc() {
        if (!user_desc.equals(desc.getEditText().getText().toString()))
        {
            reference.child(user_mobile).child("description").setValue(desc.getEditText().getText().toString());
            user_desc = desc.getEditText().getText().toString();
            return true;

        }else{
            return false;
        }
    }

    private boolean isLocation() {
        if (!user_location.equals(location.getEditText().getText().toString()))
        {
            reference.child(user_mobile).child("location").setValue(location.getEditText().getText().toString());
            user_location = location.getEditText().getText().toString();
            return true;

        }else{
            return false;
        }
    }

    private boolean isEmailChanged() {
        if (!user_email.equals(email.getEditText().getText().toString()))
        {
            reference.child(user_mobile).child("email").setValue(email.getEditText().getText().toString());
            user_email = email.getEditText().getText().toString();
            return true;

        }else{
            return false;
        }
    }


    private boolean isNameChange() {
        if (!full_name.equals(fname.getEditText().getText().toString()))
        {
            reference.child(user_mobile).child("fname").setValue(fname.getEditText().getText().toString());
            full_name = fname.getEditText().getText().toString();
            return true;

        }else{
            return false;
        }
    }

}