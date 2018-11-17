package com.tw2.myapplication.view.activity;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.tw2.myapplication.R;
import com.tw2.myapplication.adapter.HomeAdapter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private HomeAdapter adapter;
    private TabLayout tabLayout;
    private PublisherAdView mPublisherAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar));
        }
        requestAds();
        initView();
        initPager();
    }

    private void requestAds() {
        mPublisherAdView = findViewById(R.id.publisherAdView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);
    }

    private void initPager() {
        FragmentManager manager = getSupportFragmentManager();
        adapter = new HomeAdapter(manager, this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tabLayout.setBackgroundResource(R.drawable.tab1);
                        break;
                    case 1:
                        tabLayout.setBackgroundResource(R.drawable.tab2);
                        break;
                    case 2:
                        tabLayout.setBackgroundResource(R.drawable.tab2);
                        break;
                    case 3:
                        tabLayout.setBackgroundResource(R.drawable.tab3);
                        break;

                }
            }
        });
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.home_tab_layout);

        findViewById(R.id.btn_rank).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rank:
                Intent intent = new Intent(MainActivity.this, RankActivity.class);
                startActivity(intent);
        }
    }
}
