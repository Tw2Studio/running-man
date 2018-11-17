package com.tw2.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;
import com.tw2.myapplication.R;
import com.tw2.myapplication.model.Member;
import com.tw2.myapplication.view.activity.DetailMemberActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private List<Member> data = new ArrayList<>();
    private Context context;
    private List<Integer> list = new ArrayList<>();

    public RankAdapter(List<Member> data, Context context) {
        this.data = data;
        this.context = context;
        list.add(R.drawable.rank_1);
        list.add(R.drawable.rank_2);
        list.add(R.drawable.rank_3);
        list.add(R.drawable.rank_4);
        list.add(R.drawable.rank_5);
        list.add(R.drawable.rank_6);
        list.add(R.drawable.rank_7);
        list.add(R.drawable.rank_8);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_rank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Member member = data.get(position);
        holder.tvrank.setText(member.getLove());
        Picasso.get().load(member.getImage()).into(holder.imageView);
        holder.imgRank.setBackgroundResource(list.get(position));
        holder.imgCount.setChecked(true);
        holder.imgCount.setEnabled(false);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMemberActivity.class);
                intent.putExtra("NAME", member.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView tvrank;
        ImageView imgRank;
        ShineButton imgCount;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.img_avatar_rank);
            tvrank = (TextView) itemView.findViewById(R.id.tv_count_rank);
            imgCount = (ShineButton) itemView.findViewById(R.id.img_count_rank);
            imgRank = (ImageView) itemView.findViewById(R.id.img_rank);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.container_rank);
        }
    }
}
