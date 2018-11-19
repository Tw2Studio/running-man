package com.tw2.myapplication.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tw2.myapplication.R;
import com.tw2.myapplication.view.activity.DetailMemberActivity;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class CongDongDialog extends Dialog implements View.OnClickListener {
    private Activity activity;

    public CongDongDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_congdong);
        initDialog();
    }

    private void initDialog() {
        findViewById(R.id.btn_dialog_ok).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dialog_ok:
                dismiss();
                break;
        }
    }
}
