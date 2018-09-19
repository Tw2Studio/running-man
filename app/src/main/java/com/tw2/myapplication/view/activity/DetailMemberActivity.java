package com.tw2.myapplication.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.glomadrian.loadingballs.BallView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;
import com.tw2.myapplication.R;
import com.tw2.myapplication.adapter.MemberAdapter;
import com.tw2.myapplication.model.Member;

import java.util.ArrayList;
import java.util.List;

public class DetailMemberActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ShineButton shineButton;
    private SharedPreferences sharedPreferences;
    private boolean isVote;
    private SharedPreferences.Editor edit;
    private Intent intent;
    private String name;
    private DatabaseReference mReference;
    private ImageView imgMember;
    private TextView tvNbLove;
    private int nbLove;
    private String keyFirebase;
    private BallView ballView;
    private TextView tvInfo1, tvInfo2, tvInfo3, tvInfo4;
    private AdView banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_member);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar));
        }
        mReference = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();
        name = intent.getStringExtra("NAME");
        sharedPreferences = getSharedPreferences("my_data", MODE_PRIVATE);
        edit=sharedPreferences.edit();
        initToobar();
        requestAds();
        initView();
        initData();
    }

    private void requestAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2328589623882503~5777206290");
        banner = (AdView) findViewById(R.id.banner_3);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);
    }

    private void initView() {
        imgMember = (ImageView) findViewById(R.id.img_member);
        shineButton = (ShineButton) findViewById(R.id.shine_button);
        tvNbLove = (TextView) findViewById(R.id.tv_nb_love);
        ballView = (BallView) findViewById(R.id.loading);
        tvInfo1 = (TextView) findViewById(R.id.tv_detail_1);
        tvInfo2 = (TextView) findViewById(R.id.tv_detail_2);
        tvInfo3 = (TextView) findViewById(R.id.tv_detail_3);
        tvInfo4 = (TextView) findViewById(R.id.tv_detail_4);

        shineButton.init(this);
        isVote = sharedPreferences.getBoolean(name, false);
        shineButton.setChecked(isVote);
        shineButton.setOnClickListener(this);
    }

    private void initData() {
        mReference.child("member").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Member member = dataSnapshot.getValue(Member.class);
                if (name.equals(member.getName())){
                    Picasso.get().load(member.getThumbnail()).into(imgMember);
                    keyFirebase = dataSnapshot.getKey();
                    nbLove = Integer.parseInt(member.getLove());
                    tvNbLove.setText(member.getLove());
                    getInfo(dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Member member = dataSnapshot.getValue(Member.class);
                if (name.equals(member.getName())){
                    Picasso.get().load(member.getThumbnail()).into(imgMember);
                    nbLove = Integer.parseInt(member.getLove());
                    tvNbLove.setText(member.getLove());
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

    private void getInfo(String key) {
        final int index = 0;
        final List<TextView> list = new ArrayList<>();
        list.add(tvInfo1);
        list.add(tvInfo2);
        list.add(tvInfo3);
        list.add(tvInfo4);
        mReference.child("member").child(key).child("info").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String info = dataSnapshot.getValue(String.class);
                list.get(Integer.parseInt(dataSnapshot.getKey())-1).setText("   " + info);
                ballView.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String info = dataSnapshot.getValue(String.class);
                list.get(Integer.parseInt(dataSnapshot.getKey())-1).setText(info);
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

    private void initToobar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shine_button:
                isVote = !isVote;
                edit.putBoolean(name, isVote);
                edit.commit();
                if (isVote){
                    nbLove++;
                } else {
                    nbLove--;
                }

                mReference.child("member").child(keyFirebase).child("love").setValue(nbLove+"");

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
