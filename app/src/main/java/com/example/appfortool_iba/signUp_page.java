package com.example.appfortool_iba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signUp_page extends AppCompatActivity {

    private DataBase_Of_Parts mDataBase;
    private TextInputLayout mFulName,mEmail,mPassword,mPhoneNumber;
    private TextView signIn_btn_PageOfSignUP;

    private Button signUP;

    FirebaseAuth mAuth;
    DatabaseReference mReference;




    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mAuth= FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference();



        pd=new ProgressDialog(this);




        final String[] userDataArray=new String[3];

        mDataBase=new DataBase_Of_Parts(getApplicationContext());

        mFulName= findViewById(R.id.name_of_user);

        mEmail=findViewById(R.id.email);

        mPassword=findViewById(R.id.enter_password);

        signUP=findViewById(R.id.signUP_btn);




        signIn_btn_PageOfSignUP=findViewById(R.id.sign_IN_OfSignUpPage);





        signIn_btn_PageOfSignUP.setOnClickListener(new View.OnClickListener()
        {


            public void onClick(View v)
            {
                startActivity(new Intent(signUp_page.this,login.class));
            }
        });






        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String fullname=mFulName.getEditText().getText().toString().trim();
                final String email=mEmail.getEditText().getText().toString().trim();
                final String password=mPassword.getEditText().getText().toString().trim();
                final String phoneNumber=mPhoneNumber.getEditText().getText().toString().trim();

                if(!TextUtils.isEmpty(fullname) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(phoneNumber))

                {
                    if (checkEmailValidation(email))
                    {
                        if(password.length() < 4)
                        {
                            Toast.makeText(getApplicationContext(),"Very weak Password "+userDataArray[2].toString(),Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            RegisterUser(fullname,phoneNumber,email,password);
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Bande ka putar ban sahi mail laga",Toast.LENGTH_LONG).show();

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Text field tera baap bhare ga",Toast.LENGTH_LONG).show();
                }


            }
        });


    }



    public boolean checkEmailValidation(String mail)
    {

        try
        {

            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(mail);
            return matcher.matches();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }




    public void RegisterUser(final String fullName, final String phoneNumber, final String email, String password) {




        pd.setMessage("Please Wait...");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {

            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object> map=new HashMap<>();

                map.put("Id",mAuth.getCurrentUser().getUid());
                map.put("Name",fullName);
                map.put("PhoneNumber",phoneNumber);
                map.put("email",email);


                mReference.child("User").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),"Done ",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(signUp_page.this,MainActivity.class));



                        }




                    }


                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();

                showMessage("Error",e.getMessage());


            }
        });

    }
}
