package com.neperiagroup.happysalus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignUpActivity  extends AppCompatActivity{
    Button login;
    EditText username, password, passwordR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        login=(Button)findViewById(R.id.btnlogin);
        username=(EditText)findViewById(R.id.txtuser);
        password=(EditText)findViewById(R.id.txtpass);
        passwordR=(EditText)findViewById(R.id.txtpassR);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogin();
            }
        });
    }
    public void getLogin(){
        String user=username.getText().toString().trim();
        String pass=password.getText().toString().trim();
        String passR=passwordR.getText().toString().trim();
        if(user.equals("username")&& pass.equals("password") && passR.equals("password"))
        {
            Toast.makeText(this,"username and password matched!",Toast.LENGTH_LONG).show();
            startActivity(WelcomeActivity.getIstanceIntente(getApplicationContext()));
            finish();
        }else {
            Toast.makeText(this,"username and password do not matched!",Toast.LENGTH_LONG).show();
        }
    }
    public static Intent getInstanceIntent(Context context){
        Intent intent = new Intent(context, SignUpActivity.class);
        return intent;
    }
}