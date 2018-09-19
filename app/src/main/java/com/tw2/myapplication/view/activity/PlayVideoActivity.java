package com.tw2.myapplication.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tw2.myapplication.R;
import com.tw2.myapplication.adapter.VideoAdapter;
import com.tw2.myapplication.model.MyVideo;
import com.tw2.myapplication.model.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private YouTubePlayerView youTubePlayerView;
    private int REQUEST_CODE = 123;
    private String idVideo;
    private RecyclerView recyclerView;
    private List<MyVideo> list;
    private VideoAdapter adapter;
    private String ID_PLAYLIST = "PLYLXC-cAzgsnKy105XaSlnS_C7xPYFHKJ";
    private String URL_PLAYLIST = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="
            + ID_PLAYLIST + "&key=" + Utils.KEY_YOUTUBE;

    private AdView banner;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_play_video);
        Intent intent = getIntent();
        idVideo = intent.getStringExtra("ID_VIDEO");
        requestAds();
        mReference = FirebaseDatabase.getInstance().getReference();
        initData();
        //getJsonYouTube(URL_PLAYLIST);
        initView();
    }

    public String getIdVideo() {
        return idVideo;
    }

    private void requestAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2328589623882503~5777206290");
        banner = (AdView) findViewById(R.id.banner_2);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);
    }

    private void initData() {

        mReference.child("key").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ID_PLAYLIST = dataSnapshot.getValue(String.class);
                URL_PLAYLIST = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="
                        + ID_PLAYLIST + "&key=" + Utils.KEY_YOUTUBE;
                getJsonYouTube(URL_PLAYLIST);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ID_PLAYLIST = dataSnapshot.getValue(String.class);
                URL_PLAYLIST = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="
                        + ID_PLAYLIST + "&key=" + Utils.KEY_YOUTUBE;
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

    private void getDataYouTube(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(PlayVideoActivity.this);
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

                            for (int i = 0; i < jsonItems.length(); i++) {
                                JSONObject jsonItem = jsonItems.getJSONObject(i);
                                JSONObject jsonSnippet = jsonItem.getJSONObject("snippet");
                                JSONObject jsonThumbnail = jsonSnippet.getJSONObject("thumbnails");
                                JSONObject jsonMedium = jsonThumbnail.getJSONObject("medium");
                                name = jsonSnippet.getString("title");
                                image = jsonMedium.getString("url");

                                JSONObject jsonIdVideo = jsonSnippet.getJSONObject("resourceId");
                                idVideo = jsonIdVideo.getString("videoId");

                                MyVideo video = new MyVideo(name, image, idVideo);
                                list.add(video);
                            }

                            List<MyVideo> myVideoList = new ArrayList<>();
                            int size = list.size() > 10 ? 10 : list.size();
                            for (int i = 0; i < size; i++) {
                                Random rd = new Random();
                                int index = rd.nextInt(size);
                                if (!myVideoList.contains(list.get(index))) {
                                    if (!list.get(index).getIdVideo().equals(getIdVideo())) {
                                        myVideoList.add(list.get(index));
                                    }
                                } else {
                                    i--;
                                }

                            }

                            adapter = new VideoAdapter(myVideoList, PlayVideoActivity.this);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(PlayVideoActivity.this);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(PlayVideoActivity.this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        requestQueue.add(jsonObjectRequest);
    }

    private void getJsonYouTube(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(PlayVideoActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonMax = response.getJSONObject("pageInfo");
                            int maxItem = jsonMax.getInt("totalResults");
                            getDataYouTube(URL_PLAYLIST + "&maxResults=" + maxItem);
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
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);

        youTubePlayerView.initialize(Utils.KEY_YOUTUBE, this);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_play_video);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.cueVideo(idVideo);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(PlayVideoActivity.this, REQUEST_CODE);
        } else {
            Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            youTubePlayerView.initialize(Utils.KEY_YOUTUBE, PlayVideoActivity.this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        if (banner != null) {
            banner.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (banner != null) {
            banner.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (banner != null) {
            banner.destroy();
        }
        super.onDestroy();
    }
}
