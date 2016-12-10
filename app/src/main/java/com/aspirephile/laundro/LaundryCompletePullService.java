package com.aspirephile.laundro;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.aspirephile.laundro.bill.BillViewerActivity;
import com.aspirephile.laundro.db.LaundroDb;
import com.aspirephile.laundro.db.async.OnQueryCompleteListener;
import com.aspirephile.laundro.db.tables.Bill;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Reuben John on 11/18/2016.
 */
public class LaundryCompletePullService extends IntentService {
    private Logger l = new Logger(LaundryCompletePullService.class);
    private NullPointerAsserter asserter = new NullPointerAsserter(l);
    private ArrayList<RunnableNotification> scheduledNotifications = new ArrayList<>();
    private Handler handler = new Handler();

    public LaundryCompletePullService() {
        super(LaundryCompletePullService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        for (int i = 0; i < 100; i++) {
            LaundroDb.getBillManager().getBillListQuery().queryInBackground(new OnQueryCompleteListener() {
                @Override
                public void onQueryComplete(Cursor c, SQLException e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        List<Bill> list = LaundroDb.getBillManager().getBillListFromResult(c);
                        l.i("Point query completed with " + list.size() + " results");
                        scheduleNotifications(list);
                    }
                }
            });
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopSelf();
    }

    private void scheduleNotifications(List<Bill> list) {
        final Random r = new Random();
        for (final Bill bill : list) {
            if (bill.payedAt == -2 && !isAlreadyScheduled(bill)) {
                RunnableNotification runnable = new RunnableNotification() {
                    Bill getBill() {
                        return bill;
                    }

                    @Override
                    public void run() {
                        showNotification(getBaseContext().getApplicationContext(), this);
                    }
                };
                scheduledNotifications.add(runnable);
                handler.postDelayed(runnable, 5000 + r.nextLong() % 10000);
            }
        }
    }

    private boolean isAlreadyScheduled(Bill bill) {
        for (RunnableNotification r : scheduledNotifications) {
            if (bill._id == r.getBill()._id)
                return true;
        }
        return false;
    }

    public void showNotification(Context context, RunnableNotification runnableNotification) {//delay is after how much time(in millis) from current time you want to schedule the notification
        Bill bill = runnableNotification.getBill();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(bill.service.name + " has just finished with your batch of clothes")
                .setContentText("You can clothes your clothes from their service center when they're open")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.my_logo)
                .setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.my_logo)).getBitmap())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, BillViewerActivity.class);
        intent.putExtra(Constants.extras._id, bill._id);
        PendingIntent activity = PendingIntent.getActivity(context, (int) bill._id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(activity);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify((int) bill._id, builder.build());
        scheduledNotifications.remove(runnableNotification);
    }

    private abstract class RunnableNotification implements Runnable {
        abstract Bill getBill();
    }
}
