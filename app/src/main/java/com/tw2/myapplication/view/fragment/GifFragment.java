package com.tw2.myapplication.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tw2.myapplication.R;
import com.tw2.myapplication.adapter.GifAdapter;
import com.tw2.myapplication.adapter.GuestAdapter;
import com.tw2.myapplication.model.Gif;
import com.tw2.myapplication.model.Guest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GifFragment extends Fragment {
    private View view;
    private List<Gif> list;
    private RecyclerView recyclerView;
    private GifAdapter adapter;
    private DatabaseReference mReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gif, container, false);
        mReference = FirebaseDatabase.getInstance().getReference();
        initView();
        initData();
        return view;
    }

    private void initData() {
        list = new ArrayList<>();
        list.clear();
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }

        adapter = new GifAdapter(list, getContext());
        recyclerView.setAdapter(adapter);

        mReference.child("gif").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String image = dataSnapshot.getValue(String.class);
                Gif gif = new Gif(image, dataSnapshot.getKey());
                list.add(gif);

                Collections.sort(list, new Comparator<Gif>() {
                    @Override
                    public int compare(final Gif object1, final Gif object2) {
                        return Integer.parseInt(object2.getId()) - Integer.parseInt(object1.getId());
                    }
                });

                adapter = new GifAdapter(list, getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_gif);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }
}
