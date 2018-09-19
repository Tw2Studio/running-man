package com.tw2.myapplication.view.activity;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tw2.myapplication.R;
import com.tw2.myapplication.adapter.HomeAdapter;


public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private HomeAdapter adapter;
    private TabLayout tabLayout;
    private AdView banner;

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
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2328589623882503~5777206290");
        banner = (AdView) findViewById(R.id.banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);
    }

    private void initPager() {
        FragmentManager manager = getSupportFragmentManager();
        adapter = new HomeAdapter(manager);
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
                        tabLayout.setBackgroundResource(R.drawable.tab3);
                        break;
                }
            }
        });
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.home_tab_layout);

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
