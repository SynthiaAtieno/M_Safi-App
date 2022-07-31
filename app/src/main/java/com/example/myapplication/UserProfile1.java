package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile1 extends AppCompatActivity {

    ImageView select_image;
    Button upload;
    Uri imguri;
    RelativeLayout relativeLayout;
    DatabaseReference databaseReference;
    StorageReference reference;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    MainModel mainModel;
    ProgressDialog progressDialog;
    String uid;
    TextView fullname, emailtv, locationtv, descrtv,mobiletv;
    CircleImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile1);

        relativeLayout = findViewById(R.id.main_relativeLayout);
        fullname = findViewById(R.id.full_name_tv);
        locationtv = findViewById(R.id.location_tv);
        descrtv = findViewById(R.id.desc_tv);
        mobiletv = findViewById(R.id.phone_tv);
        emailtv = findViewById(R.id.email_tv);
        profile = findViewById(R.id.profile_image);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("Employees");


        if (!uid.isEmpty())
        {
            getUserData();
        }

        select_image= findViewById(R.id.profile_image);
        FirebaseRecyclerOptions<user_model> options= new FirebaseRecyclerOptions.Builder<user_model>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Employees"), user_model.class)
                .build();



    }
    private void getUserData() {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mainModel=snapshot.getValue(MainModel.class);
                locationtv.setText(GlobalUser.currentUser.getLocation());
                emailtv.setText(GlobalUser.currentUser.getEmail());
                descrtv.setText(GlobalUser.currentUser.getDescription());
                mobiletv.setText(GlobalUser.currentUser.getMobile());
                fullname.setText(GlobalUser.currentUser.getFname());
                profile.setImageURI(imguri);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });
    }

    private void uploadtofirebase(Uri uri) {
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        database = FirebaseDatabase.getInstance();
                        databaseReference = database.getReference("Employees");
                        profile.setImageURI(uri);

                        Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), UserProfile.class));
                        progressDialog.dismiss();
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
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
                Toast.makeText(getApplicationContext(), "Uploading failed" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private String getFileExtension (Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }




}







