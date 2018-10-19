package com.neperiagroup.happysalus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.neperiagroup.happysalus.bean.User;
import com.neperiagroup.happysalus.utility.StoreInMemory;

public class ModificationSex extends Fragment {

    private Button womanButton;
    private Button manButton;
    private Button nextButton;

    private StoreInMemory storeInMemory;

    User user;

    public ModificationSex() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.modify_sex, container, false);

        storeInMemory = new StoreInMemory(getActivity().getApplicationContext());
        user = new User();
        user = storeInMemory.getUser(1);

        womanButton = view.findViewById(R.id.womanButtonSex);
        manButton = view.findViewById(R.id.manButtonSex);
        nextButton = view.findViewById(R.id.nextButtonSex);


        womanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manButton.setEnabled(false);
                user.setSex(false);
                storeInMemory.setUser(user);
            }
        });

        manButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                womanButton.setEnabled(false);
                user.setSex(true);
                storeInMemory.setUser(user);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
