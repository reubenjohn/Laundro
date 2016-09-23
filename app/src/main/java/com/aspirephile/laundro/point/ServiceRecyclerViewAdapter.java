package com.aspirephile.laundro.point;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspirephile.laundro.R;
import com.aspirephile.laundro.db.tables.Service;
import com.aspirephile.laundro.point.ServiceListFragment.OnListFragmentInteractionListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PointListItem.PointItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ServiceRecyclerViewAdapter extends RecyclerView.Adapter<ServiceRecyclerViewAdapter.ViewHolder> {

    private final List<Service> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ServiceRecyclerViewAdapter(List<Service> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nameView.setText(holder.mItem.name);
        Date createdAt = new Date(holder.mItem.createdAt);
        holder.createdAtView.setText(DateFormat.getDateInstance().format(createdAt));
        holder.locationView.setText(holder.mItem.location);
        //holder.ratingView.setProgress(mValues.get(position).rating);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPointListItemSelected(holder.mItem);
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
        public final TextView createdAtView;
        public final TextView nameView;
        public final TextView locationView;
        public final ProgressBar ratingView;
        //public final TextView tag1View, tag2View, tag3View, tag4View;
        public Service mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            createdAtView = (TextView) view.findViewById(R.id.tv_item_service_created_at);
            nameView = (TextView) view.findViewById(R.id.tv_item_service_name);
            locationView = (TextView) view.findViewById(R.id.tv_item_service_location);
            ratingView = (ProgressBar) view.findViewById(R.id.pm_item_service_rating);
        }

    }
}