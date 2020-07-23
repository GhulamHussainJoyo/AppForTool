package com.example.appfortool_iba;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
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

                }


            }
        });
    }
    private boolean AddDataIntoFirebase(final String name, final String purchase, final String sell, final String noOfItems)
    {
        final boolean[] check = {false};
        DatabaseReference mRef=FirebaseDatabase.getInstance().getReference();
        DatabaseReference userDataRefernce=mRef.child("User").child(userID).push();
        HashMap<String,Object> map=new HashMap<>();
        map.put("nameofparts",name);
        map.put("purchase",purchase);
        map.put("sell",sell);
        map.put("numberofitems",noOfItems);

        userDataRefernce.child("Parts").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                check[0]=false;
                showMessage("error",e.toString());
            }
        });

        return check[0];
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
