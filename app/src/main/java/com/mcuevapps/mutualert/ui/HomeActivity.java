package com.mcuevapps.mutualert.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.UIService;
import com.mcuevapps.mutualert.ui.contacts.ContactListFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private TextView textViewToolbar;
    private BottomSheetDialog bottomSheetDialog;
    private FloatingActionButton fab;

    private Toolbar.OnMenuItemClickListener mOnMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    fab.show();
                    break;
                case R.id.navigation_contact:
                    fragment = new ContactListFragment();
                    fab.hide();
                    break;
            }

            FragmentManager fm = getSupportFragmentManager();
            Fragment currentFragment = fm.findFragmentById(R.id.frameLayout);
            if( fragment != null && !fragment.getClass().toString().equals(currentFragment.getTag()) ) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, fragment, fragment.getClass().toString())
                        .commit();
                setToolbarText(item.getTitle().toString());
                UIService.showEventToast(item.getTitle().toString());
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

        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setOnMenuItemClickListener(mOnMenuItemClickListener);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomModal();
            }
        });

        Fragment fragment = new DashboardFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, fragment, fragment.getClass().toString())
                .commit();
    }

    private void setToolbarText(String text){
        textViewToolbar.setText(text);
    }

    private void showBottomModal(){
        HomeBottomModalFragment bottomModalFragment = new HomeBottomModalFragment();
        bottomModalFragment.show( getSupportFragmentManager(), "HomeBottomModalFragment" );
    }
}