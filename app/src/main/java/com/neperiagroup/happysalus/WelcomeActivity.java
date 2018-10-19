package com.neperiagroup.happysalus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.neperiagroup.happysalus.utility.TagUser;
import com.neperiagroup.happysalus.utility.StoreInMemory;

public class WelcomeActivity extends AppCompatActivity {

    private Button womanButton;
    private Button manButton;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //signup toolbar setup.
        toolbar = findViewById(R.id.signupToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        womanButton = findViewById(R.id.womanButton);
        manButton = findViewById(R.id.manButton);

        womanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(InfoWelcomeActivity.getIstanceIntent(getApplicationContext(), R.id.womanButton));
            }
        });

        manButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(InfoWelcomeActivity.getIstanceIntent(getApplicationContext(), R.id.manButton));
            }
        });
    }

    public static Intent getIstanceIntente(Context context){
        Intent intent = new Intent(context, WelcomeActivity.class);
        return intent;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
