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
import com.shawnlin.numberpicker.NumberPicker;

public class ModificationsWeight extends Fragment {

    private Button nextButton;
    private NumberPicker numberPicker;

    private StoreInMemory storeInMemory;
    User user;

    int weight;

    public ModificationsWeight() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modify_weight, container, false);

        storeInMemory = new StoreInMemory(getActivity().getApplicationContext());
        user = new User();
        user = storeInMemory.getUser(1);

        nextButton = view.findViewById(R.id.nextButtonWeight);
        numberPicker = view.findViewById(R.id.number_picker_weight);

        weight = user.getWeight();
        numberPicker.setValue(weight);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int weight = numberPicker.getValue();
                user.setWeight(weight);
                storeInMemory.setUser(user);
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
