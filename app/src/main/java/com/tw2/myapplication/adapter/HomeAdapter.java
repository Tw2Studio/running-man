package com.tw2.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tw2.myapplication.view.fragment.GuestFragment;
import com.tw2.myapplication.view.fragment.HomeMemberFragment;
import com.tw2.myapplication.view.fragment.VideoFragment;

public class HomeAdapter extends FragmentStatePagerAdapter {

    public HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = new HomeMemberFragment();
                break;
            case 1:
                frag = new GuestFragment();
                break;
            case 2:
                frag = new VideoFragment();
                break;

        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Thành viên";
                break;
            case 1:
                title = "Khách mời";
                break;
            case 2:
                title = "Video";
                break;

        }

        return title;
    }
}
