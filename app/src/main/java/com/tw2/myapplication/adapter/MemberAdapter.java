package com.tw2.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tw2.myapplication.model.Member;
import com.tw2.myapplication.R;
import com.tw2.myapplication.view.activity.DetailMemberActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private List<Member> data = new ArrayList<>();
    private Context context;

    public MemberAdapter(List<Member> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Member member = data.get(position);
        holder.tvName.setText(member.getName());
        holder.tvNote.setText(member.getNote());
        Picasso.get().load(member.getImage()).into(holder.imageView);
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
        TextView tvName;
        TextView tvNote;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.img_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_name_member);
            tvNote = (TextView) itemView.findViewById(R.id.tv_note_member);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_member);
        }
    }
}
