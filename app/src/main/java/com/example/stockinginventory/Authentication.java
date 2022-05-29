package com.example.stockinginventory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Authentication extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG1 = "TAG";
    private EditText etfirstName;
    private EditText etlastName;
    private EditText etid;
    private EditText etphno;
    private EditText etemail;
    private EditText etpassword;
    private FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        Button registeruser = (Button) findViewById(R.id.register);
        registeruser.setOnClickListener(this);

        TextView login = (TextView) findViewById(R.id.Login);
        login.setOnClickListener(this);

        etfirstName = findViewById(R.id.firstName);
        etlastName = findViewById(R.id.lastName);
        etid = findViewById(R.id.id);
        //etblock = findViewById(R.id.lastName);
        etphno = findViewById(R.id.phno);
        etemail = findViewById(R.id.email);
        etpassword = findViewById(R.id.password);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                Registeruser(v);
                break;
            case R.id.Login:
                login(v);
                break;
        }

    }

    public void login(View v) {
        startActivity(new Intent(getApplicationContext(),Login.class));
    }

    public void Registeruser(View v){
        String firstName = etfirstName.getText().toString().trim();
        String lastName = etlastName.getText().toString().trim();
        String id = etid.getText().toString().trim();
       // String block = etblock.getText().toString().trim();
        String phno = etphno.getText().toString().trim();
        String email = etemail.getText().toString().trim();
        String password = etpassword.getText().toString().trim();

        if(email.isEmpty()){
            etemail.setError("Email is required");
            etemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etemail.setError("please provide valid email");
            etemail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            etpassword.setError("password is required");
            etpassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            etpassword.setError("password should be atleast 6 characters");
            etpassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(Authentication.this,"User Created", Toast.LENGTH_SHORT).show();
                userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                DocumentReference documentReference = fstore.collection("users").document(userID);

                Map<String,Object> user = new HashMap<>();
                user.put("First Name",firstName);
                user.put("Last name",lastName);
                user.put("ID",id);
                user.put("Phone No.",phno);
                user.put("Email",email);
                documentReference.set(user).addOnSuccessListener(unused -> Log.d(TAG1,"onSuccess: user profile is created for"+userID)).addOnFailureListener(e -> Log.d(TAG1,"OnFailure"+ e));
                startActivity(new Intent(getApplicationContext(),Login.class));
            } else{
                  Toast.makeText(Authentication.this,"Error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}