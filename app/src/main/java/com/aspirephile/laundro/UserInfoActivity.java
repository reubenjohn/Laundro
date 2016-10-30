package com.aspirephile.laundro;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspirephile.laundro.db.LaundroDb;
import com.aspirephile.laundro.db.OnQueryCompleteListener;
import com.aspirephile.laundro.db.tables.User;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView contactImgImgView;
    private CoordinatorLayout coordinatorLayout;
    private EditText editTextFirst;
    private EditText editTextLast;
    private String email;
    private TextView tUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.aspirephile.laundro.R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(com.aspirephile.laundro.R.id.toolbar);
        setSupportActionBar(toolbar);

        email = getSharedPreferences(Constants.files.authentication, Context.MODE_PRIVATE)
                .getString(Constants.preferences.username, null);

        coordinatorLayout = (CoordinatorLayout) findViewById(com.aspirephile.laundro.R.id.cl_user_info);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setDisplayHomeAsUpEnabled(true);

        Button saveChangesButton = (Button) findViewById(com.aspirephile.laundro.R.id.buttonSaveChanges);
        saveChangesButton.setOnClickListener(this);

        //Intent for choosing image and bringing it to this activity
        contactImgImgView = (ImageView) findViewById(com.aspirephile.laundro.R.id.imageViewUserImage);
        contactImgImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Set Profile Picture"), 1);
            }
        });

        editTextFirst = (EditText) findViewById(com.aspirephile.laundro.R.id.editTextFname);
        editTextLast = (EditText) findViewById(com.aspirephile.laundro.R.id.editTextLname);
        tUsername = (TextView) findViewById(com.aspirephile.laundro.R.id.textViewUsername);

        LaundroDb.getUserManager().getUser(email).queryInBackground(new OnQueryCompleteListener() {
            @Override
            public void onQueryComplete(Cursor c, SQLException e) {
                if (e != null) {
                    e.printStackTrace();
                    Snackbar.make(coordinatorLayout, e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    User user = LaundroDb.getUserManager().getUserFromResult(c);
                    updateProfile(user);
                }
            }
        });
    }

    private void updateProfile(User user) {
        tUsername.setText(user.email);
        editTextFirst.setText(user.name);
        //TODO Introduce first and last name into database
        //editTextLast.setText(user.lName);

    }

    public void onActivityResult(int resCode, int reqCode, Intent data) {
        if (resCode == RESULT_OK)        //If user presses back button
            if (reqCode == 1)
                contactImgImgView.setImageURI(data.getData());
    }

    @Override
    public void onClick(View v) {
        final String fname = editTextFirst.getText().toString();
        final String lname = editTextLast.getText().toString();

        LaundroDb.getUserManager().updateName(email, fname)
                .executeInBackground(new com.aspirephile.laundro.db.OnUpdateCompleteListener() {
                    @Override
                    public void onUpdateComplete(int cursor, SQLException e) {
                        if (e != null) {
                            e.printStackTrace();
                            Snackbar.make(coordinatorLayout, e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                                    .show();
                        } else {
                            Snackbar.make(coordinatorLayout, com.aspirephile.laundro.R.string.user_info_update_success, Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }
}
