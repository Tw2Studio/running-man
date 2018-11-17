package com.tw2.myapplication.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tw2.myapplication.R;
import com.tw2.myapplication.view.activity.DetailMemberActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomDialog extends Dialog implements View.OnClickListener {
    private Activity activity;
    private String name, image;
    private CircleImageView circleImageView;
    private TextView textView;

    public CustomDialog(Activity activity, String name, String image) {
        super(activity);
        this.activity = activity;
        this.name = name;
        this.image = image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gift);
        initDialog();
    }

    private void initDialog() {
        circleImageView = (CircleImageView) findViewById(R.id.img_dialog);
        textView = (TextView) findViewById(R.id.tv_dialog);

        Picasso.get().load(image).into(circleImageView);
        textView.setText("Xem quảng cáo để nhận thêm 1 lượt vote cho " + name);

        findViewById(R.id.btn_dialog_cancel).setOnClickListener(this);
        findViewById(R.id.btn_dialog_ok).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dialog_cancel:
                dismiss();
                ((DetailMemberActivity) activity).showAd();
                break;
            case R.id.btn_dialog_ok:
                ((DetailMemberActivity) activity).upDateVote();
                dismiss();
                break;
        }
    }
}
