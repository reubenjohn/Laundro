package com.aspirephile.laundro.bill;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspirephile.laundro.R;
import com.aspirephile.laundro.bill.BillListFragment.OnListFragmentInteractionListener;
import com.aspirephile.laundro.db.tables.Bill;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

class BillRecyclerViewAdapter extends RecyclerView.Adapter<BillRecyclerViewAdapter.ViewHolder> {

    private final List<Bill> mValues;
    private final OnListFragmentInteractionListener mListener;

    BillRecyclerViewAdapter(List<Bill> items, OnListFragmentInteractionListener listener) {
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
        holder.nameView.setText(holder.mItem.service.name);
        Date createdAt = new Date(holder.mItem.issuedAt);
        holder.createdAtView.setText(DateFormat.getDateInstance().format(createdAt));
        String status;
        if (holder.mItem.payedAt == -2) {
            status = "SERVICING";
            holder.locationView.setTextColor(Color.BLUE);
        } else if (holder.mItem.payedAt == -1) {
            status = "READY";
            holder.locationView.setTextColor(Color.RED);
        } else if (holder.mItem.payedAt > 0) {
            status = "PAYED";
            holder.locationView.setTextColor(Color.GREEN);
        } else {
            status = "UNKNOWN";
        }
        holder.locationView.setText(status);
        //holder.ratingView.setProgress(mValues.get(position).rating);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListItemSelected(holder.mItem);
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
        final TextView createdAtView;
        final TextView nameView;
        final TextView locationView;
        final ProgressBar ratingView;
        Bill mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            createdAtView = (TextView) view.findViewById(R.id.tv_item_service_created_at);
            nameView = (TextView) view.findViewById(R.id.tv_item_service_name);
            locationView = (TextView) view.findViewById(R.id.tv_item_service_location);
            ratingView = (ProgressBar) view.findViewById(R.id.pm_item_service_rating);
        }

    }
}
