package com.iamaravind.datasaveandauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {


    Firebase mFire;
    FirebaseAuth mAuth;
    DatabaseReference mFiredb;

    EditText te1,te2,te3;
    Button savebtn, tomap;
    String t1,t2,t3, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        te1 = (EditText)findViewById(R.id.text1);
        te2 = (EditText)findViewById(R.id.text2);
        te3 = (EditText)findViewById(R.id.text3);

        savebtn =(Button)findViewById(R.id.save);
        tomap = (Button)findViewById(R.id.map);

        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mFire = new Firebase("https://datasaveandauth.firebaseio.com/User/"+uid);

        mFiredb = FirebaseDatabase.getInstance().getReference().child("User").child(uid);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveData();
                Toast.makeText(HomeActivity.this, "User ID = "+uid, Toast.LENGTH_SHORT).show();
            }
        });

        tomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tomap = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(tomap);
            }
        });
    }

    private void saveData() {
        t1 = te1.getText().toString();
        t2 = te2.getText().toString();
        t3 = te3.getText().toString();

        /*Map userInfo = new HashMap();
        userInfo.put("name", t1);
        userInfo.put("phone", t2);
        userInfo.put("college", t3);
        mFiredb.updateChildren(userInfo);*/
        Firebase myNewChild = mFire.child("name");
        myNewChild.setValue(t1);
        Firebase myNewChild1 = mFire.child("phone");
        myNewChild1.setValue(t2);
        Firebase myNewChild2 = mFire.child("college");
        myNewChild2.setValue(t3);
    }
}
