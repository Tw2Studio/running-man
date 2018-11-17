package com.tw2.myapplication.adapter;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tw2.myapplication.model.Guest;
import com.tw2.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.ViewHolder> {
    private List<Guest> data = new ArrayList<>();
    private Context context;

    public GuestAdapter(List<Guest> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_guest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Guest guest = data.get(position);
        holder.tvEp.setText(context.getResources().getString(R.string.txt_ep) +" "+ guest.getEp());
        holder.tvName.setText(guest.getName());
        Picasso.get().load(guest.getImage()).into(holder.imageView);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("android.intent.action.MAIN");
                    intent.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    intent.addCategory("android.intent.category.LAUNCHER");
                    intent.setData(Uri.parse(guest.getLink()));
                    context.startActivity(intent);
                } catch(ActivityNotFoundException e) {
                    // Chrome is not installed
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(guest.getLink()));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName;
        TextView tvEp;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_guest);
            tvName = (TextView) itemView.findViewById(R.id.tv_guest);
            tvEp = (TextView) itemView.findViewById(R.id.tv_ep);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_guest);
        }
    }
}
