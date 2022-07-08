package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<Employee, MainAdapter.myViewHolder> {

   /* DatabaseReference favouriteref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();*/
    public MainAdapter(@NonNull FirebaseRecyclerOptions<Employee> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Employee model) {

        holder.full_name.setText(model.getFname());
        holder.mobile_no.setText(model.getMobile());
        holder.email_address.setText(model.getEmail());
        holder.current_location.setText(model.getLocation());
        holder.your_description.setText(model.getDescription());

        Glide.with(holder.img.getContext())
                .load(model.getImage())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop().error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal).into(holder.img);


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup)).setExpanded(true,
                                500).create();

                //dialogPlus.show();
                View view1 = dialogPlus.getHolderView();

                TextInputLayout name = view1.findViewById(R.id.update_full_name);
                TextInputLayout mobile = view1.findViewById(R.id.update_phone);
                TextInputLayout email = view1.findViewById(R.id.update_email);
                TextInputLayout location = view1.findViewById(R.id.update_location);
                TextInputLayout desc = view1.findViewById(R.id.update_description);
                TextInputLayout image = view1.findViewById(R.id.update_image);

                Button update = view1.findViewById(R.id.update_btn);

                name.getEditText().setText(model.getFname());
                mobile.getEditText().setText(model.getMobile());
                email.getEditText().setText(model.getEmail());
                location.getEditText().setText(model.getLocation());
                desc.getEditText().setText(model.getDescription());
                image.getEditText().setText(model.getImage());

                dialogPlus.show();
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("fname", name.getEditText().getText().toString());
                        map.put("email",email.getEditText().getText().toString());
                        map.put("location", location.getEditText().getText().toString());
                        map.put("description",desc.getEditText().getText().toString());
                        map.put("mobile", mobile.getEditText().getText().toString());
                        map.put("image",image.getEditText().getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Employees")
                                .child(getRef(holder.getAbsoluteAdapterPosition()).getKey()).updateChildren(map)
                                //.child(getRef().getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(holder.full_name.getContext(), "updated successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.full_name.getContext(), "Error Occurred while updating"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                });


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.full_name.getContext());
                builder.setTitle("Are You Sure");
                builder.setMessage("Deleted Data cannot be undone");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseDatabase.getInstance().getReference().child("Employees")
                                .child(getRef(holder.getAdapterPosition()).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(holder.full_name.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
       /* FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = user.getUid();

        final String postKey = getRef(position).getKey();


        String name = getItem(position).getFname();
        String mobile = getItem(position).getMobile();
        String email = getItem(position).getEmail();
        String location = getItem(position).getLocation();
        String description = getItem(position).getDescription();
        String url = getItem(position).getImage();

        DatabaseReference databaseReference,fvrtref;

        holder.favouriteChecker(postKey);
        final Boolean[] fvrtChecker = {false};
        holder.fvrt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fvrtChecker[0] = true;
                favouriteref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (fvrtChecker.equals(true))
                        {
                            if (snapshot.child(postKey).hasChild(currentUser))
                            {
                                fvrtref.child()
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });*/
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView full_name, mobile_no, email_address,current_location,your_description;
        Button edit, delete, update;




        public myViewHolder (@NonNull View itemView) {
            super(itemView);


            img = (CircleImageView)itemView.findViewById(R.id.circle_image);

            full_name = itemView.findViewById(R.id.full_name_text);
            mobile_no = itemView.findViewById(R.id.mobile_text);
            email_address = itemView.findViewById(R.id.email_text);


            current_location =itemView.findViewById(R.id.location_text);
            your_description =itemView.findViewById(R.id.desc_text);

            edit = itemView.findViewById(R.id.button_edit);
            delete = itemView.findViewById(R.id.button_delete);
            update = itemView.findViewById(R.id.update_btn);
            //fvrt_btn = itemView.findViewById(R.id.favorite_border);






        }
    }
}
