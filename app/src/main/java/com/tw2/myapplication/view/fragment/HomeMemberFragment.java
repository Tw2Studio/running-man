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

import com.github.glomadrian.loadingballs.BallView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tw2.myapplication.R;
import com.tw2.myapplication.adapter.MemberAdapter;
import com.tw2.myapplication.model.Guest;
import com.tw2.myapplication.model.Member;

import java.util.ArrayList;
import java.util.List;

public class HomeMemberFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private List<Member> list;
    private MemberAdapter adapter;
    private DatabaseReference mReference;
    private BallView ballView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mReference = FirebaseDatabase.getInstance().getReference();
        initView();
        initData();
        return view;

    }

    private void initData() {
        list = new ArrayList<>();
        list.clear();

        mReference.child("member").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Member member = dataSnapshot.getValue(Member.class);
                list.add(member);
                adapter = new MemberAdapter(list, getContext());
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                ballView.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Member member = dataSnapshot.getValue(Member.class);
                if (!list.contains(member)){
                    list.set((Integer.parseInt(dataSnapshot.getKey()) - 1), member);
                    adapter.notifyDataSetChanged();
                }
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_home);
        ballView = (BallView) view.findViewById(R.id.loading);
    }
}
