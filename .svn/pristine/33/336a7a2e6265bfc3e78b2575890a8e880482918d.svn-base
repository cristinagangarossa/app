package com.neperiagroup.happysalus;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.neperiagroup.happysalus.adapters.MyDeviceAdapter;
import com.neperiagroup.happysalus.services.DeviceService;
import com.neperiagroup.happysalus.services.LoginService;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT_NOTIFICATIONS = "notifications_fragment";

    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //signup toolbar setup.
        toolbar = findViewById(R.id.signupToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = findViewById(R.id.signup_toolbar_title);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                switch (f.getClass().getName()){
                    case "com.neperiagroup.happysalus.SettingsFragment":
                        toolbarTitle.setText(R.string.settings);
                        break;
                    case "com.neperiagroup.happysalus.NotificationsFragment":
                        toolbarTitle.setText(R.string.notifications);
                        break;
                    case "com.neperiagroup.happysalus.DeviceSettingsFragment":
                        toolbarTitle.setText(R.string.ip68);
                        break;
                    case "com.neperiagroup.happysalus.ActivityDeviceFragment":
                        toolbarTitle.setText(R.string.activity);
                        break;
                }
            }
        }, true);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayoutSettings, new SettingsFragment());
        fragmentTransaction.commit();


    }

    public static Intent getIstanceIntent(Context context){
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }

    @Override
    public boolean onSupportNavigateUp(){
        if(!getSupportFragmentManager().popBackStackImmediate()){
            finish();
        }
        return true;
    }
}
