package com.tw2.myapplication.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.glomadrian.loadingballs.BallView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tw2.myapplication.R;
import com.tw2.myapplication.adapter.MemberAdapter;
import com.tw2.myapplication.adapter.VideoAdapter;
import com.tw2.myapplication.model.Member;
import com.tw2.myapplication.model.MyVideo;
import com.tw2.myapplication.model.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private List<MyVideo> list;
    private VideoAdapter adapter;
    private String ID_PLAYLIST = "PLYLXC-cAzgsnKy105XaSlnS_C7xPYFHKJ";
    private String URL_PLAYLIST = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="
            +ID_PLAYLIST+"&key="+ Utils.KEY_YOUTUBE;

    private BallView ballView;
    private DatabaseReference mReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        mReference = FirebaseDatabase.getInstance().getReference();
        initView();
        initData();
        //getJsonYouTube(URL_PLAYLIST);
        return view;

    }

    private void initData() {

        mReference.child("key").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ID_PLAYLIST = dataSnapshot.getValue(String.class);
                URL_PLAYLIST = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="
                        +ID_PLAYLIST+"&key="+ Utils.KEY_YOUTUBE;
                getJsonYouTube(URL_PLAYLIST);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ID_PLAYLIST = dataSnapshot.getValue(String.class);
                URL_PLAYLIST = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="
                        +ID_PLAYLIST+"&key="+ Utils.KEY_YOUTUBE;
                getJsonYouTube(URL_PLAYLIST);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getDataYouTube(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        list = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonItems = response.getJSONArray("items");
                            String name = "";
                            String image = "";
                            String idVideo = "";

                            for (int i=0; i<jsonItems.length();i++){
                                JSONObject jsonItem = jsonItems.getJSONObject(i);
                                JSONObject jsonSnippet  = jsonItem.getJSONObject("snippet");
                                JSONObject jsonThumbnail = jsonSnippet.getJSONObject("thumbnails");
                                JSONObject jsonMedium = jsonThumbnail.getJSONObject("medium");
                                name = jsonSnippet.getString("title");
                                image = jsonMedium.getString("url");

                                JSONObject jsonIdVideo = jsonSnippet.getJSONObject("resourceId");
                                idVideo = jsonIdVideo.getString("videoId");

                                MyVideo video = new MyVideo(name, image, idVideo);
                                list.add(video);
                            }

                            adapter = new VideoAdapter(list, getContext());
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                                    layoutManager.getOrientation());
                            recyclerView.addItemDecoration(dividerItemDecoration);
                            ballView.setVisibility(View.GONE);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void getJsonYouTube(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonMax = response.getJSONObject("pageInfo");
                            int maxItem = jsonMax.getInt("totalResults");
                            getDataYouTube(URL_PLAYLIST+"&maxResults="+maxItem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_video);
        ballView = (BallView) view.findViewById(R.id.loading);
    }
}
