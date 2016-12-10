package com.aspirephile.laundro;

import android.support.multidex.MultiDexApplication;

import com.aspirephile.laundro.db.LaundroDb;
import com.aspirephile.shared.debug.Logger;

public class App extends MultiDexApplication {
    Logger l = new Logger(App.class);

    @Override
    public void onCreate() {
        super.onCreate();
        l.d(App.class.getSimpleName() + " being created");

        LaundroDb.initialize(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        l.w("onTerminate");
    }
}
