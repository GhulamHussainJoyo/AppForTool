package com.example.appfortool_iba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {

    private final String tableName="USERDATA";

    private DataBase_Of_Parts mDatabase;

    // private LoginButton facebook_button;
    private Button facebook_login_btn_custom,signIN;
    private TextInputLayout mEmailLayout,mPasswordlayout;
    private TextView sign_up_textView;
    private EditText email,password;
    Add_Data_Into_SQL_From_Firebase  sql_from_firebase;

    FirebaseAuth mAuth;

    private int flag1=0,flag2=0;



    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);





        mAuth=FirebaseAuth.getInstance();
        HashMap<String , Object> map=new HashMap<>();
        map.put("Abbal","Joyo");
        map.put("MyParts","Ready");
        //FirebaseDatabase.getInstance().getReference().child("User").child("Multiple values").updateChildren(map);


        //FirebaseDatabase.getInstance().getReference().child("abbal").child("Anfroid").setValue("ABCD");

        pd=new ProgressDialog(this);


        final String[] loginArray=new String[2];
        final int[] temp=new int[4];




        mDatabase=new DataBase_Of_Parts(this);

        signIN=findViewById(R.id.signIn_btn);

        email=findViewById(R.id.Email);
        password=findViewById(R.id.Password);

        mEmailLayout=findViewById(R.id.EmailTextInputLayout);
        mPasswordlayout=findViewById(R.id.PasswordTextInputLayout);


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {





            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s.toString()))
                {
                    mEmailLayout.setError("Emplty");
                }
                else if(!checkEmailValidation(s.toString()))
                {
                    mEmailLayout.setError("WRONG MAIL");
                }
                else
                {

                    String local=String.valueOf(s);
                    loginArray[0]=local;
                    mEmailLayout.setError(null);
                    flag1=1;

                }



            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s.toString()))
                {
                    mEmailLayout.setError("Emplty");
                }
                else if (s.toString().length() <= 3)
                {
                    mPasswordlayout.setError("Weak Password");
                }
                else
                {
                    mPasswordlayout.setError(null);
                    String local=String.valueOf(s);
                    loginArray[1]=local;
                    flag2=1;

                }
            }
        });


/*************************      SIGN IN METHOD                *********************************************/

        signIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (flag1==1 && flag2 == 1)
                {
                    loginUser(loginArray[0].toString(),loginArray[1].toString());
                    showMessage("Confirmation","Working");
                }
                else
                {
                    Toast.makeText(login.this,"please make sure you filled correctly",Toast.LENGTH_SHORT).show();
                }


            }
        });





        sign_up_textView=(TextView) findViewById(R.id.signUp_textview);

        sign_up_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),signUp_page.class);
                startActivity(intent);
            }
        });









    }

    public void onClickSIGNUP(View view)
    {
        if(view == sign_up_textView)
        {
            sign_up_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(getApplicationContext(),signUp_page.class);
                    startActivity(intent);
                }
            });
        }
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


    public void loginUser(String email,String password)
    {


//        StringBuffer stringBuffer=new StringBuffer();
//        stringBuffer.append(email+"\n");
//        stringBuffer.append(password);
//
//        showMessage("User login Data",stringBuffer.toString());
//        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();



         mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    startActivity(new Intent(getApplicationContext(),DashBoard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .putExtra("flag","1"));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(login.this,"Email or password is wrong",Toast.LENGTH_LONG).show();

            }
        });


    }
}
