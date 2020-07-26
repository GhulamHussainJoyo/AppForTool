package com.example.appfortool_iba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashBoard extends AppCompatActivity {


    private TextView numberOfLossItems,personNameDashbord,personNumberDashbord,lossPriceDashbord,sellProgressText,itemProgressText,profitProgressText;
    private Button addItemBtn,sellItemBtn,item,loss,profit,sell,addNewItemsBtn,go_btn;
    private ProgressBar sellProgressBar,itemProgressBar,profitProgressBar;
    private ListView lossItemsListView;
    private Dialog epicDialog;
    private ImageView cancel_btn_image;
    private TextInputLayout nameOfSellingItem,priceOfSellingItem;


    FirebaseAuth mUser;
    DatabaseReference mRef;
    String userId;
    private  int flagOfUserdataExist;
   ProgressDialog progressDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*********************** initializing Text View  ***************************/

        numberOfLossItems=findViewById(R.id.NumberOfLossItems);
        personNameDashbord=findViewById(R.id.Dashboard_PersonName);
        personNumberDashbord=findViewById(R.id.Dashboard_PersonPhoneNumber);
        lossPriceDashbord=findViewById(R.id.Dashboard_lossPrice);
        sellProgressText=findViewById(R.id.sellProgressText);
        itemProgressText=findViewById(R.id.itemProgressText);
        profitProgressText=findViewById(R.id.profitprogressText);

        /*********************** initializing Buttons  ***************************/

        addItemBtn=findViewById(R.id.addItemsBtn);
        addNewItemsBtn=findViewById(R.id.addNewItemsBtn);
        sellItemBtn=findViewById(R.id.sellItemBtn);
        item=findViewById(R.id.items);
        loss=findViewById(R.id.loss);
        profit=findViewById(R.id.profit);
        sell=findViewById(R.id.sell);




        /*********************** initializing ProgressBar  ***************************/


        sellProgressBar=findViewById(R.id.sell1_progressBar);
        itemProgressBar=findViewById(R.id.item_progressBar);
        profitProgressBar=findViewById(R.id.profit_progressBar);



        /*********************** initializing ListView  ***************************/

        lossItemsListView=findViewById(R.id.lossItemsInListView);


        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRef=FirebaseDatabase.getInstance().getReference("User");


        /*********************** initializing Dialog Box  ***************************/
        epicDialog =new Dialogg(this);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading....");



        setProfileName_Number(personNameDashbord,personNumberDashbord);
        checkFlagForItems();

       // Toast.makeText(getApplicationContext(),userId,Toast.LENGTH_LONG).show();




        addNewItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this,add_Items_For_Parts.class)
                        .putExtra("userID",userId)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"GO to Button",Toast.LENGTH_LONG).show();
                showSellDialogBox();
            }
        });

        sellItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sell_item sell_item=new Sell_item(userId,getApplicationContext());

            }
        });

    }

    private void showSellDialogBox()
    {
        final Dialog epiDailog=new Dialog(this);
        epiDailog.setContentView(R.layout.sell_dailog);
        go_btn = epiDailog.findViewById(R.id.GO_btn);
        cancel_btn_image =  epiDailog.findViewById(R.id.cancel_button_Image);

        nameOfSellingItem=epiDailog.findViewById(R.id.nameOfSellingItem);
        priceOfSellingItem=epiDailog.findViewById(R.id.priceOfSellingItem);

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

                String name = nameOfSellingItem.getEditText().getText().toString();
               String price = priceOfSellingItem.getEditText().getText().toString();

               Sell_item sellFirebase=new Sell_item(userId,getApplicationContext());

              if (TextUtils.isEmpty(name))
              {
                  nameOfSellingItem.setError("Empty");
                  priceOfSellingItem.setError("Empty");
              }
              else if (TextUtils.isEmpty(name) )
              {
                  nameOfSellingItem.setError("Empty");
                  priceOfSellingItem.setErrorEnabled(false);
                  priceOfSellingItem.setError(null);
              }
              else if ( TextUtils.isEmpty(price))
              {
                  priceOfSellingItem.setError("Empty");
                  nameOfSellingItem.setErrorEnabled(false);
                  nameOfSellingItem.setError(null);
              }
              else if(sellFirebase.getValue(name))
              {
                  nameOfSellingItem.setError("Not Exist");
                  priceOfSellingItem.setErrorEnabled(false);
                  priceOfSellingItem.setError(null);
              }
              else
              {
                  //Sell_item update=new Sell_item(userId,name,price);
                  //update.sellProduct();
                  Toast.makeText(getApplicationContext(),"ho gaya ",Toast.LENGTH_LONG).show();
                  epiDailog.dismiss();
              }




            }
        });

        epiDailog.show();

    }

    private void checkShortItems()
    {

    }



    private void setSell_Items_Max_Progressbar()
    {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userId).child("total").exists())
                {

                    int total = snapshot.child(userId).child("total").getValue(Integer.class);


                    sellProgressBar.setMax(total);
                    itemProgressBar.setMax(total);

                    Toast.makeText(getApplicationContext(),String.valueOf(total),Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Total not Exist",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void checkFlagForItems()
    {
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



    private boolean checkForLoss()
    {

        final boolean[] bol={false};
        mRef.child(userId).child("loss").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total=0;
                float price=0;
                if (snapshot.exists())
                {
                    for (DataSnapshot i:snapshot.getChildren())
                    {
                        total++;
                        // this is total price of loss Items
                        price+=i.child("price").getValue(Float.class);

                    }


                    lossPriceDashbord.setText(String.valueOf(price));
                    numberOfLossItems.setText(String.valueOf(total));
                }
                else
                {
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

    private void setProfit_MaxProgressBar()
    {
        mRef.child(userId).child("Parts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int profit=0;
                for (DataSnapshot childs : snapshot.getChildren())
                {
                    int purchase = Integer.parseInt(childs.child("purchase_price").getValue(String.class));
                    int sell = Integer.parseInt(childs.child("sell").getValue(String.class));

                    if (purchase<sell)
                    {
                        profit+=sell-purchase;
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

    private void checkForSell_ItemProgressBar()
    {
           final int[] total=new int[1];
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int max=0;

                        if (snapshot.child(userId).child("sell").exists())
                        {
                            int txt=snapshot.child(userId).child("sell").getValue(Integer.class);
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

    private void setProfileName_Number(final TextView userName, final TextView userNumber)
    {
        progressDialog.show();
        final String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
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
}