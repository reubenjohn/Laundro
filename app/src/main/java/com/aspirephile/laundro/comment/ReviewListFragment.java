package com.aspirephile.laundro.comment;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspirephile.laundro.R;
import com.aspirephile.laundro.db.LaundroDb;
import com.aspirephile.laundro.db.OnQueryCompleteListener;
import com.aspirephile.laundro.db.tables.Review;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ReviewListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_SERVICE_ID = "serviceId";
    private static final String ARG_USER_ID = "userId";
    private Logger l = new Logger(ReviewListFragment.class);
    private NullPointerAsserter asserter = new NullPointerAsserter(l);

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private long serviceId, userId;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReviewListFragment() {
        serviceId = -1;
        userId = -1;
    }

    @SuppressWarnings("unused")
    public static ReviewListFragment newInstance(int columnCount, long serviceId, long userId) {
        ReviewListFragment fragment = new ReviewListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putLong(ARG_SERVICE_ID, serviceId);
        args.putLong(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        l.onCreate();
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            serviceId = getArguments().getLong(ARG_SERVICE_ID);
            userId = getArguments().getLong(ARG_USER_ID);
        }

        onRefresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_comment_list);
        swipeRefreshLayout.setOnRefreshListener(this);
        // Set the adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_comment_list);
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        l.d("onRefresh");
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }

        if (serviceId != -1 && userId != -1) {
            l.d("Fetching reviews for serviceId: " + serviceId + " and userId=" + userId);
            LaundroDb.getReviewManager().getAllReviews(serviceId, userId).queryInBackground(new OnQueryCompleteListener() {
                @Override
                public void onQueryComplete(Cursor c, SQLException e) {
                    if (e != null) {
                        mListener.onCommentListLoadFailed(e);
                    } else {
                        List<Review> list = LaundroDb.getReviewManager().getRowsFromResult(c);
                        l.i("Point query completed with " + list.size() + " results");
                        if (asserter.assertPointer(recyclerView))
                            recyclerView.setAdapter(new ReviewRecyclerViewAdapter(list, mListener));
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
    public interface OnListFragmentInteractionListener {
        void onCommentListItemSelected(Review item);

        void onCommentListLoadFailed(SQLException e);
    }
}
