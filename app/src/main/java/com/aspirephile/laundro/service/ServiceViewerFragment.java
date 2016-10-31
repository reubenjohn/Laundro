package com.aspirephile.laundro.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.aspirephile.laundro.Constants;
import com.aspirephile.laundro.R;
import com.aspirephile.laundro.db.OnQueryCompleteListener;
import com.aspirephile.laundro.db.tables.Location;
import com.aspirephile.laundro.db.tables.OfferedItemType;
import com.aspirephile.laundro.db.tables.Service;
import com.aspirephile.laundro.review.ReviewListActivity;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

import java.util.List;

import static com.aspirephile.laundro.db.LaundroDb.getLocationManager;
import static com.aspirephile.laundro.db.LaundroDb.getOfferedItemTypeManagerManager;
import static com.aspirephile.laundro.db.LaundroDb.getReviewManager;
import static com.aspirephile.laundro.db.LaundroDb.getServiceManager;

public class ServiceViewerFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener fragmentInteractionListener;
    private Logger l = new Logger(ServiceViewerFragment.class);
    private NullPointerAsserter asserter = new NullPointerAsserter(l);

    private CoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton editFab;
    private TextView descriptionView;
    private long _id;
    private long userId;
    private Service service;
    private float rating = 0.0f;
    private RatingBar ratingBar;
    private View contactView, locationView;
    private TextView phoneView;
    private TextView locationTextView;
    private Location location;
    private List<OfferedItemType> costChart;
    private RecyclerView costChartView;
    private OnOfferedItemTypeFragmentInteractionListener mListener;

    public ServiceViewerFragment() {
        l.onConstructor();
    }

    @Override
    public void onAttach(Context context) {
        l.onAttach();
        super.onAttach(context);

        if (context instanceof ServiceViewerFragment.OnOfferedItemTypeFragmentInteractionListener) {
            mListener = (ServiceViewerFragment.OnOfferedItemTypeFragmentInteractionListener) context;
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

        getServiceManager().getService(_id).queryInBackground(new OnQueryCompleteListener() {
            @Override
            public void onQueryComplete(Cursor c, android.database.SQLException e) {
                if (e != null) {
                    fragmentInteractionListener.onFetchFailed(e);
                } else {
                    service = getServiceManager().getServiceFromResult(c);
                    getLocationManager().getLocation(service.location._id).queryInBackground(new OnQueryCompleteListener() {
                        @Override
                        public void onQueryComplete(Cursor c, SQLException e) {
                            if (e != null) {
                                fragmentInteractionListener.onFetchFailed(e);
                            } else {
                                location = getLocationManager().getLocationFromCursor(c);
                                getReviewManager().getAverageReview(service._id).queryInBackground(new OnQueryCompleteListener() {
                                    @Override
                                    public void onQueryComplete(Cursor c, android.database.SQLException e) {
                                        if (e != null) {
                                            fragmentInteractionListener.onFetchFailed(e);
                                        } else {
                                            rating = getReviewManager().getAverageRatingFromCursor(c);
                                            getOfferedItemTypeManagerManager().getOfferedItemTypeQuery(_id).queryInBackground(new OnQueryCompleteListener() {
                                                @Override
                                                public void onQueryComplete(Cursor c, SQLException e) {
                                                    if (e != null) {
                                                        fragmentInteractionListener.onFetchFailed(e);
                                                    } else {
                                                        costChart = getOfferedItemTypeManagerManager().getOfferedItemTypeListFromCursor(c);
                                                        updateViews(service, location, rating, costChart);
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
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        l.onCreateView();
        View v = inflater.inflate(R.layout.fragment_service_viewer, container, false);

        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.cl_point_viewer);
        collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.ctl_point_viewer);

        descriptionView = (TextView) v.findViewById(R.id.tv_point_viewer_description);

        editFab = (FloatingActionButton) v.findViewById(R.id.fab_point_viewer_edit);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.feature_not_available, Snackbar.LENGTH_LONG).show();
            }
        });

        (v.findViewById(R.id.cv_review)).setOnClickListener(this);
        ratingBar = (RatingBar) v.findViewById(R.id.service_rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser)
                    openReviews(_id);
            }
        });

        contactView = v.findViewById(R.id.cv_contact);
        phoneView = (TextView) v.findViewById(R.id.tv_phone);
        locationView = v.findViewById(R.id.cv_location);
        locationTextView = (TextView) v.findViewById(R.id.tv_location);

        costChartView = (RecyclerView) v.findViewById(R.id.cost_chart);
        Context context = v.getContext();
        costChartView.setLayoutManager(new LinearLayoutManager(context));

        if (service != null && location != null && costChart != null)
            updateViews(service, location, rating, costChart);
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

    private void updateViews(@NonNull final Service service, @NonNull final Location location, float rating, @NonNull List<OfferedItemType> costChart) {
        collapsingToolbarLayout.setTitle(service.name);
        ratingBar.setRating(rating);
        phoneView.setText(service.phone);
        locationTextView.setText(location.name);
        descriptionView.setText(service.description);
        contactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + service.phone));
                startActivity(intent);
            }
        });
        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location.lon + "," + location.lat + "(" + Uri.encode(location.name) + ")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        costChartView.setAdapter(new OfferedItemTypeRecyclerViewAdapter(costChart, mListener));
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
        void onFetchFailed(android.database.SQLException e);
    }

    public interface OnOfferedItemTypeFragmentInteractionListener {
        void onOfferedItemTypeListItemSelected(OfferedItemType mItem);
    }
}
