package com.example.appfortool_iba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class DashBoard extends AppCompatActivity {


    private TextView numberOfLossItems, personNameDashbord, personNumberDashbord, lossPriceDashbord, sellProgressText, itemProgressText, profitProgressText;
    private Button addItemBtn, sellItemBtn, item, loss, profit, sell, addNewItemsBtn, go_btn;
    private ProgressBar sellProgressBar, itemProgressBar, profitProgressBar;
    private ListView lossItemsListView;
    private Dialog epicDialog;
    private ImageView cancel_btn_image;
    private TextInputLayout nameOfSellingItem, priceOfSellingItem;
    private EditText flagEditText;



    FirebaseAuth mUser;
    DatabaseReference mRef;
    String userId;
    private int flagOfUserdataExist;
    ProgressDialog progressDialog;

    Add_Data_Into_SQL_From_Firebase sql_from_firebase;

    DataBase_Of_Parts database;
    public static Context myContext;

    public DashBoard() {
    }


    public static Context getMyContext()
    {
        return myContext;
    }
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_with_scrolldown_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myContext=getApplicationContext();


        /*********************** initializing RefreshLayout   ***************************/

         final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipeRefreshLayout);
         pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 checkFlagForItems();
                 pullToRefresh.setRefreshing(false);
             }
         });


        /*********************** initializing Text View  ***************************/

        numberOfLossItems = findViewById(R.id.NumberOfLossItems);
        personNameDashbord = findViewById(R.id.Dashboard_PersonName);
        personNumberDashbord = findViewById(R.id.Dashboard_PersonPhoneNumber);
        lossPriceDashbord = findViewById(R.id.Dashboard_lossPrice);
        sellProgressText = findViewById(R.id.sellProgressText);
        itemProgressText = findViewById(R.id.itemProgressText);
        profitProgressText = findViewById(R.id.profitprogressText);

        /*********************** initializing Buttons  ***************************/

        addItemBtn = findViewById(R.id.addItemsBtn);
        addNewItemsBtn = findViewById(R.id.addNewItemsBtn);
        sellItemBtn = findViewById(R.id.sellItemBtn);
        item = findViewById(R.id.items);
        loss = findViewById(R.id.loss);
        profit = findViewById(R.id.profit);
        sell = findViewById(R.id.sell);


        /*********************** initializing ProgressBar  ***************************/


        sellProgressBar = findViewById(R.id.sell1_progressBar);
        itemProgressBar = findViewById(R.id.item_progressBar);
        profitProgressBar = findViewById(R.id.profit_progressBar);


        /*********************** initializing ListView  ***************************/

        lossItemsListView = findViewById(R.id.lossItemsInListView);


        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference("User");


        /*********************** initializing Dialog Box  ***************************/
        epicDialog = new Dialogg(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");

        /*********************** initializing  Edit Text ***************************/

        flagEditText = findViewById(R.id.flag);
        flagEditText.setText("False");


        /*********************** initializing  Database from Firebase  ***************************/

//        sql_from_firebase = new Add_Data_Into_SQL_From_Firebase(userId, getApplicationContext());
//        sql_from_firebase.AddDataFromFirebase();
//        sql_from_firebase.showAllTablesData("Parts");





















        setProfileName_Number(personNameDashbord, personNumberDashbord);
        checkFlagForItems();

        // Toast.makeText(getApplicationContext(),userId,Toast.LENGTH_LONG).show();


        addNewItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this, add_Items_For_Parts.class)
                        .putExtra("userID", userId)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "GO to Button", Toast.LENGTH_LONG).show();
                showSellDialogBox();
            }
        });

        sellItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSellDialogBox();


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setProfileName_Number(personNameDashbord,personNumberDashbord);
    }

    private void showSellDialogBox() {
        final Dialog epiDailog = new Dialog(this);
        epiDailog.setContentView(R.layout.sell_dailog);
        go_btn = epiDailog.findViewById(R.id.GO_btn);
        cancel_btn_image = epiDailog.findViewById(R.id.cancel_button_Image);

        nameOfSellingItem = epiDailog.findViewById(R.id.nameOfSellingItem);
        priceOfSellingItem = epiDailog.findViewById(R.id.priceOfSellingItem);

        epiDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        cancel_btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                epiDailog.dismiss();
            }
        });



        go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = nameOfSellingItem.getEditText().getText().toString();
                final String price = priceOfSellingItem.getEditText().getText().toString();



                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price)) {
                    nameOfSellingItem.setError("Empty");

                    priceOfSellingItem.setError("Empty");
                    priceOfSellingItem.requestFocus();

                    nameOfSellingItem.requestFocus();

                } else if (TextUtils.isEmpty(name)) {

                    nameOfSellingItem.setError("Empty");
                    priceOfSellingItem.setError(null);
                    priceOfSellingItem.setErrorEnabled(false);

                } else if (TextUtils.isEmpty(price)) {

                    priceOfSellingItem.setError("Empty");
                    priceOfSellingItem.requestFocus();
                    nameOfSellingItem.setError(null);
                    nameOfSellingItem.setErrorEnabled(false);

                }
                else {
                    final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

//                    final DatabaseReference referenceOfPartChild = mRef.child("Parts");

                    Query query = mRef.child("Parts").orderByChild("name_of_parts").equalTo(name);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists())
                            {
                                //Toast.makeText(DashBoard.this, snapshot.toString(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG,snapshot.toString());
                                priceOfSellingItem.setError(null);
                                priceOfSellingItem.setErrorEnabled(false);

                                nameOfSellingItem.setError(null);
                                nameOfSellingItem.setErrorEnabled(false);

                                int purchase_price = 0;
                              for(DataSnapshot fields : snapshot.getChildren())
                              {
                                    purchase_price = Integer.parseInt(fields.child("purchase_price").getValue(String.class));

                              }


                                if (Integer.parseInt(price) >= purchase_price)
                                {
                                    final int finalPurchase_price = purchase_price;
                                    final int finalPurchase_price1 = purchase_price;

                                    mRef.child("Profit").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists())
                                            {
                                                Query profit_Query = mRef.child("Profit").orderByChild("name_of_parts").equalTo(name);

                                                //  Query profit_Query = dbRef.orderByChild("name_of_parts").equalTo(name);

                                                profit_Query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.exists())
                                                        {
                                                            int profit = Integer.parseInt(price) - finalPurchase_price;

                                                            int price_temp=0;
                                                            String key="";
                                                            for (DataSnapshot pri : snapshot.getChildren())
                                                            {
                                                                price_temp = Integer.parseInt(pri.child("price").getValue().toString());
                                                                key = pri.getKey();
                                                            }

                                                           price_temp+=profit;

                                                            //String profiDatabaseRefernce = snapshot.getRef().toString();

                                                            mRef.child("Profit").child(key).child("price").setValue(String.valueOf(price_temp)).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                        @Override
                                                               public void onComplete(@NonNull Task<Void> task) {
                                                                   if (task.isSuccessful())
                                                                   {
                                                                       Toast.makeText(getMyContext(),"new Value of Price is Added to Profit",Toast.LENGTH_LONG).show();

                                                                       epiDailog.dismiss();

                                                                       checkFlagForItems();


                                                                   }
                                                               }
                                                           });
                                                        }
                                                        else
                                                        {
                                                            int profit = Integer.parseInt(price) - finalPurchase_price;

                                                            HashMap map = new HashMap();
                                                            map.put("name_of_parts",name);
                                                            map.put("price",String.valueOf(profit));

                                                            mRef.child("Profit").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(myContext,"map is added to profit",Toast.LENGTH_LONG).show();

                                                                    epiDailog.dismiss();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {

                                                                    Toast.makeText(myContext,"map is not added to profit",Toast.LENGTH_LONG).show();
                                                                    epiDailog.dismiss();
                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                            else
                                            {
                                                    int profit = Integer.parseInt(price) - finalPurchase_price1;

                                                    HashMap map = new HashMap();
                                                    map.put("name_of_parts",name);
                                                    map.put("price",String.valueOf(profit));

                                                    mRef.child("Profit").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(myContext,"map is added to profit",Toast.LENGTH_LONG).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                            Toast.makeText(myContext,"map is not added to profit",Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                  epiDailog.dismiss();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                else
                                {
                                    //// LOSSS In Items
                                }
                            }
                            else
                            {
                                nameOfSellingItem.setError("This Item is not available");
                                nameOfSellingItem.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }
        });

        epiDailog.show();

    }

    private void checkShortItems() {

    }



    private void setSell_Items_Max_Progressbar() {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userId).child("total").exists()) {

                    int total = snapshot.child(userId).child("total").getValue(Integer.class);


                    sellProgressBar.setMax(total);
                    itemProgressBar.setMax(total);

                    Toast.makeText(getApplicationContext(), String.valueOf(total), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Total not Exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkFlagForItems() {
        setSell_Items_Max_Progressbar();
        setProfit_MaxProgressBar();
        checkForLoss();
        checkForSell_ItemProgressBar();

        //checkForParts();
        //checkForShortItems();

    }

//    private void checkForShortItems()
//    {
//
//    }
//
//    private void checkForParts() {
//    }
//
//    boolean checkForSellItems()
//    {
//     return false ;
//    }


    private boolean checkForLoss() {

        final boolean[] bol = {false};
        mRef.child(userId).child("loss").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total = 0;
                float price = 0;
                if (snapshot.exists()) {
                    for (DataSnapshot i : snapshot.getChildren()) {
                        total++;
                        // this is total price of loss Items
                        price += i.child("price").getValue(Float.class);

                    }


                    lossPriceDashbord.setText(String.valueOf(price));
                    numberOfLossItems.setText(String.valueOf(total));
                } else {
                    lossPriceDashbord.setText("0.0");
                    numberOfLossItems.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return bol[0];
    }

    private void setProfit_MaxProgressBar() {
        mRef.child(userId).child("Parts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int profit = 0;
                for (DataSnapshot childs : snapshot.getChildren()) {
                    int purchase = Integer.parseInt(childs.child("purchase_price").getValue(String.class));
                    int sell = Integer.parseInt(childs.child("sell").getValue(String.class));

                    if (purchase < sell) {
                        profit += sell - purchase;
                    }
                }

                profitProgressBar.setMax(profit);
                profitProgressBar.setProgress(profit);
                profitProgressText.setText(String.valueOf(profit));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkForSell_ItemProgressBar() {
        final int[] total = new int[1];
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int max = 0;

                if (snapshot.child(userId).child("sell").exists()) {
                    int txt = snapshot.child(userId).child("sell").getValue(Integer.class);
                    sellProgressText.setText(String.valueOf(txt));
                    sellProgressBar.setProgress(txt);


                }


                if (snapshot.child(userId).child("purchase").exists()) {

                    int txt = snapshot.child(userId).child("purchase").getValue(Integer.class);
                    itemProgressText.setText(String.valueOf(txt));
                    itemProgressBar.setProgress(txt);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


//    private void setProgressBarStartUp()
//    {
//        sellProgressBar.setMax(0);
//        sellProgressBar.setProgress(0);
//        sellProgressText.setText("0");
//
//        purchaseProgressBar.setMax(0);
//        purchaseProgressBar.setProgress(0);
//        purchaseProgressText.setText("0");
//
//        totalProgressBar.setMax(0);
//        totalProgressBar.setProgress(0);
//        totalProgressText.setText("0");
//
//    }

    private void setProfileName_Number(final TextView userName, final TextView userNumber) {
        progressDialog.show();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();



       // Cursor res = sql_from_firebase.showAllTablesData("Parts");
//        DataBase_Of_Parts db=new DataBase_Of_Parts(getMyContext());
//        Cursor res = db.showAllTablesData("user_data");
//        if (res.getCount()==0)
//        {
//            showMessage("Error","Nothing Found");
//            return;
//        }
//        else
//        {
//            StringBuffer stringBuffer=new StringBuffer();
//            while (res.moveToNext())
//            {
//                stringBuffer.append("id : "+ res.getString(0)+"\n");
//                stringBuffer.append("user_id : "+ res.getString(1)+"\n");
//                stringBuffer.append("name : "+ res.getString(2)+"\n");
//                stringBuffer.append("email : "+ res.getString(3)+"\n");
//                stringBuffer.append("phoneNumber : "+ res.getString(4)+"\n\n");
//            }
//
//            showMessage("Data",stringBuffer.toString());
//        }
//        progressDialog.dismiss();
//
//        Cursor res = database.showAllUserData(userId);
//        if (res.getCount() == 0) {
//            showMessage("Error", "Emty DataBasse");
//            return;
//
//        }
//        else
//        {
//            res.moveToNext();
//            userName.setText(res.getString(2));
//            userNumber.setText(res.getString(4));
//
//        }



        FirebaseDatabase.getInstance().getReference("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name=snapshot.child(userId).child("user_data").child("name").getValue(String.class);
                String number=snapshot.child(userId).child("user_data").child("phoneNumber").getValue(String.class);

                userName.setText(name);
                userNumber.setText(number);
                progressDialog.dismiss();
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