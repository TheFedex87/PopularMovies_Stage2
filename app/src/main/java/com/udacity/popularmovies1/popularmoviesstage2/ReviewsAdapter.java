package com.udacity.popularmovies1.popularmoviesstage2;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies1.popularmoviesstage2.model.Review;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Federico on 25/02/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    private List<Review> reviews;

    @Inject
    public ReviewsAdapter(){

    }


    public void swapReviews(List<Review> reviews){
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_review, parent, false);

        ReviewsViewHolder viewHolder = new ReviewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        holder.setReview(position);
    }

    @Override
    public int getItemCount() {
        if (reviews == null) return 0;
        return reviews.size();
    }

    class ReviewsViewHolder extends ViewHolder implements View.OnClickListener {
        @BindView(R.id.review_author) TextView reviewAuthor;
        @BindView(R.id.review) TextView review;
        @BindView(R.id.review_separator) View reviewSeparator;

        private boolean isExpanded;

        @BindInt(R.integer.collapsed_review_max_lines) int compactedMaxLines;

        public ReviewsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setReview(int position){
            review.setText(reviews.get(position).getContent());
            reviewAuthor.setText(reviews.get(position).getAuthor());

            if (position == reviews.size() - 1)
                reviewSeparator.setVisibility(View.INVISIBLE);
            else
                reviewSeparator.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View view) {
            if(!isExpanded){
                review.setMaxLines(Integer.MAX_VALUE);
            } else {
                review.setMaxLines(compactedMaxLines);
            }
            isExpanded = !isExpanded;
        }
    }
}
