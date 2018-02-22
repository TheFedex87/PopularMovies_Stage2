package com.udacity.popularmovies1.popularmoviesstage2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies1.popularmoviesstage2.model.Video;

import java.util.List;

/**
 * Created by federico.creti on 22/02/2018.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {
    private List<Video> videos;

    public void swapVideos(List<Video> videos){
        this.videos = videos;
        notifyDataSetChanged();
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

    class VideoViewHolder extends RecyclerView.ViewHolder{

        private TextView videoName;

        public VideoViewHolder(View itemView) {
            super(itemView);

            videoName = itemView.findViewById(R.id.video_name);
        }

        public void setVideoName(int position){
            videoName.setText(videos.get(position).getName());
        }
    }
}
