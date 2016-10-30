package com.aspirephile.laundro.point;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aspirephile.laundro.Constants;
import com.aspirephile.laundro.R;
import com.aspirephile.laundro.comment.ReviewListActivity;
import com.aspirephile.laundro.db.LaundroDb;
import com.aspirephile.laundro.db.OnQueryCompleteListener;
import com.aspirephile.laundro.db.tables.Review;
import com.aspirephile.laundro.db.tables.Service;
import com.aspirephile.laundro.db.tables.User;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

public class ServiceViewerFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener fragmentInteractionListener;
    private Logger l = new Logger(ServiceViewerFragment.class);
    private NullPointerAsserter asserter = new NullPointerAsserter(l);

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton editFab;
    private TextView descriptionView;
    private long _id;
    private String username;
    private Service service;
    private Button reviewB;
    private Review review;
    private RatingBar ratingBar;
    private TextView phoneView;

    public ServiceViewerFragment() {
        l.onConstructor();
    }

    @Override
    public void onAttach(Context context) {
        l.onAttach();
        super.onAttach(context);
        try {
            fragmentInteractionListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        l.onCreate();
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        SharedPreferences sp = getActivity().getSharedPreferences(Constants.files.authentication, Activity.MODE_PRIVATE);
        username = sp.getString(Constants.preferences.username, null);

        LaundroDb.getServiceManager().getService(_id).queryInBackground(new OnQueryCompleteListener() {
            @Override
            public void onQueryComplete(Cursor c, android.database.SQLException e) {
                if (e != null) {
                    fragmentInteractionListener.onFetchFailed(e);
                } else {
                    final Service service = LaundroDb.getServiceManager().getServiceFromResult(c);
                    LaundroDb.getUserManager().getUser(username).queryInBackground(new OnQueryCompleteListener() {
                        @Override
                        public void onQueryComplete(Cursor c, SQLException e) {
                            if (e != null) {
                                fragmentInteractionListener.onFetchFailed(e);
                            } else {
                                User user = LaundroDb.getUserManager().getUserFromResult(c);
                                LaundroDb.getReviewManager().getAllReviews(service._id, user._id).queryInBackground(new OnQueryCompleteListener() {
                                    @Override
                                    public void onQueryComplete(Cursor c, android.database.SQLException e) {
                                        if (e != null) {
                                            fragmentInteractionListener.onFetchFailed(e);
                                        } else {
                                            Review review = LaundroDb.getReviewManager().getRowFromResult(c);
                                            updateViews(service, review);
                                        }
                                    }
                                });
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
        View v = inflater.inflate(R.layout.fragment_point_viewer, container, false);
        if (asserter.assertPointer(v))
            bridgeXML(v);
        initializeFields();
        if (service != null && review != null)
            updateViews(service, review);
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

    private void bridgeXML(View v) {
        l.bridgeXML();

        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.cl_point_viewer);
        collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.ctl_point_viewer);

        descriptionView = (TextView) v.findViewById(R.id.tv_point_viewer_description);

        editFab = (FloatingActionButton) v.findViewById(R.id.fab_point_viewer_edit);

        reviewB = (Button) v.findViewById(R.id.b_point_viewer_reviews);

        (v.findViewById(R.id.cv_review)).setOnClickListener(this);

        ratingBar = (RatingBar) v.findViewById(R.id.service_rating);

        phoneView = (TextView) v.findViewById(R.id.tv_phone);

        l.bridgeXML(asserter.assertPointer(coordinatorLayout, collapsingToolbarLayout, descriptionView, editFab, ratingBar, phoneView));
    }

    private void initializeFields() {
        l.initializeFields();

        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.feature_not_available, Snackbar.LENGTH_LONG).show();
            }
        });

        reviewB.setOnClickListener(this);
        ratingBar.setOnClickListener(this);
    }

    private void updateViews(@NonNull Service service, @NonNull Review review) {
        this.service = service;
        this.review = review;
        collapsingToolbarLayout.setTitle(service.name);
        ratingBar.setRating(review.rating);
        phoneView.setText(service.phone);
        descriptionView.setText(service.description);
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
            Intent i = new Intent(getActivity(), ReviewListActivity.class);
            i.putExtra(Constants.extras._id, _id);
            startActivity(i);
        } else {
            l.w("Unhandled view clicked with ID: " + v.getId());
        }
    }

    void setID(int ID) {
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
        void onFetchFailed(android.database.SQLException e);

        void onPointNotFound();
    }
}
