package com.example.appfortool_iba;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class Add_Data_Into_SQL_From_Firebase extends DataBase_Of_Parts
{
    String userID;
    Context mContext;
   // DataBase_Of_Parts dataBase_of_parts;

    DatabaseReference mRef=FirebaseDatabase.getInstance().getReference("User");
    public Add_Data_Into_SQL_From_Firebase(String userID, Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.userID = userID;
     //   dataBase_of_parts=new DataBase_Of_Parts(mContext);
    }


    public void AddDataFromFirebase()
    {

        Cursor cursor;
       Thread t1=new Thread(new Runnable() {
           @Override
           public void run() {
               Cursor cursor=getUserDataWithId(userID);
               {
                   if (cursor.getCount() == 0)
                   {
                       InsertUserData1();
                   }
                   else
                   {
                       Log.v(TAG,"Already Data Exist with that Id");
                   }
               }
           }
       });

        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
              Cursor cursor=getData(FirebaseAuth.getInstance().getCurrentUser().getUid(),"Parts");
                {
                    if (cursor.getCount() == 0)
                    {
                        InsertPartData1();
                    }
                    else
                    {
                        Log.v(TAG,"........................ Already Part Data Exist with that Id.............................................");
                    }
                }

            }
        });
        Thread t3=new Thread(new Runnable() {
            @Override
            public void run() {

                Cursor cursor=getData(userID,"Loss");
                {
                    if (cursor.getCount() == 0)
                    {
                        InsertLossData1();
                    }
                    else
                    {
                        Log.v(TAG,"........................ Already Loss Data Exist with that Id.............................................");
                    }
                }


            }
        });



        Thread t4=new Thread(new Runnable() {
            @Override
            public void run() {

                Cursor cursor=getData(userID,"Profit");
                {
                    if (cursor.getCount() == 0)
                    {
                        InsertProfitData1();
                    }
                    else
                    {
                        Log.v(TAG,"........................ Already Profit Data Exist with that Id.............................................");
                    }
                }





            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t3.join();
            t3.join();
            t4.join();
        }
        catch (Exception e)
        {
            Log.i(TAG,"join Exception..................................................."+e.toString());
        }




    }

    private synchronized void InsertUserData1()
    {
        DatabaseReference userRefernce  = mRef.child(userID);
        //Query userRefernce = mRef.orderByChild(userID);
        final String userTable = "user_data";
        userRefernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.child("user_data").exists())
                {
                    String user_id = snapshot.child("user_data").child("id").getValue().toString();
                    String name = snapshot.child("user_data").child("name").getValue().toString();
                    String email = snapshot.child("user_data").child("email").getValue().toString();
                    String phoneNumber = snapshot.child("user_data").child("phoneNumber").getValue().toString();


                    //myCallBack.onCallBack(user_id,name,email,phoneNumber);
                    if (InsertUserData(user_id,name,email,phoneNumber))
                    {

                        Log.d(TAG,"Data added into user table");


                    }
                    else
                    {
                        Log.d(TAG,"Data not added into user table");
                    }

                }
                else
                {
                    Toast.makeText(mContext,"Please Login UserSelf",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private synchronized void InsertPartData1()
    {
        final DatabaseReference user = mRef.child(userID).child("Parts");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    for (DataSnapshot data : snapshot.getChildren())
                    {
                        String Number_of_items = data.child("Number_of_items").getValue().toString();
                        String name_of_parts = data.child("name_of_parts").getValue().toString();
                        String purchase_price = data.child("purchase_price").getValue().toString();
                        String sell = data.child("sell").getValue().toString();
                        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();




                        if (InsertPartData(Number_of_items,name_of_parts,purchase_price,sell,user_id))
                        {
                            Log.v(TAG,"Parts Data Inserted ");
                        }
                        else
                        {
                            Log.v(TAG,"Parts Data not Inserted ");
                        }

                    }

                }
                else
                {
                    Toast.makeText(mContext,"Parts data not available",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private synchronized void InsertLossData1()
    {
        final DatabaseReference user = mRef.child(userID).child("Loss");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    for (DataSnapshot data : snapshot.getChildren())
                    {
                        String name = data.child("name_of_parts").getValue().toString();
                        String price = data.child("price").getValue().toString();
                        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        if (InsertLossData(name,price,user_id))
                        {
                            Log.v(TAG,"Loss Data Inserted ");
                        }
                        else
                        {
                            Log.v(TAG,"Loss Data not Inserted ");
                        }

                    }

                }
                else
                {
                    if (InsertLossData("0","0","0"))
                    {
                        Log.v(TAG,"Loss Data Inserted of else statement");
                    }
                    else
                    {
                        Log.v(TAG,"Loss Data not Inserted of else statement");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private synchronized void InsertProfitData1()
    {
        final DatabaseReference user = mRef.child(userID).child("Profit");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    for (DataSnapshot data : snapshot.getChildren())
                    {
                        String name = data.child("name_of_parts").getValue().toString();
                        String price = data.child("price").getValue().toString();

                        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        if (InserProfitData(name,price,user_id))
                        {
                            Log.v(TAG,"Profit Data Inserted ");
                        }
                        else
                        {
                            Log.v(TAG,"Profit Data not Inserted ");
                        }

                    }

                }
                else
                {
                    if (InserProfitData("0","0","0"))
                    {
                        Log.v(TAG,"profit Data Inserted of else statement");
                    }
                    else
                    {
                        Log.v(TAG,"profit Data not Inserted of else statement");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public interface MyCallBack {
        void onCallBack(String user_id,String name,String email,String phoneNumber);


    }

    public interface MyCallBackPart {
        void onCallBackOfPart(String numberOfparts, String nameOfpart, String purchase, String sell);
    }

    public interface MyCallBackLoss {
        void onCallBackOfPart(String nameOfpart,String price);
    }

    public interface MyCallBackLossProfit {
        void onCallBackOfPart(String nameOfpart,String price);
    }

}
