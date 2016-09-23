package com.aspirephile.laundro;

import android.support.multidex.MultiDexApplication;

import com.aspirephile.laundro.db.LaundroDb;
import com.aspirephile.shared.debug.Logger;

import org.kawanfw.sql.api.client.android.AceQLDBManager;
import org.kawanfw.sql.api.client.android.BackendConnection;
import org.kawanfw.sql.api.client.android.OnRemoteConnectionEstablishedListener;

import java.sql.SQLException;

public class App extends MultiDexApplication {
    public static final String packageName = App.class.getPackage().getName();
    Logger l = new Logger(App.class);

    @Override
    public void onCreate() {
        super.onCreate();
        l.d(App.class.getSimpleName() + " being created");

        String url = getSharedPreferences(Constants.files.settings, MODE_WORLD_READABLE)
                .getString(Constants.preferences.url, getString(com.aspirephile.laundro.R.string.pref_default_data_sync_backend_url));

        AceQLDBManager.initialize(url,
                "reuben", "pass");
        LaundroDb.initialize(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        l.w("onTerminate");
        AceQLDBManager.getDefaultRemoteConnectionIfExists(new OnRemoteConnectionEstablishedListener() {
            @Override
            public void onRemoteConnectionEstablishedListener(BackendConnection remoteConnection, SQLException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    try {
                        remoteConnection.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
