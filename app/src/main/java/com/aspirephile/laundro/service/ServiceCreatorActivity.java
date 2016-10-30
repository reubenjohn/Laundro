package com.aspirephile.laundro.service;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.aspirephile.laundro.Constants;
import com.aspirephile.laundro.R;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

public class ServiceCreatorActivity extends AppCompatActivity implements ServiceCreatorFragment.PointCreatorListener {
    private Logger l = new Logger(ServiceCreatorActivity.class);
    private NullPointerAsserter asserter = new NullPointerAsserter(l);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        l.onCreate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_creator);
        openOrganizationCreatorFragment();
    }

    @Override
    protected void onStart() {
        l.onStart();
        super.onStart();
    }

    @Override
    protected void onResume() {
        l.onResume();
        super.onResume();
    }


    @Override
    protected void onPause() {
        l.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        l.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        l.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        l.onCreateOptionsMenu();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_point_creator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openOrganizationCreatorFragment() {
        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        ServiceCreatorFragment organizationCreatorF = (ServiceCreatorFragment) fm.findFragmentByTag(Constants.tags.pointViewerFragment);

        if (!asserter.assertPointerQuietly(organizationCreatorF)) {
            l.i("Creating new " + ServiceCreatorFragment.class.getSimpleName() + " fragment");
            organizationCreatorF = new ServiceCreatorFragment();
            fm.beginTransaction()
                    .replace(R.id.container_organization_creator, organizationCreatorF, Constants.tags.pointViewerFragment)
                    .commit();
        }
        asserter.assertPointer(organizationCreatorF);
    }

    @Override
    public void onCreationSuccess() {
        l.d("Heard success");
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onCreationFailed() {
        l.d("Heard failure");
        setResult(RESULT_CANCELED);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
