package com.tw2.myapplication.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.glomadrian.loadingballs.BallView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
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
import com.tw2.myapplication.view.dialog.CustomDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class DetailMemberActivity extends AppCompatActivity implements View.OnClickListener, RewardedVideoAdListener {
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
    private InterstitialAd mInterstitialAd;
    private ImageView btnGift;
    private Member member;
    private List<Integer> listGift;
    private RewardedVideoAd mRewardedVideoAd;
    private long timeStart, timeEnd;
    private String checkTime;


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
        initAds();
        initVideoAds();
        requestAds();
        initView();
        initData();
    }

    private void initVideoAds() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        mRewardedVideoAd.setImmersiveMode(true);
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(this.getResources().getString(R.string.ads_video),
                new AdRequest.Builder().build());
    }

    private void initAds() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(this.getResources().getString(R.string.banner_full));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

    }

    private void requestAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2328589623882503~5777206290");
        banner = (AdView) findViewById(R.id.banner);
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
        btnGift = (ImageView) findViewById(R.id.btn_gift);

        listGift = new ArrayList<>();
        listGift.add(R.drawable.ic_gift_blue);
        listGift.add(R.drawable.ic_gift_red);
        listGift.add(R.drawable.ic_gift_yellow);
        listGift.add(R.drawable.ic_gift_green);

        Random random = new Random();

        btnGift.setImageResource(listGift.get(random.nextInt(listGift.size())));

        shineButton.init(this);
        Calendar calendar = Calendar.getInstance();
        String time = calendar.get(Calendar.DATE)+"-"+calendar.get(Calendar.MONTH);
        checkTime = sharedPreferences.getString("time_"+name,"");
        if (time.equals(checkTime)){
            isVote = sharedPreferences.getBoolean(name, false);
        } else {
            upDateVote();
        }
        shineButton.setChecked(isVote);
        shineButton.setOnClickListener(this);
        btnGift.setOnClickListener(this);
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
                    DetailMemberActivity.this.member = member;
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
                Calendar calendar = Calendar.getInstance();
                isVote = !isVote;
                edit.putBoolean(name, isVote);
                edit.putString("time_"+name, calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.MONTH));
                edit.commit();
                if (isVote){
                    nbLove++;
                } else {
                    nbLove--;
                }

                mReference.child("member").child(keyFirebase).child("love").setValue(nbLove+"");

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }

                break;

            case R.id.btn_gift:
                CustomDialog dialog = new CustomDialog(DetailMemberActivity.this, member.getName(), member.getImage());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();
                break;
        }
    }

    public void showAd(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void upDateVote(){
        isVote=false;
        edit.putBoolean(name, false);
        edit.commit();
        shineButton.setChecked(false);
        Random random = new Random();
        btnGift.setImageResource(listGift.get(random.nextInt(listGift.size())));
    }

    public void showVideoAds(){
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {
    }

    @Override
    public void onRewardedVideoStarted() {
        Calendar calendar = Calendar.getInstance();
        timeStart = calendar.getTimeInMillis();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Calendar calendar = Calendar.getInstance();
        timeEnd = calendar.getTimeInMillis();

        if (timeEnd-timeStart>7000){
            upDateVote();
        }

        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        upDateVote();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
    }

    @Override
    public void onRewardedVideoCompleted() {
    }

    @Override
    public void onResume() {
        if (banner != null) {
            banner.resume();
        }
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (banner != null) {
            banner.pause();
        }
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (banner != null) {
            banner.destroy();
        }
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

}
