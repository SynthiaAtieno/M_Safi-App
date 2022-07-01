package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<Employee, MainAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
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


        public myViewHolder (@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView)itemView.findViewById(R.id.circle_image);

            full_name = itemView.findViewById(R.id.full_name_text);
            mobile_no = itemView.findViewById(R.id.mobile_text);
            email_address = itemView.findViewById(R.id.email_text);


            current_location =itemView.findViewById(R.id.location_text);
            your_description =itemView.findViewById(R.id.desc_text);


        }
    }
}
