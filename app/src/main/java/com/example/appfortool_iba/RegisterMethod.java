package com.example.appfortool_iba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterMethod extends AppCompatActivity {


    Button registerSignInBtn,registerSignUnBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_method);

        registerSignInBtn = findViewById(R.id.RegisterSignInBtn);

        registerSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterMethod.this,login.class));
                finish();
            }
        });


        registerSignUnBtn = findViewById(R.id.RegisterSignUpbtn);

        registerSignUnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterMethod.this,signUp_page.class));
                finish();
            }
        });
    }

    protected void onStart()
    {   super.onStart();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            startActivity(new Intent(RegisterMethod.this,DashBoard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }

    }
}
