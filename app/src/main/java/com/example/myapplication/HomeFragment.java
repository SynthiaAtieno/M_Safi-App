package com.example.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    Toolbar toolbar;
    FloatingActionButton actionButton;
    FirebaseDatabase database = FirebaseDatabase.getInstance();



    Employee employee;
    public HomeFragment(){

    }


    public static class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public CustomLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        //... constructor
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "Inconsistency detected");
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        actionButton = view.findViewById(R.id.btnOpenDialog);
        AppCompatActivity activity= (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Swipe Right");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);


        recyclerView.setLayoutManager(new CustomLinearLayoutManager(activity));

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),FloatingActivity.class));
            }
        });

        FirebaseRecyclerOptions<Employee> options= new FirebaseRecyclerOptions.Builder<Employee>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Employees"), Employee.class)
                .build();


        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = user.getUid();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu1, menu);
        MenuItem menuItem = menu.findItem(R.id.search_id);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setIconified(true);

        SearchManager searchManager= (SearchManager) getActivity().getSystemService(getContext().SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mysearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mysearch(query);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void mysearch(String query) {

        FirebaseRecyclerOptions<Employee> options= new FirebaseRecyclerOptions.Builder<Employee>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Employees")
                        .orderByChild("location").startAt(query)
                        .endAt(query+"~"), Employee.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

}
