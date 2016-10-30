package com.aspirephile.laundro.review;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspirephile.laundro.R;
import com.aspirephile.laundro.db.tables.Review;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Review} and makes a call to the
 * specified {@link ReviewListFragment.OnListFragmentInteractionListener}.
 */
public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {

    private final List<Review> mValues;
    private final ReviewListFragment.OnListFragmentInteractionListener mListener;

    SimpleDateFormat format;

    public ReviewRecyclerViewAdapter(List<Review> items, ReviewListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        format = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_review, parent, false);        //CHANGED! other than other parts
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.descView.setText(mValues.get(position).description);
        holder.timestampView.setText(String.valueOf(format.format(new Date(mValues.get(position).timestamp))));
        holder.ratingView.setText(String.valueOf(mValues.get(position).rating));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCommentListItemSelected(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView descView;
        public final TextView timestampView;
        public final TextView ratingView;
        public Review mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            descView = (TextView) view.findViewById(R.id.tv_item_comment_description);
            timestampView = (TextView) view.findViewById(R.id.tv_item_comment_timestamp);
            ratingView = (TextView) view.findViewById(R.id.tv_item_review_rating);             //What to do?
        }

    }
}
