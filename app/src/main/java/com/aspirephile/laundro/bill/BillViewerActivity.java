package com.aspirephile.laundro.bill;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.aspirephile.laundro.Constants;
import com.aspirephile.laundro.R;
import com.aspirephile.laundro.db.tables.Item;
import com.aspirephile.laundro.service.ServiceViewerFragment;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

public class BillViewerActivity extends AppCompatActivity implements BillViewerFragment.OnFragmentInteractionListener, BillViewerFragment.OnItemFragmentInteractionListener {

    private Logger l = new Logger(BillViewerActivity.class);

    private long _id;
    private NullPointerAsserter asserter = new NullPointerAsserter(l);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        l.onCreate();
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        if (i == null) {
            Intent data = new Intent();
            data.putExtra(Constants.extras.errorResult, Constants.errorResults.badIntent);
            setResult(RESULT_CANCELED, data);
            return;
        } else if ((_id = i.getLongExtra(Constants.extras._id, -1)) == -1) {
            Intent data = new Intent();
            data.putExtra(Constants.extras.errorResult, Constants.errorResults.badPID);
            data.putExtra(Constants.extras._id, _id);
            setResult(RESULT_CANCELED, data);
            return;
        }

        setContentView(R.layout.activity_service_viewer);

        openBillViewerFragment();
    }


    private void openBillViewerFragment() {
        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        BillViewerFragment serviceViewerF = (BillViewerFragment) fm.findFragmentByTag(Constants.tags.billViewerFragment);

        if (!asserter.assertPointerQuietly(serviceViewerF)) {
            l.i("Creating new " + ServiceViewerFragment.class.getSimpleName() + " fragment");
            serviceViewerF = new BillViewerFragment();
            fm.beginTransaction()
                    .replace(R.id.container_point_viewer, serviceViewerF, Constants.tags.pointViewerFragment)
                    .commit();
        }
        if (asserter.assertPointer(serviceViewerF))
            serviceViewerF.setID(_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_point_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home_action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFetchFailed(SQLException e) {
        Intent data = new Intent();
        data.putExtra(Constants.extras.errorResult, e.getLocalizedMessage());
        setResult(RESULT_CANCELED, data);
    }

    @Override
    public void onListItemSelected(Item mItem) {

    }
}
