package com.example.appfortool_iba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashBoard extends AppCompatActivity {


    private TextView numberOfLossItems,personNameDashbord,personNumberDashbord,lossPriceDashbord,sellProgressText,purchaseProgressText,totalProgressText;
    private Button addItemBtn,sellItemBtn,item,loss,profit,sell;
    private ProgressBar sellProgressBar,purchaseProgressBar,totalProgressBar;
    private ListView lossItemsListView;


    FirebaseAuth mUser;
    private  int flagOfUserdataExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        purchaseProgressText=findViewById(R.id.purchaseProgressText);
        totalProgressText=findViewById(R.id.totalprogressText);

        /*********************** initializing Buttons  ***************************/

        addItemBtn=findViewById(R.id.addItemsBtn);
        sellItemBtn=findViewById(R.id.sellItemBtn);
        item=findViewById(R.id.items);
        loss=findViewById(R.id.loss);
        profit=findViewById(R.id.profit);
        sell=findViewById(R.id.sell);




        /*********************** initializing ProgressBar  ***************************/


        sellProgressBar=findViewById(R.id.sell_progressBar);
        purchaseProgressBar=findViewById(R.id.purchase_progressBar);
        totalProgressBar=findViewById(R.id.total_progressBar);



        /*********************** initializing ListView  ***************************/

        lossItemsListView=findViewById(R.id.lossItemsInListView);



        checkShortItems();

        checkFlagForItems();


        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this,add_Items_For_Parts.class)
                        .putExtra("userID",FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });



    }

    private void checkShortItems() {
    }

    private void checkFlagForItems()
    {
        if (getFlag() == 0)
        {
            setProgressBarStartUp();
            setProfileName_Number(personNameDashbord,personNumberDashbord);
            lossPriceDashbord.setText("0.0");
            numberOfLossItems.setText("0");
        }
        else
        {

        }


    }
    private int getFlag()
    {
        final int[] temp = new int[1];
        final String userID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                   temp[0]= snapshot.child(userID).child("flag").getValue(Integer.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return temp[0];

    }

    private void setProgressBarStartUp()
    {
        sellProgressBar.setMax(0);
        sellProgressBar.setProgress(0);
        sellProgressText.setText("0");

        purchaseProgressBar.setMax(0);
        purchaseProgressBar.setProgress(0);
        purchaseProgressText.setText("0");

        totalProgressBar.setMax(0);
        totalProgressBar.setProgress(0);
        totalProgressText.setText("0");

    }
    private void setProfileName_Number(final TextView userName, final TextView userNumber)
    {
        final String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child(userId).child("Name").getValue(String.class);
                String number=snapshot.child(userId).child("PhoneNumber").getValue(String.class);

                userName.setText(name.toUpperCase());
                userNumber.setText(number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}