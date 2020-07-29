package com.example.appfortool_iba;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;


public class Sell_item
 {
    private String userID;
    private String name;
    private int price;
    private int purchase_price;
    private boolean flag;
    Context mContext;
    EditText text;

    boolean methodChecker=true;

    DatabaseReference mRefOfPart;
     private Object Sell_item;

     public Sell_item(String userID, Context mContext, EditText flagEditText){
        this.userID=userID;
             flag=false;
             mRefOfPart=null;
             this.mContext = mContext;
    }

    public Sell_item(String userID,String name, String price,EditText txt) {
        this.userID=userID;
        this.name = name;
        this.price = Integer.parseInt(price);
        this.text = txt;

    }


    int loo=0;





//    public  Sell_item state()
//    {
//
//
//        Thread t1 = new Thread(new Runnable() {
//            @Override
//            public synchronized void run() {
//                getState();
//            }
//        });
//        Thread t2=new Thread(new Runnable() {
//            @Override
//            public synchronized void run() {
//
//                getState();
//            }
//        });
//
//
//        Sell_item sell_item1 = new Sell_item(userID,mContext);
//        sell_item1.state();
//        return sell_item1;
//    }


//    public synchronized void getState()
//    {
//
//        ItemExist(new MyCallBack() {
//
//            public void onCallBack(boolean value) {
//
//                Log.d(TAG,"value  "+String.valueOf(value));
//                Log.d(TAG,"flag  "+String.valueOf(isFlag()));
//                setFlagTrue();
//
//            }
//        });
//
//    }

     public  void ItemExist(final MyCallBack myCallBack, String part)
     {
         DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("User").child(userID).child("Parts");
         Query query = mRef.orderByChild("name_of_parts").equalTo(part);

         query.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {

                 if (snapshot.exists())
                 {
                     setFlagTrue();
                    myCallBack.onCallBack(true);
                     Log.v(TAG,"...........................................This is inside Add event listener");
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
         Log.v(TAG,"................................................................This is out side of  Add event listener");


     }

     public boolean getData(String part)
     {
         Log.i(TAG,"....................................... start getData");
         ItemExist(new MyCallBack() {
             @Override
             public void onCallBack(boolean value) {

                 setFlagTrue();

                 Log.i(TAG,"....................................... callback");

                 text.setText("True");
             }
         }, part);
         Log.i(TAG,"....................................... getData");
        return false;
     }


     public boolean isFlag() {
         return flag;
     }

     public void setFlagFalse() {
         this.flag = false;
     }
     public void setFlagTrue() {
         this.flag = true;
     }
 }
