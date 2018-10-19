package com.neperiagroup.happysalus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MailLoginActivity extends AppCompatActivity {
    Button login;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail_login_activity);

        login=(Button)findViewById(R.id.btnlogin);
        username=(EditText)findViewById(R.id.txtuser);
        password=(EditText)findViewById(R.id.txtpass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    public void login(){
        String user=username.getText().toString().trim();
        String pass=password.getText().toString().trim();
        if(user.equals("username")&& pass.equals("password"))
        {
            Toast.makeText(this,"username and password matched!",Toast.LENGTH_LONG).show();
            startActivity(DashboardActivity.getInstanceIntent(getApplicationContext()));
        }else {
            Toast.makeText(this,"username and password do not matched!",Toast.LENGTH_LONG).show();
        }
    }
    public static Intent getInstanceIntent(Context context){
        Intent intent = new Intent(context, MailLoginActivity.class);
        return intent;
    }
}