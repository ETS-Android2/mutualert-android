package com.mcuevapps.mutualert.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.Service.UIService;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.data.ContactViewModel;
import com.mcuevapps.mutualert.retrofit.response.AlertContact;
import com.mcuevapps.mutualert.ui.contacts.ContactListFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private TextView textViewToolbar;
    private FloatingActionButton fab;
    private String currentFragment;

    private ContactViewModel contactViewModel;

    private Toolbar.OnMenuItemClickListener mOnMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();

            Fragment fragment = null;
            switch (id) {
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    break;
                case R.id.navigation_contact:
                    fragment = new ContactListFragment();
                    break;
            }
            if( fragment != null && !currentFragment.equals(fragment.getClass().toString()) ) {
                currentFragment = fragment.getClass().toString();
                fabDefault();

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
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        getSupportActionBar().hide();

        textViewToolbar = findViewById(R.id.textViewToolbar);

        fab = findViewById(R.id.fab);
        fabDefault();

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
        currentFragment = fragment.getClass().toString();

        loadContactData();
    }

    private void loadContactData() {
        contactViewModel.getContacts().observe(this, new Observer<List<AlertContact>>() {
            @Override
            public void onChanged(@Nullable List<AlertContact> contacts) {
                if(currentFragment.equals(new ContactListFragment().getClass().toString()) && contacts.size()<Constantes.CONTACT_LENGTH){
                    fab.setImageResource(R.drawable.ic_baseline_person_add_white_24);
                    fab.setOnClickListener(v -> contactViewModel.createContact((HomeActivity.this)));
                } else {
                    fabDefault();
                }
            }
        });
    }

    private void fabDefault(){
        fab.setImageResource(R.drawable.ic_baseline_my_location_white_24);
        fab.setOnClickListener(v -> sendAlert());
    }

    private void setToolbarText(String text){
        textViewToolbar.setText(text);
    }

    private void sendAlert(){
        Toast.makeText(this, "Comenzando alerta", Toast.LENGTH_SHORT).show();
    }

    private void showBottomModal(){
        HomeBottomModalFragment bottomModalFragment = new HomeBottomModalFragment();
        bottomModalFragment.show( getSupportFragmentManager(), "HomeBottomModalFragment" );
    }
}