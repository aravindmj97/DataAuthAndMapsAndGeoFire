package com.iamaravind.datasaveandauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText name, pwd;
    Button sub, log;

    FirebaseAuth mAuth;
    String ename, epwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        name = (EditText)findViewById(R.id.uname);
        pwd = (EditText)findViewById(R.id.passwd);

        sub = (Button)findViewById(R.id.save);
        log = (Button)findViewById(R.id.login);


        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ename = name.getText().toString();
                epwd = pwd.getText().toString();
                CreateUser();
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ename = name.getText().toString();
                epwd = pwd.getText().toString();
                LoginUser();
            }
        });

    }
    private void CreateUser(){
        mAuth.createUserWithEmailAndPassword(ename,epwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Intent home = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(home);
                }
                else {
                    Toast.makeText(MainActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void LoginUser(){
        mAuth.signInWithEmailAndPassword(ename,epwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent home = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(home);
                }
                else {
                    Toast.makeText(MainActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
