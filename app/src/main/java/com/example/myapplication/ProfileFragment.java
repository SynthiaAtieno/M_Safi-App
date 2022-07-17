package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    TextInputLayout fname, mobile, email, location, desc;
    Button btn_save, btn_close;

    String user_email ;
    String user_location;
    String user_mobile ;
    String full_name ;
    NavigationView navigationView;
    String user_desc ;
    String user_image;

    DatabaseReference reference;
    FirebaseAuth mAuth;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fname =(TextInputLayout) view.findViewById(R.id.full_name);
        mobile = (TextInputLayout) view.findViewById(R.id.phone);
        email = (TextInputLayout) view.findViewById(R.id.email);
        location = (TextInputLayout) view.findViewById(R.id.location);
        desc = (TextInputLayout) view.findViewById(R.id.description);


        btn_save = (Button) view.findViewById(R.id.btnSave);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !validateLocation() | !validateEmail()| !validateMobile() | !validateFullName() )
                {
                    return;
                }
                else
                {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Employee1");
                    Toast.makeText(getActivity(), "Profile created successfully", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        return view;

    }

    private boolean validateEmail() {
        String email_address = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email_address.isEmpty()) {
            email.setError("Please fill your email address");
            email.requestFocus();
            return false;
        } else if (!email_address.matches(emailPattern)) {
            email.setError("Please enter a valid email address");
            email.requestFocus();
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;

        }

    }

    private boolean validateFullName() {
        String Full_name = fname.getEditText().getText().toString();


        if (Full_name.isEmpty()) {
            fname.setError("Please fill your full name");
            fname.requestFocus();
            return false;
        }
        else {
            fname.setError(null);
            fname.setErrorEnabled(false);
            return true;
        }

    }
    private boolean validateMobile() {
        String phone = mobile.getEditText().getText().toString();


        if (phone.isEmpty()) {
            mobile.setError("Please fill your phone number");
            mobile.requestFocus();
            return false;
        }
        else {
            mobile.setError(null);
            mobile.setErrorEnabled(false);
            return true;
        }

    }
    private boolean validateLocation() {
        String current_location = location.getEditText().getText().toString();


        if (current_location.isEmpty()) {
            location.setError("Please fill your email address");
            location.requestFocus();
            return false;
        }
        else {
            location.setError(null);
            location.setErrorEnabled(false);
            return true;
        }

    }
    private void showAllUserData() {

        Intent intent = new Intent(getContext(),ProfileFragment.class);
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
        fname.getEditText().setText(full_name);
        email.getEditText().setText(user_email);



    }
    public void update(View view)
    {
        if (isNameChange() || isEmailChanged() || isLocation()|| isDesc()/*|| isImage()*/|| isMobile())
        {
            Toast.makeText(getActivity(), "Data has been changed Successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Data is same and cannot be updated", Toast.LENGTH_SHORT).show();
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
