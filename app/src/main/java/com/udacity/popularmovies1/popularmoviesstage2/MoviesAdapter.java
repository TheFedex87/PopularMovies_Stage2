package com.udacity.popularmovies1.popularmoviesstage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies1.popularmoviesstage2.dagger.ApplicationModule;
import com.udacity.popularmovies1.popularmoviesstage2.dagger.DaggerRetrofitApiInterfaceComponent;
import com.udacity.popularmovies1.popularmoviesstage2.model.Movie;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by federico.creti on 16/02/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private final String URL_BASE_MOVIE_BANNER = "http://image.tmdb.org/t/p/w185";
    private List<Movie> moviesList;
    @Inject Context context;

    private ListItemClickListener clickListener;

    public MoviesAdapter( ListItemClickListener clickListener){
        //this.context = context;
        this.clickListener = clickListener;
    }

    public interface ListItemClickListener{
        void onListItemClick(int positionClicked);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recyclerview_movie, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.setMovieImage(position);
    }

    @Override
    public int getItemCount() {
        if (moviesList == null) return 0;
        return moviesList.size();
    }

    public void swapMoviesList(List<Movie> moviesList){
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends ViewHolder implements View.OnClickListener{

        private ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.movie_poster);
            poster.setAdjustViewBounds(true);
            poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
            poster.setOnClickListener(this);
        }

        public void setMovieImage(int position){
            String imageUrl = URL_BASE_MOVIE_BANNER + moviesList.get(position).getBackdropPath();
            //Picasso.with(context).load(imageUrl).into(poster);
            DaggerRetrofitApiInterfaceComponent.builder().applicationModule(new ApplicationModule(context)).build().getPicasso().load(imageUrl).into(poster);
        }

        @Override
        public void onClick(View view) {
            clickListener.onListItemClick(getAdapterPosition());
        }
    }
}
