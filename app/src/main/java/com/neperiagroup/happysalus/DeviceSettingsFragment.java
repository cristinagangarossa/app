package com.neperiagroup.happysalus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.neperiagroup.happysalus.utility.StoreInMemory;
import com.neperiagroup.happysalus.utility.TagSettings;


public class DeviceSettingsFragment extends Fragment {

    private LinearLayout linearLayoutActivityDevice;

    private Switch switchSettingsDevice1;
    private Switch switchSettingsDevice2;
    private Switch switchSettingsDevice3;
    private Switch switchSettingsDevice4;
    private Switch switchSettingsDevice5;

    private StoreInMemory storeInMemory;
    private TagSettings tagSettings;

    public DeviceSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_settings, container, false);

        storeInMemory = new StoreInMemory(getContext().getApplicationContext());
        tagSettings = new TagSettings();

        linearLayoutActivityDevice = view.findViewById(R.id.linearLayoutActivityDevice);
        linearLayoutActivityDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                fragmentTransaction.replace(R.id.frameLayoutSettings, new ActivityDeviceFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        switchSettingsDevice1 = view.findViewById(R.id.switchSettingsDevice1);
        switchSettingsDevice2 = view.findViewById(R.id.switchSettingsDevice2);
        switchSettingsDevice3 = view.findViewById(R.id.switchSettingsDevice3);
        switchSettingsDevice4 = view.findViewById(R.id.switchSettingsDevice4);
        switchSettingsDevice5 = view.findViewById(R.id.switchSettingsDevice5);

        switchSettingsDevice1.setChecked(storeInMemory.getValueBooleanFromStore(tagSettings.getKEY_INCOMING_CALL()));
        switchSettingsDevice2.setChecked(storeInMemory.getValueBooleanFromStore(tagSettings.getKEY_WHATSAPP()));
        switchSettingsDevice3.setChecked(storeInMemory.getValueBooleanFromStore(tagSettings.getKEY_INCOMING_EMAIL()));
        switchSettingsDevice4.setChecked(storeInMemory.getValueBooleanFromStore(tagSettings.getKEY_GOL_NOTIFICATIONS()));
        switchSettingsDevice5.setChecked(storeInMemory.getValueBooleanFromStore(tagSettings.getKEY_HEALTH_HISTORY()));

        switchSettingsDevice1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    storeInMemory.setValueBooleanToStore(tagSettings.getKEY_INCOMING_CALL(), true);
                } else {
                    storeInMemory.setValueBooleanToStore(tagSettings.getKEY_INCOMING_CALL(), false);
                }
            }
        });

        switchSettingsDevice2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    storeInMemory.setValueBooleanToStore(tagSettings.getKEY_WHATSAPP(), true);
                } else {
                    storeInMemory.setValueBooleanToStore(tagSettings.getKEY_WHATSAPP(), false);
                }
            }
        });

        switchSettingsDevice3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    storeInMemory.setValueBooleanToStore(tagSettings.getKEY_INCOMING_EMAIL(), true);
                } else {
                    storeInMemory.setValueBooleanToStore(tagSettings.getKEY_INCOMING_EMAIL(), false);
                }
            }
        });

        switchSettingsDevice4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    storeInMemory.setValueBooleanToStore(tagSettings.getKEY_GOL_NOTIFICATIONS(), true);
                } else {
                    storeInMemory.setValueBooleanToStore(tagSettings.getKEY_GOL_NOTIFICATIONS(), false);
                }
            }
        });

        switchSettingsDevice5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    storeInMemory.setValueBooleanToStore(tagSettings.getKEY_HEALTH_HISTORY(), true);
                } else {
                    storeInMemory.setValueBooleanToStore(tagSettings.getKEY_HEALTH_HISTORY(), false);
                }
            }
        });
        return view;
    }

}
