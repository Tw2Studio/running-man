package com.tw2.myapplication.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tw2.myapplication.R;
import com.tw2.myapplication.view.fragment.GifFragment;
import com.tw2.myapplication.view.fragment.GuestFragment;
import com.tw2.myapplication.view.fragment.HomeMemberFragment;
import com.tw2.myapplication.view.fragment.VideoFragment;

public class HomeAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public HomeAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context =context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = new HomeMemberFragment();
                break;
            case 1:
                frag = new GifFragment();
                break;
            case 2:
                frag = new GuestFragment();
                break;
            case 3:
                frag = new VideoFragment();
                break;


        }
        return frag;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = context.getResources().getString(R.string.txt_thanhvien);
                break;
            case 1:
                title = context.getResources().getString(R.string.txt_gif_hay);
                break;
            case 2:
                title = context.getResources().getString(R.string.txt_khachmoi);
                break;
            case 3:
                title = context.getResources().getString(R.string.txt_video);
                break;

        }

        return title;
    }
}
