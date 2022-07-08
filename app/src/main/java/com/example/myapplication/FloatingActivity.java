package com.example.myapplication;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FloatingActivity extends AppCompatActivity {
    TextInputLayout email, location, mobile, fname, description;
    ProgressDialog progressDialog;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    MainModel mainModel;

    ImageView select_image;
    Uri imguri;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    Button create_profile, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);

        email = findViewById(R.id.email);
        location = findViewById(R.id.location);
        mobile = findViewById(R.id.phone);
        fname = findViewById(R.id.full_name);
        description = findViewById(R.id.description);
        create_profile = findViewById(R.id.save);
        select_image = findViewById(R.id.select_image);
        back = findViewById(R.id.back);

        progressDialog = new ProgressDialog(this);

       /* LinearLayout linearLayout = findViewById(R.id.fragment_profile);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
            }
        });

        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        create_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateEmail() | !validatePhone() | !validateLocation() | !validateFullName() | !validatedescription()) {
                    Toast.makeText(FloatingActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                } else {

                    if (imguri != null) {
                        uploadtofirebase(imguri);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();
                        progressDialog.setCanceledOnTouchOutside(false);

                    } else {

                        Toast.makeText(FloatingActivity.this, " select image", Toast.LENGTH_SHORT).show();
                    }
                }

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
            email.setErrorEnabled(false);
            return true;

        }

    }

    private boolean validateFullName() {
        String full_name = fname.getEditText().getText().toString();

        if (full_name.isEmpty()) {
            fname.setError("Please fill your full name");
            fname.requestFocus();
            return false;
        } else {
            fname.setError(null);
            fname.setErrorEnabled(false);
            return true;

        }

    }

    private boolean validateLocation() {
        String loc = location.getEditText().getText().toString();

        if (loc.isEmpty()) {
            location.setError("Please fill your location");
            location.requestFocus();
            return false;
        } else {
            location.setError(null);
            location.setErrorEnabled(false);
            return true;

        }

    }

    private boolean validatePhone() {
        String phone_number = mobile.getEditText().getText().toString();

        if (phone_number.isEmpty()) {
            mobile.setError("Please fill your mobile number");

            mobile.requestFocus();
            return false;
        } else {
            mobile.setError(null);
            mobile.setErrorEnabled(false);
            return true;

        }

    }

    private boolean validatedescription() {
        String desc = description.getEditText().getText().toString();

        if (desc.isEmpty()) {
            description.setError("Please fill your mobile number");

            description.requestFocus();
            return false;
        } else {
            description.setError(null);
            description.setErrorEnabled(false);
            return true;

        }

    }

    private boolean validateimageLink() {
        String phone_number = mobile.getEditText().getText().toString();

        if (phone_number.isEmpty()) {
            mobile.setError("Please fill your mobile number");

            mobile.requestFocus();
            return false;
        } else {
            mobile.setError(null);
            mobile.setErrorEnabled(false);
            return true;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imguri = data.getData();
            select_image.setImageURI(imguri);
        }
    }

    private void uploadtofirebase(Uri uri) {
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("Employees");

                        String full_name = fname.getEditText().getText().toString();
                        String email_address = email.getEditText().getText().toString();
                        String c_location = location.getEditText().getText().toString();
                        String desc = description.getEditText().getText().toString();
                        String mob = mobile.getEditText().getText().toString();

                        mainModel = new MainModel(full_name, email_address, c_location, desc, mob, uri.toString());
                        reference.child(mob).setValue(mainModel);


                        Toast.makeText(FloatingActivity.this, "Uploaded Succsefully", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), Login.class));
                        progressDialog.dismiss();
                    }
                });

            }
        });/*.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                progressDialog.setMessage("Uploading image, please wait...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(FloatingActivity.this, "Uploading failed" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });*/
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
/*
    private void ClearField()
    {
        fname.getEditText().setText("");
        location.getEditText().setText("");
        email.getEditText().setText("");
        mobile.getEditText().setText("");
        description.getEditText().setText("");

    }*/
}