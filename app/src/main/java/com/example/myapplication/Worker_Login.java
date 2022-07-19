package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Worker_Login extends AppCompatActivity {

    Button send_otp;
    TextView dont_have_account, forgotPassword;
    TextInputLayout phone_number;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    CheckBox login_as_employer;
    String verificatioID;
    MainModel mainModel;
    TextInputLayout otpno;
    Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        forgotPassword = findViewById(R.id.forgot_password);

        send_otp = findViewById(R.id.send_otp_btn);
        dont_have_account = findViewById(R.id.do_not_have_an_account);
        phone_number = findViewById(R.id.phone_number_otp);
       // password = findViewById(R.id.login_password);

        otpno = findViewById(R.id.otp);
        verify = findViewById(R.id.verify_otp_btn);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(otpno.getEditText().getText().toString())) {
                    Toast.makeText(Worker_Login.this, "Wrong Otp Entered", Toast.LENGTH_SHORT).show();
                    
                }
                else{
                    verifyCode(otpno.getEditText().getText().toString());
                }

            }
        });

    login_as_employer = findViewById(R.id.employer_login_checkbox);
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(phone_number.getEditText().getText().toString()))
                {
                    Toast.makeText(Worker_Login.this, "Enter a valid number", Toast.LENGTH_SHORT).show();

                }
                else {
                    String number = phone_number.getEditText().getText().toString();
                    sendVerificationCode(number);
                }
            }
        });

        dont_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register_Worker.class));
            }
        });

        login_as_employer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

       /* loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(!validatePassword() | !validateEmail())) {
                    loginUser();
                    progressDialog.setMessage("Signing In...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            }
        });*/
    }

    private void sendVerificationCode(String PhoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+254"+PhoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            final  String code =phoneAuthCredential.getSmsCode();
            if (code!=null)
            {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(Worker_Login.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificatioID = s;
            Toast.makeText(Worker_Login.this, "Code is sent", Toast.LENGTH_SHORT).show();
            verify.setEnabled(true);
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificatioID,code);
        signInByCredentials(credential);
    }

    private void signInByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference("Employees")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            GlobalUser.currentUser = snapshot.getValue(Employee.class);
                                            Toast.makeText(Worker_Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), UserProfile.class));
                                            finish();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


   /* private boolean validateEmail() {
        String email_address = email.getEditText().getText().toString();
        if (email_address.isEmpty()) {
            email.setError("Please fill your email address");
            email.requestFocus();
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;

        }

    }

    private boolean validatePassword() {
        String password_filed = password.getEditText().getText().toString();
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

        if (password_filed.isEmpty()) {
            password.setError("Please fill your password");
            password.requestFocus();
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }


    }

    private void loginUser() {
        String email_address = email.getEditText().getText().toString();
        String password_filed = password.getEditText().getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Employees");
        Query checkUser = reference.orderByChild("email").equalTo(email_address);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    email.setError(null);
                    email.setErrorEnabled(false);
                    String phoneNoFromDb = snapshot.child(password_filed).child("mobile").getValue(String.class);

                    progressDialog.dismiss();
                    if (phoneNoFromDb.equals(password_filed)) {

                        email.setError(null);
                        email.setErrorEnabled(false);

                        String email = snapshot.child(password_filed).child("email").getValue(String.class);
                        String mobile = snapshot.child(password_filed).child("mobile").getValue(String.class);
                        String description = snapshot.child(password_filed).child("description").getValue(String.class);
                        String location = snapshot.child(password_filed).child("location").getValue(String.class);
                        String image = snapshot.child(password_filed).child("image").getValue(String.class);
                        String full_name = snapshot.child(password_filed).child("fname").getValue(String.class);


                        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                        intent.putExtra("email", email);
                        intent.putExtra("mobile", mobile);
                        intent.putExtra("description", description);
                        intent.putExtra("location", location);
                        intent.putExtra("image", image);
                        intent.putExtra("fname", full_name);
                        startActivity(intent);
                    } else {

                        password.setError("Wrong password");
                        password.requestFocus();
                    }
                } else {
                    Toast.makeText(Worker_Login.this, "No such user exist", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                   *//* email.setError("No such user exist");
                    email.requestFocus();*//*

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}