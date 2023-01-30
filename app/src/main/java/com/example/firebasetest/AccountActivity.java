package com.example.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity {

    private ImageButton mGeneralAccountButton;
    private ImageButton mTradesmanAccountButton;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mGeneralAccountButton = (ImageButton) findViewById(R.id.generalUserButton);
        mTradesmanAccountButton = (ImageButton) findViewById(R.id.tradesmanButton);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mGeneralAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAccountType("general");
                Intent intent = new Intent(AccountActivity.this, GeneralProfileActivity.class);
                startActivity(intent);
            }
        });

        mTradesmanAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAccountType("tradesman");
                Intent intent = new Intent(AccountActivity.this, TradesmanProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setAccountType(String accountType) {
        mDatabase.child("users").child(getCurrentUser().getUid()).child("account_type").setValue(accountType);
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}