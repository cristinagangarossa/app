package com.neperiagroup.happysalus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.Or;
import com.neperiagroup.happysalus.bean.User;
import com.neperiagroup.happysalus.services.LoginService;
import com.neperiagroup.happysalus.utility.TagUser;
import com.neperiagroup.happysalus.utility.StoreInMemory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class InfoWelcomeActivity extends AppValidator{

    private static final String TAG = InfoWelcomeActivity.class.getName();
    private static final String KEY_ID_BUTTON = TAG + ".sex";

    private Button selectedWomanButton;
    private Button selectedManButton;

    @Min(value = 1, message = "L'età deve essere maggiore di 1.")
    @Or
    @Max(value = 200, message = "L'età deve essere minore di 200.")
    private TextInputEditText textInputAge;

    @Min(value = 10, message = "L'altezza deve essere maggiore di 10 cm.")
    @Or
    @Max(value = 300, message = "L'altezza deve essere minore di 300 cm.")
    private TextInputEditText textInputHeight;

    @Min(value = 10, message = "Il peso deve essere maggiore di 25 Kg.")
    @Or
    @Max(value = 300, message = "Il peso deve essere minore di 600 Kg.")
    private TextInputEditText textInputWeight;

    private Button nextButton;

    private int selectedIdButton;

    private User user;
    private TagUser tagUser;
    private StoreInMemory storeInMemory;
    private LoginService loginService;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_welcome);

        //signup toolbar setup.
        toolbar = findViewById(R.id.signupToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storeInMemory = new StoreInMemory(getApplicationContext());
        tagUser = new TagUser();
        user = new User();
        loginService = new LoginService(getApplicationContext());

        selectedWomanButton = findViewById(R.id.selectedWomanButton);
        selectedManButton = findViewById(R.id.selectedManButton);

        textInputAge = findViewById(R.id.textInputAge);
        textInputHeight = findViewById(R.id.textInputHeight);
        textInputWeight = findViewById(R.id.textInputWeight);

        nextButton = findViewById(R.id.nextButton);

        selectedIdButton = getIntent().getIntExtra(KEY_ID_BUTTON, 0);

        switch (selectedIdButton){
            case R.id.womanButton:
                selectedManButton.setEnabled(false);
                user.setSex(false);//False=Woman
                break;
            case R.id.manButton:
                selectedWomanButton.setEnabled(false);
                user.setSex(true);//True=Man
                break;
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoWelcomeActivity.super.onClick(v);
                if (validated) {
                    user.setId(1);
                    user.setHeight(Integer.parseInt(textInputHeight.getText().toString()));
                    user.setWeight(Integer.parseInt(textInputWeight.getText().toString()));
                    user.setAge(Integer.parseInt(textInputAge.getText().toString()));
                    user.setBirthday("03/05/1989");

                    user.setWearLeft(true);
                    loginService.signup(user);
                    Toast.makeText(getApplicationContext(), "Registrazione effettuata con successo.", Toast.LENGTH_SHORT).show();
                    startActivity(DashboardActivity.getInstanceIntent(getApplicationContext()));
                }
            }
        });
    }

    public static Intent getIstanceIntent(Context context, int idButton){
        Intent intent = new Intent(context, InfoWelcomeActivity.class);
        intent.putExtra(KEY_ID_BUTTON, idButton);
        return intent;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
