package com.aspirephile.laundro.bill;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspirephile.laundro.R;
import com.aspirephile.laundro.db.tables.Item;
import com.aspirephile.laundro.db.tables.OfferedItemType;
import com.aspirephile.laundro.review.ReviewListFragment;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link OfferedItemType} and makes a call to the
 * specified {@link ReviewListFragment.OnListFragmentInteractionListener}.
 */
class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private final List<Item> mValues;
    private final BillViewerFragment.OnItemFragmentInteractionListener mListener;


    ItemRecyclerViewAdapter(List<Item> items, BillViewerFragment.OnItemFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_item, parent, false);        //CHANGED! other than other parts
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nameView.setText(mValues.get(position).itemTypeName);
        holder.costView.setText(String.valueOf(mValues.get(position).cost));
        holder.countView.setText(String.valueOf(mValues.get(position).count));
        holder.sumView.setText(String.valueOf(mValues.get(position).cost * mValues.get(position).count));

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
        final TextView nameView;
        final TextView countView;
        final TextView costView;
        Item mItem;
        private TextView sumView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            nameView = (TextView) view.findViewById(R.id.tv_item_name);
            costView = (TextView) view.findViewById(R.id.tv_item_cost);             //What to do?
            countView = (TextView) view.findViewById(R.id.tv_item_count);
            sumView = (TextView) view.findViewById(R.id.tv_item_sum);
        }

    }
}
