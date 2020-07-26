package com.example.appfortool_iba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static com.example.appfortool_iba.main_layout_for_action.gridView;
import static com.example.appfortool_iba.main_layout_for_action.partsAdapter;
import static com.example.appfortool_iba.main_layout_for_action.parts_list;


public class add_Items_For_Parts extends AppCompatActivity
{

    String userID;
    private EditText nameOfPartsForInput1,nameOfPartsForInput2,nameOfPartsForInput3,nameOfPartsForInput4;
    private TextInputLayout nameOfPartsForInput1Layout,nameOfPartsForInput2Layout,nameOfPartsForInput3Layout,nameOfPartsForInput4Layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__items__for__parts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent userintent=getIntent();
        userID=userintent.getStringExtra("userID");

        nameOfPartsForInput1Layout= findViewById(R.id.nameOfPartsForInput1Layout);
        nameOfPartsForInput2Layout= findViewById(R.id.nameOfPartsForInput2Layout);
        nameOfPartsForInput3Layout= findViewById(R.id.nameOfPartsForInput3Layout);
        nameOfPartsForInput4Layout= findViewById(R.id.nameOfPartsForInput4Layout);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String nameOfPart=nameOfPartsForInput1Layout.getEditText().getText().toString();
                String purcchase=nameOfPartsForInput2Layout.getEditText().getText().toString();
                String sell=nameOfPartsForInput3Layout.getEditText().getText().toString();
                String numberOfItems=nameOfPartsForInput4Layout.getEditText().getText().toString();

                if (TextUtils.isEmpty(nameOfPart) || TextUtils.isEmpty(purcchase) || TextUtils.isEmpty(sell) || TextUtils.isEmpty(numberOfItems))
                {
                    nameOfPartsForInput1Layout.setError("This can not be Empty");
                    nameOfPartsForInput2Layout.setError("This can not be Empty");
                    nameOfPartsForInput3Layout.setError("This can not be Empty");
                    nameOfPartsForInput4Layout.setError("This can not be Empty");

                }
                else if (nameOfPart.length() <= 2)
                {
                    nameOfPartsForInput1Layout.setError("Length greater than 2");

                    nameOfPartsForInput2Layout.setError(null);
                    nameOfPartsForInput2Layout.setErrorEnabled(false);

                    nameOfPartsForInput3Layout.setError(null);
                    nameOfPartsForInput3Layout.setErrorEnabled(false);

                    nameOfPartsForInput4Layout.setError(null);
                    nameOfPartsForInput4Layout.setErrorEnabled(false);
                }
                else if (Integer.parseInt(sell) == 0 || Integer.parseInt(purcchase) == 0 || Integer.parseInt(numberOfItems) == 0 )
                {
                    nameOfPartsForInput1Layout.setError(null);
                    nameOfPartsForInput1Layout.setErrorEnabled(false);
                    nameOfPartsForInput2Layout.setError("value can not be zero");
                    nameOfPartsForInput3Layout.setError("value can not be zero");
                    nameOfPartsForInput4Layout.setError("value can not be zero");
                }
                else
                {
                    nameOfPartsForInput1Layout.setError(null);
                    nameOfPartsForInput1Layout.setErrorEnabled(false);

                    nameOfPartsForInput2Layout.setError(null);
                    nameOfPartsForInput2Layout.setErrorEnabled(false);

                    nameOfPartsForInput3Layout.setError(null);
                    nameOfPartsForInput3Layout.setErrorEnabled(false);

                    nameOfPartsForInput4Layout.setError(null);
                    nameOfPartsForInput4Layout.setErrorEnabled(false);



                        if (AddDataIntoFirebase(nameOfPart,purcchase,sell,numberOfItems) == true)
                        {
                            startActivity(new Intent(add_Items_For_Parts.this,DashBoard.class).putExtra("while","true"));
                            finish();
                        }
                        else
                        {

                            Toast.makeText(add_Items_For_Parts.this,"Not added into Database",Toast.LENGTH_LONG).show();
                        }


                }


            }
        });
    }
    private boolean AddDataIntoFirebase(final String name, final String purchase_price, final String sell, final String total)
    {

        final boolean[] check={true,true,true,true};
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference("User");
        final DatabaseReference userDataRefernce=mRef.child(userID);

        final HashMap<String,Object> map=new HashMap<>();
        map.put("name_of_parts",name);
        map.put("purchase_price",purchase_price);
        map.put("sell",sell);
        map.put("Number_of_items",total);

        userDataRefernce.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("flag").exists())
                {

                    int temp=snapshot.child("flag").getValue(Integer.class);

                    userDataRefernce.child("Parts").child(String.valueOf(temp)).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful())
                           {
                               check[0]=true;
                           }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            showMessage("error",e.toString());
                        }
                    });

                    int inc=temp;
                    inc++;


                    userDataRefernce.child("flag").setValue(inc).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                check[1]=true;
                            }
                            else
                            {

                                showMessage("flag","not changed");
                            }
                        }
                    });

                }
                else
                {
                    showMessage("flag","no Exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        userDataRefernce.child("total").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    int temp=snapshot.getValue(Integer.class);
                    temp+=Integer.parseInt(total);
                    userDataRefernce.child("total").setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                check[2]=true;
                            }
                            else
                            {
                                showMessage("total","no Exist");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            showMessage("error",e.toString());
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        userDataRefernce.child("purchase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    int temp=snapshot.getValue(Integer.class);
                    temp+=Integer.parseInt(total);
                    userDataRefernce.child("purchase").setValue(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                check[3]=true;
                            }
                            else
                            {
                                check[3]=false;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            showMessage("error",e.toString());
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        if (check[0] == true && check[1] == true && check[2] == true && check[3] == true)
        {

            return true;

        }
        else
        {

            showMessage("return Else","Ye False return kar raha ha");
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


}
