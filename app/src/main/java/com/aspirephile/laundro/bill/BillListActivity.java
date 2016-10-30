package com.aspirephile.laundro.bill;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aspirephile.laundro.Constants;
import com.aspirephile.laundro.R;
import com.aspirephile.laundro.db.tables.Bill;

public class BillListActivity extends AppCompatActivity implements BillListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onListItemSelected(Bill item) {
        //TODO Open the right point here
        Intent i = new Intent(BillListActivity.this, BillViewerActivity.class);
        i.putExtra(Constants.extras._id, item._id);
        startActivity(i);
    }

    @Override
    public void onListLoadFailed(SQLException e) {
        e.printStackTrace();
        Snackbar.make(findViewById(R.id.cl_bill_list), e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                //TODO Set Action
//                .setAction(R.string.retry, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (asserter.assertPointer(pointListF))
//                            pointListF.onRefresh();
//                    }
//                })
                .show();
    }
}
