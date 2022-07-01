package com.example.myapplication;

import android.app.SearchManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    Toolbar toolbar;
    private SearchView searchView;
    private MenuItem menuItem;
    public HomeFragment(){

    }

   /* public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1,param1);
        args.putString(ARG_PARAM2,param2);

        fragment.setArguments(args);
        return fragment;

    }*/


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        toolbar = view.findViewById(R.id.toolbar);

        AppCompatActivity activity= (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Swipe Right");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu1, menu);
        menuItem = menu.findItem(R.id.search_id);
        searchView= (SearchView) MenuItemCompat.getActionView(menuItem);
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
