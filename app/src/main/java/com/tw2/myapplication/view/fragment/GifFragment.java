package com.tw2.myapplication.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tw2.myapplication.R;
import com.tw2.myapplication.adapter.GifAdapter;

import java.util.ArrayList;
import java.util.List;

public class GifFragment extends Fragment {
    private View view;
    private List<String> list;
    private RecyclerView recyclerView;
    private GifAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gif, container, false);
        initView();
        initData();
        return view;
    }

    private void initData() {
        list = new ArrayList<>();

        list.add("http://mediaold.tiin.vn:8080/media_old_2016//archive/images/2016/12/18/112241_17.gif");
        list.add("https://media.giphy.com/media/jYwkNLtqUScHm/giphy.gif");
        list.add("https://media.giphy.com/media/NjOSCCItRX0d2/giphy.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");
        list.add("http://images6.fanpop.com/image/photos/34500000/Monday-couple-running-man-34584716-500-227.gif");

        adapter = new GifAdapter(list, getContext());
        recyclerView.setAdapter(adapter);

    }

    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_gif);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }
}
