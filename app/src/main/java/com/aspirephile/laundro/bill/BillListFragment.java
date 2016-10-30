package com.aspirephile.laundro.bill;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.IntRange;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspirephile.laundro.R;
import com.aspirephile.laundro.db.LaundroDb;
import com.aspirephile.laundro.db.OnQueryCompleteListener;
import com.aspirephile.laundro.db.tables.Bill;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class BillListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PID = "_id";
    private static final String ARG_STANDING = "STANDING";
    private Logger l = new Logger(BillListFragment.class);
    private NullPointerAsserter asserter = new NullPointerAsserter(l);

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Bill> pointItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String PID;
    private char standing = '\0';

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BillListFragment() {
    }

    public static BillListFragment newInstance(int columnCount) {
        BillListFragment fragment = new BillListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static BillListFragment newInstance(int columnCount, @IntRange(from = 0) int PID, char standing) {
        BillListFragment fragment = new BillListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_PID, PID);
        args.putChar(ARG_STANDING, standing);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        l.onCreate();
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            PID = getArguments().getString(ARG_PID);
            standing = getArguments().getChar(ARG_STANDING);
        }

        onRefresh();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_point_list);
        swipeRefreshLayout.setOnRefreshListener(this);
        // Set the adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_point_list);
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new BillRecyclerViewAdapter(pointItems, mListener));

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
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);

        LaundroDb.getBillManager().getBillListQuery().queryInBackground(new OnQueryCompleteListener() {
            @Override
            public void onQueryComplete(Cursor c, SQLException e) {
                if (e != null) {
                    e.printStackTrace();
                    mListener.onListLoadFailed(e);
                } else {
                    List<Bill> list = LaundroDb.getBillManager().getBillListFromResult(c);
                    l.i("Point query completed with " + list.size() + " results");
                    if (asserter.assertPointer(recyclerView))
                        recyclerView.setAdapter(new BillRecyclerViewAdapter(list, mListener));
                    swipeRefreshLayout.setRefreshing(false);

                    scheduleNotifications(list);
                }
            }
        });
    }

    private void scheduleNotifications(List<Bill> list) {
        final Random r = new Random();
        for (final Bill bill : list) {
            if (bill.payedAt == -2) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showNotification(getActivity(), bill);
                    }
                }, 5000 + r.nextLong() % 10000);
            }
        }
    }

    public void showNotification(Context context, Bill bill) {//delay is after how much time(in millis) from current time you want to schedule the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(bill.service.name + " has just finished with your batch of clothes")
                .setContentText("You can clothes your clothes from their service center when they're open")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.my_logo)
                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.my_logo)).getBitmap())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, BillViewerActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, (int) bill._id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify((int) bill._id, builder.build());
    }

    public void scheduleNotification(Context context, long delay, Bill bill) {//delay is after how much time(in millis) from current time you want to schedule the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(bill.service.name + " has just finished with your batch of clothes")
                .setContentText("You can clothes your clothes from their service center when they're open")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.my_logo)
                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.my_logo)).getBitmap())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, BillViewerActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, (int) bill._id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, BillCompletionNotificationPublisher.class);
        notificationIntent.putExtra(BillCompletionNotificationPublisher.NOTIFICATION_ID, (int) bill._id);
        notificationIntent.putExtra(BillCompletionNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) bill._id, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

//        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify((int) bill._id, builder.build());

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
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
        void onListItemSelected(Bill item);

        void onListLoadFailed(SQLException e);
    }
}
