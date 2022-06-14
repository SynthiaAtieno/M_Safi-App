package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button loginbtn;
    TextView dont_have_account, forgot_password;
    TextInputLayout email, password;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LinearLayout linearLayout= findViewById(R.id.main_layout);
        AnimationDrawable animationDrawable= (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        progressDialog= new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();

        loginbtn = findViewById(R.id.login_btn);
        dont_have_account = findViewById(R.id.do_not_have_an_account);
        email= findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        forgot_password = findViewById(R.id.forgot_password);

        dont_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail() | !validatePassword())
                {
                    progressDialog.dismiss();
                }
                else {

                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    loginUser();

                }

            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail=new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialoq =new AlertDialog.Builder(view.getContext());
                passwordResetDialoq.setTitle("Enter your Email To reset password");
                passwordResetDialoq.setView(resetMail);
            }
        });
    }
    private boolean validateEmail()
    {
        String email_address = email.getEditText().getText().toString();
        if (email_address.isEmpty()) {
            email.setError("Please fill your email address");
            email.requestFocus();
            return false;
        }
        else {
            email.setError(null);
            return true;

        }

    }
    private  boolean validatePassword()
    {
        String password_filed = password.getEditText().getText().toString();
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

        if (password_filed.isEmpty())
        {
            password.setError("Please fill your password");
            password.requestFocus();
            return false;
        }
        else
        {
            password.setError(null);
            return true;
        }


    }

    private void loginUser(){
        String email_address = email.getEditText().getText().toString();
        String password_filed = password.getEditText().getText().toString();

        mAuth.signInWithEmailAndPassword(email_address, password_filed).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {


                    Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(Login.this, "Login Failed"+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });

    }

}