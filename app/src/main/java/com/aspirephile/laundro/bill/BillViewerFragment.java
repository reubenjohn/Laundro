package com.aspirephile.laundro.bill;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspirephile.laundro.Constants;
import com.aspirephile.laundro.R;
import com.aspirephile.laundro.db.OnQueryCompleteListener;
import com.aspirephile.laundro.db.tables.Bill;
import com.aspirephile.laundro.db.tables.Item;
import com.aspirephile.laundro.review.ReviewListActivity;
import com.aspirephile.laundro.service.ServiceViewerActivity;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

import java.util.List;

import static com.aspirephile.laundro.db.LaundroDb.getBillManager;
import static com.aspirephile.laundro.db.LaundroDb.getItemManager;

public class BillViewerFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener fragmentInteractionListener;
    private Logger l = new Logger(BillViewerFragment.class);
    private NullPointerAsserter asserter = new NullPointerAsserter(l);

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton editFab;
    private long _id;
    private long userId;
    private Bill bill;
    private View serviceView;
    private TextView serviceNameView;
    private List<Item> items;
    private RecyclerView itemsView;
    private OnItemFragmentInteractionListener mListener;
    private TextView sumTotalView;

    public BillViewerFragment() {
        l.onConstructor();
    }

    @Override
    public void onAttach(Context context) {
        l.onAttach();
        super.onAttach(context);

        if (context instanceof OnItemFragmentInteractionListener) {
            mListener = (OnItemFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement listener");
        }

        if (context instanceof OnFragmentInteractionListener) {
            fragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement listener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        l.onCreate();
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        SharedPreferences sp = getActivity().getSharedPreferences(Constants.files.authentication, Activity.MODE_PRIVATE);
        userId = sp.getLong(Constants.preferences.userId, -1);
        if (userId == -1) {
            fragmentInteractionListener.onFetchFailed(new SQLException("userId not found in shared preferences"));
        }

        getBillManager().getBill(_id).queryInBackground(new OnQueryCompleteListener() {
            @Override
            public void onQueryComplete(Cursor c, SQLException e) {
                if (e != null) {
                    fragmentInteractionListener.onFetchFailed(e);
                } else {
                    bill = getBillManager().getBillFromResult(c);
                    getItemManager().getItemQuery(_id).queryInBackground(new OnQueryCompleteListener() {
                        @Override
                        public void onQueryComplete(Cursor c, SQLException e) {
                            if (e != null) {
                                fragmentInteractionListener.onFetchFailed(e);
                            } else {
                                items = getItemManager().getItemListFromCursor(c);
                                updateViews(bill, items);
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        l.onCreateView();
        View v = inflater.inflate(R.layout.fragment_bill_viewer, container, false);

        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.cl_point_viewer);
        collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.ctl_point_viewer);

        editFab = (FloatingActionButton) v.findViewById(R.id.fab_point_viewer_edit);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.feature_not_available, Snackbar.LENGTH_LONG).show();
            }
        });

        serviceView = v.findViewById(R.id.cv_contact);
        serviceView.setOnClickListener(this);
        serviceNameView = (TextView) v.findViewById(R.id.tv_phone);

        itemsView = (RecyclerView) v.findViewById(R.id.cost_chart);
        Context context = v.getContext();
        itemsView.setLayoutManager(new LinearLayoutManager(context));

        sumTotalView = (TextView) v.findViewById(R.id.tv_sum_total);

        if (bill != null && items != null)
            updateViews(bill, items);
        return v;
    }

    @Override
    public void onStart() {
        l.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        l.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        l.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        l.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        l.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        l.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        l.onDetach();
        super.onDetach();
        fragmentInteractionListener = null;
    }

    private void updateViews(@NonNull final Bill bill, @NonNull List<Item> items) {
        collapsingToolbarLayout.setTitle(bill.service.name);
        serviceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ServiceViewerActivity.class);
                intent.putExtra(Constants.extras._id, bill.service._id);
                startActivity(intent);
            }
        });
        serviceNameView.setText(bill.service.name);

        itemsView.setAdapter(new ItemRecyclerViewAdapter(items, mListener));

        float sumTotal = 0.0f;
        for (Item item : items) {
            sumTotal += item.cost * item.count;
        }
        sumTotalView.setText("Rs. " + sumTotal);
        //TODO Fill other fields here
    }

    private void editPoint() {
        //TODO Start activity to edit point
        Snackbar.make(coordinatorLayout, R.string.feature_not_available, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab_point_creator) {
            editPoint();
        } else if (id == R.id.b_point_viewer_reviews || id == R.id.cv_review || id == R.id.service_rating) {
            l.d("Opening reviews for id: " + _id);
            openReviews(_id);
        } else {
            l.w("Unhandled view clicked with ID: " + v.getId());
        }
    }

    private void openReviews(long serviceId) {
        Intent i = new Intent(getActivity(), ReviewListActivity.class);
        i.putExtra(Constants.extras._id, serviceId);
        startActivity(i);
    }

    void setID(long ID) {
        this._id = ID;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFetchFailed(SQLException e);
    }

    public interface OnItemFragmentInteractionListener {
        void onListItemSelected(Item mItem);
    }
}
