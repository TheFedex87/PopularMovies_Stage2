package com.udacity.popularmovies1.popularmoviesstage2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies1.popularmoviesstage2.model.Video;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by federico.creti on 22/02/2018.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {
    private List<Video> videos;
    private movieVideoClickListener videoClickListener;

    @Inject
    public VideosAdapter(@NonNull movieVideoClickListener onVideoClicked){
        this.videoClickListener = onVideoClicked;
    }

    public void swapVideos(List<Video> videos){
        this.videos = videos;
        notifyDataSetChanged();
    }

    public interface movieVideoClickListener {
        void onVideoItemClick(int clickedVideoId);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.recyclerview_video, parent, false);

        VideoViewHolder videoViewHolder = new VideoViewHolder(view);

        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.setVideoName(position);
    }

    @Override
    public int getItemCount() {
        if (videos == null) return 0;
        return videos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.video_name) TextView videoName;
        @BindView(R.id.video_separator) View videoSeparator;

        public VideoViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void setVideoName(int position){
            videoName.setText(videos.get(position).getName());

            if (position == videos.size() - 1)
                videoSeparator.setVisibility(View.INVISIBLE);
            else
                videoSeparator.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View view) {
            videoClickListener.onVideoItemClick(getAdapterPosition());
        }
    }
}
