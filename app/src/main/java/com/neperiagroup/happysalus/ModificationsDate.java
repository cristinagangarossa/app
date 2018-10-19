package com.neperiagroup.happysalus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.neperiagroup.happysalus.bean.User;
import com.neperiagroup.happysalus.utility.StoreInMemory;

public class ModificationsDate extends Fragment {

    DatePicker simpleDatePicker;
    private Button nextButton;

    private StoreInMemory storeInMemory;
    User user;

    String date = "";
    String[] calend = {};
    String strDate = "";
    int day;
    int month;
    int year;

    public ModificationsDate() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.modify_date, container, false);

        storeInMemory = new StoreInMemory(getActivity().getApplicationContext());
        user = new User();
        user = storeInMemory.getUser(1);

        simpleDatePicker = (DatePicker) view.findViewById(R.id.simpleDatePicker); //
        nextButton = view.findViewById(R.id.nextButtonDate);

        date = user.getBirthday();
        calend = date.split("/");
        day = Integer.parseInt(calend[0]);
        month = Integer.parseInt(calend[1]);
        year = Integer.parseInt(calend[2]);

        simpleDatePicker.init(year, month-1, day, null);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = simpleDatePicker.getYear();
                int month = simpleDatePicker.getMonth();
                int day = simpleDatePicker.getDayOfMonth();

                if (Integer.toString(day).length() > 1) {
                    strDate += Integer.toString(day);
                } else {
                    strDate += "0" + Integer.toString(day);
                }
                strDate += "/";
                if (Integer.toString(month).length() > 1) {
                    strDate += Integer.toString(month+1);
                } else {
                    strDate += "0" + Integer.toString(month+1);
                }
                strDate += "/";
                strDate += Integer.toString(year);
                user.setBirthday(strDate);
                storeInMemory.setUser(user);
                getActivity().onBackPressed();
            }
        });
        
        return view;
    }
}
