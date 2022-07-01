package com.example.myapplication;

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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {
    TextInputLayout fname, mobile, email, location, desc;
    Button create_profile;



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

        create_profile = (Button) view.findViewById(R.id.create_profile);

        create_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !validateLocation() | !validateEmail()| !validateMobile() | !validateFullName() )
                {
                    return;
                }
                else
                {
                    /*MainModel mainModel = new MainModel(fname.getEditText().getText()
                    ,mobile.getEditText().getText().toString(),
                            email.getEditText().getText(),
                            location.getEditText().getText(),
                            desc.getEditText().getText());*/
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

}
