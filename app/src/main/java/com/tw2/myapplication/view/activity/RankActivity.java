package com.tw2.myapplication.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tw2.myapplication.R;
import com.tw2.myapplication.adapter.MemberAdapter;
import com.tw2.myapplication.adapter.RankAdapter;
import com.tw2.myapplication.model.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<Member> list;
    private RankAdapter adapter;
    private DatabaseReference mReference;
    private AdView banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        mReference = FirebaseDatabase.getInstance().getReference();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar));
        }
        requestAds();
        initView();
        initData();
    }

    private void requestAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2328589623882503~5777206290");
        banner = (AdView) findViewById(R.id.banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);


    }

    private void initData() {
        list = new ArrayList<>();
        list.clear();
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }

        mReference.child("member").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (Integer.parseInt(dataSnapshot.getKey()) < 9) {
                    Member member = dataSnapshot.getValue(Member.class);
                    list.add(member);
                }

                if (list.size() == 8) {
                    Collections.sort(list, new Comparator<Member>() {
                        @Override
                        public int compare(final Member object1, final Member object2) {
                            return Integer.parseInt(object2.getLove()) - Integer.parseInt(object1.getLove());
                        }
                    });

                    adapter = new RankAdapter(list, RankActivity.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(RankActivity.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    //ballView.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (Integer.parseInt(dataSnapshot.getKey()) < 9) {
                    Member member = dataSnapshot.getValue(Member.class);
                    for (int i=0;i<list.size();i++){
                        if (list.get(i).getName().equals(member.getName())){
                            list.set(i, member);
                            Collections.sort(list, new Comparator<Member>() {
                                @Override
                                public int compare(final Member object1, final Member object2) {
                                    return Integer.parseInt(object2.getLove()) - Integer.parseInt(object1.getLove());
                                }
                            });
                            adapter.notifyDataSetChanged();
                        }
                    }
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
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycle_rank);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RankActivity.this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        findViewById(R.id.btn_back_rank).setOnClickListener(this);
        findViewById(R.id.btn_help).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_rank:
                onBackPressed();
                break;
            case R.id.btn_help:
                Intent intent = new Intent(RankActivity.this, HelpActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onPause() {
        if (banner != null) {
            banner.pause();
        }
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (banner != null) {
            banner.resume();
        }
    }
    @Override
    public void onDestroy() {
        if (banner != null) {
            banner.destroy();
        }
        super.onDestroy();
    }
}
