package com.example.appfortool_iba;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Sell_item
{
    private String userID;
    private String name;
    private int price;
    private int purchase_price;
    private boolean flag;
    Context mContext;
    private LinkedList<HashMap<String,Object>> hashMap=new LinkedList<>();

    int check;

    DatabaseReference mRefOfPart;
    public Sell_item(String userID,Context mContext){
        this.userID=userID;
             flag=false;
             mRefOfPart=null;
             this.mContext = mContext;
    }

    public Sell_item(String userID,String name, String price) {
        this.userID=userID;
        this.name = name;
        this.price = Integer.parseInt(price);

    }

    public void setCheckTrue()
    {
        check=1;
    }
    public void setCheckFalse()
    {
        check=0;
    }
    public int getCheck()
    {
        return check;
    }

    public void sellProduct()
    {
        if (price >= purchase_price )
        {
            goToProfit();

        }
        else
        {
            //goToLoss();
        }
    }

    private void goToProfit()
    {

    }



    void setHashMap(HashMap<String,Object> map)
    {
        hashMap.add(map);
    }

    public void ItemExist(final String part)
    {

        final boolean[] bool = new boolean[1];
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("User").child(userID);

        DatabaseReference partsReference = reference.child("Parts");

        partsReference.limitToFirst(1).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    for (DataSnapshot ds : snapshot.getChildren())
                    {


                        String name=ds.child("name_of_parts").getValue(String.class);
                        name.toUpperCase();
                        if (part.toUpperCase().equals(name))
                        {
                            setFlagTrue();
                            Toast.makeText(mContext,"Equal "+String.valueOf(isFlag()) ,Toast.LENGTH_LONG).show();

                            Log.d(TAG,"True...."+String.valueOf(bool[0]));


                        }
                    }


//                        //Log.d(TAG,ds.toString());
//                        HashMap<String,Object> map=new HashMap<>();
//
//                        String na =  ds.child("name_of_parts").getValue(String.class);
//                        String part =   ds.child("Number_of_items").getValue(String.class);
//                        String sell =    ds.child("sell").getValue(String.class);
//                        String purchase =   ds.child("purchase_price").getValue(String.class);
//                        map.put("Number_of_items",part);
//                        //Log.d(TAG,ds.child("Number_of_items").getValue(String.class).toString());
//                        map.put("name_of_parts",na);
//                        //Log.d(TAG,ds.child("name_of_parts").getValue(String.class).toString());
//                        map.put("sell",sell);
//                       // Log.d(TAG,ds.child("sell").getValue(String.class).toString());
//                        map.put("purchase_price",purchase);
//                        //Log.d(TAG,ds.child("purchase_price").getValue(String.class).toString());
//                   hashMap.add(map);











            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Log.d(TAG,String.valueOf(bool[0]));
////        Toast.makeText(mContext,"Return "+String.valueOf(isFlag()),Toast.LENGTH_LONG).show();
////        return isFlag();
//        return bool[0];
    }

    public void PrintHashMap()
    {
        Log.d(TAG,String.valueOf(hashMap.size()));
        HashMap<String,Object> map=new HashMap<>();
        for (int i=0;i<hashMap.size();i++)
        {
            map=hashMap.get(i);
            for (Map.Entry<String, Object> pair : map.entrySet())
            {
                Log.d(TAG,"key "+pair.getKey()+" values "+pair.getValue());
            }
        }


    }

    public boolean getValue(String part)
    {
        try {

            ItemExist(part);
        }catch (Exception e)
        {
            Log.d(TAG,e.toString());

        }

        return isFlag();
    }


    public void setFlagTrue()
    {
        flag=true;
    }

    private void setFlagFalse()
    {
        flag=false;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void ItemsExist()
    {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(int purchase_price) {
        this.purchase_price = purchase_price;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public DatabaseReference getmRefOfPart() {
        return mRefOfPart;
    }

    public void setmRefOfPart(DatabaseReference mRefOfPart) {
        this.mRefOfPart = mRefOfPart;
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public class Set_Hash
    {

        private String Number_of_items, name_of_parts ,sell ,purchase_price;

        public Set_Hash()
        {

        }
        public Set_Hash(String number_of_items, String name_of_parts, String sell, String purchase_price) {
            Number_of_items = number_of_items;
            this.name_of_parts = name_of_parts;
            this.sell = sell;
            this.purchase_price = purchase_price;


        }



        public String getNumber_of_items() {
            return Number_of_items;
        }

        public void setNumber_of_items(String number_of_items) {
            Number_of_items = number_of_items;
        }

        public String getName_of_parts() {
            return name_of_parts;
        }

        public void setName_of_parts(String name_of_parts) {
            this.name_of_parts = name_of_parts;
        }

        public String getSell() {
            return sell;
        }

        public void setSell(String sell) {
            this.sell = sell;
        }

        public String getPurchase_price() {
            return purchase_price;
        }

        public void setPurchase_price(String purchase_price) {
            this.purchase_price = purchase_price;
        }


    }

}
