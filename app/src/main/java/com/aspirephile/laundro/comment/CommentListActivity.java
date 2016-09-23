
package com.aspirephile.laundro.comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.aspirephile.laundro.Constants;
import com.aspirephile.laundro.R;
import com.aspirephile.shared.debug.Logger;
import com.aspirephile.shared.debug.NullPointerAsserter;

import org.kawanfw.sql.api.client.android.AceQLDBManager;
import org.kawanfw.sql.api.client.android.BackendConnection;
import org.kawanfw.sql.api.client.android.execute.OnGetPrepareStatement;
import org.kawanfw.sql.api.client.android.execute.update.OnUpdateCompleteListener;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommentListActivity extends AppCompatActivity implements CommentListFragment.OnListFragmentInteractionListener {
    private Logger l = new Logger(CommentListActivity.class);

    private String PID;
    private NullPointerAsserter asserter = new NullPointerAsserter(l);
    private CommentListFragment commentListF;
    private EditText description;

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
        } else if ((PID = i.getStringExtra(Constants.extras.PID)) == null) {
            Intent data = new Intent();
            data.putExtra(Constants.extras.errorResult, Constants.errorResults.badPID);
            data.putExtra(Constants.extras.PID, PID);
            setResult(RESULT_CANCELED, data);
            return;
        }

        setContentView(R.layout.activity_comment_list);

        description = (EditText) findViewById(R.id.et_comment_description);
        findViewById(R.id.fab_comment_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AceQLDBManager.executeUpdate(new OnGetPrepareStatement() {
                    @Override
                    public PreparedStatement onGetPreparedStatement(BackendConnection remoteConnection) {
                        String sql = "insert into ParlayComment (PID,username,description) values (?,?,?)";
                        PreparedStatement preparedStatement = null;
                        try {
                            preparedStatement = remoteConnection.prepareStatement(sql);
                            preparedStatement.setString(1, PID);
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
        openCommentListFragment();
    }


    private void openCommentListFragment() {
        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        commentListF = (CommentListFragment) fm.findFragmentByTag(Constants.tags.commentListFragment);

        if (!asserter.assertPointerQuietly(commentListF)) {
            l.i("Creating new " + CommentListFragment.class.getSimpleName() + " fragment");
            commentListF = CommentListFragment.newInstance(1, PID);
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
    public void onCommentListItemSelected(CommentListItem.CommentItem item) {
        l.d("Comment list item selected");
    }

    @Override
    public void onCommentListLoadFailed(SQLException e) {
        e.printStackTrace();
    }
}
