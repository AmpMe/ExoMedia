package com.devbrackets.android.exomediademo.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.devbrackets.android.exomediademo.R;
import com.devbrackets.android.exomediademo.data.Samples;
import com.devbrackets.android.recyclerext.adapter.RecyclerListAdapter;

import java.util.LinkedList;
import java.util.List;

public class RecyclerVideoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_video);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        setupRecyclerView();
    }

    @Override
    protected void onDestroy() {
        recyclerView.setAdapter(null);
        super.onDestroy();
    }

    private void setupRecyclerView() {
        VideoAdapter adapter = new VideoAdapter();

        //Done a few times to get a larger list of items
        adapter.addAll(Samples.getVideoSamples());
        adapter.addAll(Samples.getVideoSamples());
        adapter.addAll(Samples.getVideoSamples());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private static class VideoAdapter extends RecyclerListAdapter<VideoViewHolder, Samples.Sample> {

        private List<VideoViewHolder> createdViewHolders = new LinkedList<>();

        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            VideoViewHolder holder = VideoViewHolder.newInstance(parent);
            createdViewHolders.add(holder);
            return holder;
        }

        @Override
        @SuppressWarnings("ConstantConditions") //getItem() won't return null here
        public void onBindViewHolder(VideoViewHolder holder, int position) {
            holder.setVideoUri(getItem(position).getMediaUrl());
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);

            //Releases all the EMVideoViews resources
            for (VideoViewHolder holder : createdViewHolders) {
                holder.release();
            }
        }
    }

    private static class VideoViewHolder extends RecyclerView.ViewHolder {
        private EMVideoView videoView;

        public static VideoViewHolder newInstance(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video, parent, false);
            return new VideoViewHolder(view);
        }

        public VideoViewHolder(View itemView) {
            super(itemView);

            //NOTE: ideally you won't load any part of the video until the user specifies they want to play the video,
            // instead you should display an image to represent the video then swap it out for the VideoView when the user
            // clicks on it
            videoView = (EMVideoView)itemView.findViewById(R.id.videoView);
            videoView.setReleaseOnDetachFromWindow(false);
        }

        public void release() {
            videoView.release();
        }

        public void setVideoUri(String uri) {
            videoView.setVideoURI(Uri.parse(uri));
        }
    }
}
