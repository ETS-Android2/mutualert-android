package com.mcuevapps.mutualert.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mcuevapps.mutualert.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class HomeActivity extends AppCompatActivity {

    private TextView textViewToolbar;
    private FloatingActionButton fab;

    private Toolbar.OnMenuItemClickListener mOnMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    setToolbarText(getString(R.string.dashboard));
                    Toast.makeText(getApplicationContext(), getString(R.string.dashboard), Toast.LENGTH_SHORT).show();
                    fab.show();
                    break;
                case R.id.navigation_contact:
                    fragment = new ContactsFragment();
                    setToolbarText(getString(R.string.contacts));
                    Toast.makeText(getApplicationContext(),getString(R.string.contacts), Toast.LENGTH_SHORT).show();
                    fab.hide();
                    break;
            }

            if(fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .commit();
                return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            initUI();
        }
    }

    private void initUI() {
        getSupportActionBar().hide();

        fab = findViewById(R.id.fab);
        textViewToolbar = findViewById(R.id.textViewToolbar);

        BottomAppBar navigation = findViewById(R.id.bottomAppBar);
        navigation.setOnMenuItemClickListener(mOnMenuItemClickListener);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, new DashboardFragment())
                .commit();
    }

    private void setToolbarText(String text){
        textViewToolbar.setText(text);
    }
}