package com.example.appfortool_iba;


/***
 *
 * **********************          AUTHER OF APPLICATION       **********************
 * *******************           MR. GHULAM HUSSAIN JOYO       *******************
 * ****************            Contact No: 03068237508         *****************
 *
 *
 */


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.GridView;

import java.util.LinkedList;

public class main_layout_for_action extends AppCompatActivity
{
    public static DataBase_Of_Parts mDataBase;

    private int count=1;

    static GridView gridView;
  //  AlertController.RecycleListView
    static LinkedList<String[]> parts_list=new LinkedList<String[]>();
    static PartsAdapter partsAdapter;

    private static String userID;
    private static Context myContext ;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout_for_action);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myContext = getApplicationContext();

       // num 1 index of Table is  Name Of parts
        // num 2 index of Table is  sell price
        // num 3 index of Table is  purchase price
        // num 4 index of Table is  sold Items
        // num 5 index of Table is  Number of items

        // num 1 index of array is  Name Of parts
        // num 2 index of array is  sell price
        // num 3 index of array is  Number of items

        gridView=(GridView) findViewById(R.id.gridview);

            Intent intent = getIntent();

            userID = intent.getStringExtra("userId");


            addData();




    }

    public void addData()
    {
        if (parts_list.size() == 0)
        {

            DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("User").child(userID);
            user.child("Parts").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot items : snapshot.getChildren())
                    {
                        String[]  arr = new String[4];

                        arr[0] = items.child("name_of_parts").getValue(String.class);
                        arr[2] = items.child("sell").getValue(String.class);
                        arr[3] = items.child("Number_of_items").getValue(String.class);

                        parts_list.add(arr);


                    }
                    partsAdapter=new PartsAdapter(getApplicationContext(),parts_list);
                    gridView.setAdapter(partsAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
//

//        {
//            parts_list.clear();
//            DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("User").child(userID);
//            user.child("Parts").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot items : snapshot.getChildren())
//                    {
//                        String[]  arr = new String[4];
//
//                        arr[0] = items.child("name_of_parts").getValue(String.class);
//                        arr[2] = items.child("sell").getValue(String.class);
//                        arr[3] = items.child("Number_of_items").getValue(String.class);
//
//                        parts_list.add(arr);
//
//
//                    }
//                    partsAdapter=new PartsAdapter(getApplicationContext(),parts_list);
//                    gridView.setAdapter(partsAdapter);
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }
    }

    public static void Update()
    {
        parts_list.clear();
        DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("User").child(userID);
        user.child("Parts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot items : snapshot.getChildren())
                {
                    String[]  arr = new String[4];

                    arr[0] = items.child("name_of_parts").getValue(String.class);
                    arr[2] = items.child("sell").getValue(String.class);
                    arr[3] = items.child("Number_of_items").getValue(String.class);

                    parts_list.add(arr);


                }
                partsAdapter=new PartsAdapter(myContext,parts_list);
                gridView.setAdapter(partsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
