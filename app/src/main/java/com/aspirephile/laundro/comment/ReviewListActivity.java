
package com.aspirephile.laundro.comment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.aspirephile.laundro.Constants;
import com.aspirephile.laundro.R;
import com.aspirephile.laundro.db.LaundroDb;
import com.aspirephile.laundro.db.OnInsertCompleteListener;
import com.aspirephile.laundro.db.tables.Review;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

public class ReviewListActivity extends AppCompatActivity implements ReviewListFragment.OnListFragmentInteractionListener {
    private Logger l = new Logger(ReviewListActivity.class);

    private long serviceId;
    private NullPointerAsserter asserter = new NullPointerAsserter(l);
    private ReviewListFragment commentListF;
    private EditText description;
    private long userId;
    private RatingBar ratingBar;

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
        } else if ((serviceId = i.getLongExtra(Constants.extras._id, -1)) == -1L) {
            Intent data = new Intent();
            data.putExtra(Constants.extras.errorResult, Constants.errorResults.badPID);
            data.putExtra(Constants.extras._id, serviceId);
            setResult(RESULT_CANCELED, data);
            return;
        }

        setContentView(R.layout.activity_review_list);

        description = (EditText) findViewById(R.id.et_comment_description);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);


        SharedPreferences sp = getSharedPreferences(Constants.files.authentication, MODE_PRIVATE);
        userId = sp.getLong(Constants.preferences.userId, -1);
        if (userId == -1) {
            Intent data = new Intent();
            data.putExtra(Constants.extras.errorResult, Constants.errorResults.badUsername);
            setResult(RESULT_CANCELED, data);
            return;
        }

        findViewById(R.id.fab_comment_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Review review = new Review(serviceId, ReviewListActivity.this.userId, System.currentTimeMillis(), ratingBar.getRating(), description.getText().toString());
                LaundroDb.getReviewManager().insertReview(review).executeInBackground(new OnInsertCompleteListener() {
                    @Override
                    public void onInsertComplete(long rowId, SQLException e) {
                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(ReviewListActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            commentListF.onRefresh();
                        }
                    }
                });
            }
        });
        //Review review = new Review(sp.getString());
        //LaundroDb.getReviewManager().insertReview();
        /*
        findViewById(R.id.fab_comment_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AceQLDBManager.executeUpdate(new OnGetPrepareStatement() {
                    @Override
                    public PreparedStatement onGetPreparedStatement(BackendConnection remoteConnection) {
                        String sql = "insert into ParlayComment (serviceId,email,description) values (?,?,?)";
                        PreparedStatement preparedStatement = null;
                        try {
                            preparedStatement = remoteConnection.prepareStatement(sql);
                            preparedStatement.setString(1, serviceId);
                            String username = getSharedPreferences(Constants.files.authentication, Context.MODE_PRIVATE)
                                    .getString(Constants.preferences.username, null);
                            preparedStatement.setString(2, username);
                            preparedStatement.setString(3, description.getText().toString());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return preparedStatement;
                    }
                }, new OnUpdateCompleteListener() {
                    @Override
                    public void onUpdateComplete(int result, SQLException e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            l.i("Comments affected: " + result);
                            commentListF.onRefresh();
                        }
                    }
                });
            }
        });
        */
        openCommentListFragment();
    }


    private void openCommentListFragment() {
        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        commentListF = (ReviewListFragment) fm.findFragmentByTag(Constants.tags.commentListFragment);

        if (!asserter.assertPointerQuietly(commentListF)) {
            l.i("Creating new " + ReviewListFragment.class.getSimpleName() + " fragment");
            commentListF = ReviewListFragment.newInstance(1, serviceId, userId);
            fm.beginTransaction()
                    .replace(R.id.container_comment_list, commentListF, Constants.tags.commentListFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment_list, menu);
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
    public void onCommentListItemSelected(Review item) {
        l.d("Comment list item selected");
    }

    @Override
    public void onCommentListLoadFailed(SQLException e) {
        e.printStackTrace();
    }
}
