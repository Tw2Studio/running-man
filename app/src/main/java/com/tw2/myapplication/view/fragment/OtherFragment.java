package com.tw2.myapplication.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tw2.myapplication.R;
import com.tw2.myapplication.view.activity.AppOtherActivity;
import com.tw2.myapplication.view.dialog.CongDongDialog;

public class OtherFragment extends Fragment implements View.OnClickListener {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_other, container, false);
        initView();
        return view;
    }

    private void initView() {
        view.findViewById(R.id.btn_cong_dong).setOnClickListener(this);
        view.findViewById(R.id.btn_danh_gia).setOnClickListener(this);
        view.findViewById(R.id.btn_phan_hoi).setOnClickListener(this);
        view.findViewById(R.id.btn_ung_dung_khac).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cong_dong:
                CongDongDialog dialog = new CongDongDialog(getActivity());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.show();
                break;
            case R.id.btn_danh_gia:
                rateStore();
                break;
            case R.id.btn_phan_hoi:
                sendMail();
                break;
            case R.id.btn_ung_dung_khac:
                Intent intent = new Intent(getContext(), AppOtherActivity.class);
                getContext().startActivity(intent);
                break;
        }
    }

    public void rateStore(){
        Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
        }
    }

    public void  sendMail(){
        Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
        intentEmail.setType("text/plain");
        intentEmail.setData(Uri.parse("mailto:" + "gamecover96@gmail.com"));
        intentEmail.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Phản hồi, góp ý app RM");
        intentEmail.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
        startActivity(Intent.createChooser(intentEmail, "Send Email"));
    }
}
