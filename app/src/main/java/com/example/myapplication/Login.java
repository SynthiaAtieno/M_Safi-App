package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    Button loginbtn;
    TextView dont_have_account, forgotPassword;
    TextInputLayout email, password;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    CheckBox login_as_a_worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog= new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        forgotPassword= findViewById(R.id.forgot_password);

        loginbtn = findViewById(R.id.login_btn);
        dont_have_account = findViewById(R.id.do_not_have_an_account);
        email= findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        login_as_a_worker = findViewById(R.id.worker_login_checkbox);

        login_as_a_worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(),Worker_Login.class));
               finish();
            }
        });



        dont_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !validatePassword() |!validateEmail() )
                {
                    progressDialog.dismiss();
                }
                else {


                    progressDialog.setMessage("Signing In...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    loginUser();

                }

            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordReset = new AlertDialog.Builder(view.getContext());
                passwordReset.setTitle("Reset Password");
                passwordReset.setMessage("Enter Your Email To Receive Reset Link.");
                passwordReset.setView(resetMail);

                passwordReset.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetMail.getText().toString();
                        if (mail.isEmpty()){
                            resetMail.setError("Please enter your email address");
                            resetMail.requestFocus();
                        }else
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login.this, "Reset Link Has Been Sent to Your Email", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error, Reset Link Has Nt Been Sent "+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                passwordReset.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                passwordReset.show();
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
            email.setErrorEnabled(false);
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
            password.setErrorEnabled(false);
            return true;
        }


    }

    private void loginUser(){
        String email_address = email.getEditText().getText().toString();
        String password_filed = password.getEditText().getText().toString();

       /* DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Employees");
        Query checkUser = reference.orderByChild("email").equalTo(email_address);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    email.setError(null);
                    email.setErrorEnabled(false);
                    String phoneNoFromDb = snapshot.child(email_address).child("mobile").getValue(String.class);

                    if (phoneNoFromDb.equals(password_filed)){

                        email.setError(null);
                        email.setErrorEnabled(false);

                        String email = snapshot.child(email_address).child("email").getValue(String.class);
                        String mobile = snapshot.child(email_address).child("mobile").getValue(String.class);
                        String description = snapshot.child(email_address).child("description").getValue(String.class);
                        String location = snapshot.child(email_address).child("location").getValue(String.class);
                        String image = snapshot.child(email_address).child("image").getValue(String.class);
                        String full_name = snapshot.child(email_address).child("fname").getValue(String.class);


                        Intent intent = new Intent(getApplicationContext(),UserProfile.class);
                        intent.putExtra("email", email);
                        intent.putExtra("mobile", mobile);
                        intent.putExtra("description", description);
                        intent.putExtra("location", location);
                        intent.putExtra("image", image);
                        intent.putExtra("fname", full_name);
                        startActivity(intent);
                    }
                    else {
                        password.setError("Wrong password");
                        password.requestFocus();
                    }
                }
                else
                {
                    email.setError("No such user exist");
                    email.requestFocus();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        mAuth.signInWithEmailAndPassword(email_address, password_filed).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {


                    Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                }
                else
                {
                    Toast.makeText(Login.this, "Login Failed"+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();
                }
            }
        });

    }

}