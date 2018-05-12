package com.udacity.popularmovies1.popularmoviesstage2;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
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

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by federico.creti on 16/02/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private final String URL_BASE_MOVIE_BANNER = "http://image.tmdb.org/t/p/w185";
    private List<Movie> moviesList;
    private Context context;
    private Context parentActivity;

    private ListItemClickListener clickListener;

    @Inject
    public MoviesAdapter(Context context, ListItemClickListener listItemClickListener){
        //MyApp.app().appComponent().inject(this);
        this.context = context;
        this.clickListener = listItemClickListener;
    }

    public interface ListItemClickListener{
        void onListItemClick(int positionClicked, @Nullable ImageView imageView);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (parentActivity == null)
            parentActivity = context;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recyclerview_movie, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.setMovieImage(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //holder.poster.setTransitionName(moviesList.get(position).getTitle());
            //holder.optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) parentActivity, holder.poster, holder.poster.getTransitionName());

            ViewCompat.setTransitionName(holder.poster, moviesList.get(position).getTitle());
        }
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

        @BindView(R.id.movie_poster) ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
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
            clickListener.onListItemClick(getAdapterPosition(), poster);
        }
    }
}
