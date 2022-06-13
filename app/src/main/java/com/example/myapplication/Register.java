package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityRegisterBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    Button regbtn;
    TextView have_account;
    TextInputLayout email, password, con_pass;

    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        LinearLayout linearLayout = findViewById(R.id.main_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        regbtn = findViewById(R.id.register_btn);
        have_account = findViewById(R.id.have_an_account);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        con_pass = findViewById(R.id.confirm_password);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail() | !validatePassword() | !CheckPassword()) {
                    progressDialog.dismiss();
                    return;
                } else {
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Register.this, Login.class));
                    finish();
                }

            }

        });

        have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });


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
            return true;

        }

    }

    private boolean validatePassword() {
        String password_filed = password.getEditText().getText().toString();
        String confirm_password = con_pass.getEditText().getText().toString();
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

        if (password_filed.isEmpty()) {
            password.setError("Please fill your password");
            password.requestFocus();
            return false;
        } else if (!password_filed.matches(PASSWORD_PATTERN)) {
            password.setError("Password must be at least 4 characters long, one uppercase, one letter and a symbol ");
            password.requestFocus();
            return false;
        } else {
            password.setError(null);
            return true;
        }


    }

    private boolean CheckPassword() {
        String password_filed = password.getEditText().getText().toString();
        String confirm_password = con_pass.getEditText().getText().toString();

        if (!password_filed.equals(confirm_password)) {
            con_pass.setError("Password does not match");
            con_pass.requestFocus();
            return false;
        } else {
            con_pass.setError(null);
            return true;
        }
    }
}