package com.aspirephile.laundro.service;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspirephile.laundro.R;
import com.aspirephile.laundro.db.tables.OfferedItemType;
import com.aspirephile.laundro.review.ReviewListFragment;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link OfferedItemType} and makes a call to the
 * specified {@link ReviewListFragment.OnListFragmentInteractionListener}.
 */
class OfferedItemTypeRecyclerViewAdapter extends RecyclerView.Adapter<OfferedItemTypeRecyclerViewAdapter.ViewHolder> {

    private final List<OfferedItemType> mValues;
    private final ServiceViewerFragment.OnOfferedItemTypeFragmentInteractionListener mListener;


    OfferedItemTypeRecyclerViewAdapter(List<OfferedItemType> items, ServiceViewerFragment.OnOfferedItemTypeFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_offered_item_type, parent, false);        //CHANGED! other than other parts
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.descView.setText(mValues.get(position).itemTypeName);
        holder.ratingView.setText("Rs. " + String.valueOf(mValues.get(position).cost));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onOfferedItemTypeListItemSelected(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView descView;
        final TextView timestampView;
        final TextView ratingView;
        OfferedItemType mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            descView = (TextView) view.findViewById(R.id.tv_item_comment_description);
            timestampView = (TextView) view.findViewById(R.id.tv_item_comment_timestamp);
            ratingView = (TextView) view.findViewById(R.id.tv_item_review_rating);             //What to do?
        }

    }
}
