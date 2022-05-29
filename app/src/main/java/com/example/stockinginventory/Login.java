package com.example.stockinginventory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity implements View.OnClickListener {
    TextView register;
    EditText mEmail,mPassword;
    FirebaseAuth fAuth;
    Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail=(EditText) findViewById(R.id.email);
        mPassword=(EditText) findViewById(R.id.password);
        fAuth = FirebaseAuth.getInstance();
        Login=(Button)findViewById(R.id.Login);
        Login.setOnClickListener(this);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register:
                startActivity(new Intent(this,Authentication.class));
                break;
            case R.id.Login:
                signin(v);
                break;
            default:
                Toast.makeText(Login.this,"Bandh", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void signin(View v){
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            mEmail.setError("Email is Required");
            return;
        }
        if(TextUtils.isEmpty(password)){
            mEmail.setError("password is Required");
            return;
        }
        if(password.length() < 6){
            mPassword.setError("password must be >= 6 characters");
            return;
        }
        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }else{

                Toast.makeText(Login.this,"Error !" + Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
            }
            mEmail.setText("");
            mPassword.setText("");
        });
    }
}