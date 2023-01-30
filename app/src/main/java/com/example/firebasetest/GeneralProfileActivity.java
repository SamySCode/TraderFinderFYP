package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.firebasetest.Model.User;

public class GeneralProfileActivity extends AppCompatActivity {

    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mContactNumberEditText;
    private EditText mRegionEditText;
    private Button mSubmitButton;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_profile);

        mFirstNameEditText = (EditText) findViewById(R.id.edit_text_first_name);
        mLastNameEditText = (EditText) findViewById(R.id.edit_text_last_name);
        mContactNumberEditText = (EditText) findViewById(R.id.edit_text_contact_number);
        mRegionEditText = (EditText) findViewById(R.id.edit_text_region);
        mSubmitButton = (Button) findViewById(R.id.button_submit);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = mFirstNameEditText.getText().toString();
                String lastName = mLastNameEditText.getText().toString();
                String contactNumber = mContactNumberEditText.getText().toString();
                String region = mRegionEditText.getText().toString();
                String accountType = "general";

                Intent profileIntent = new Intent(GeneralProfileActivity.this, MainActivity.class);
                startActivity(profileIntent);

                saveUserInfo(firstName, lastName, contactNumber, region, accountType);

            }
        });
    }

    private void saveUserInfo(String firstName, String lastName, String contactNumber, String region, String accountType) {
        //Creates new User object
        User user = new User(firstName, lastName, contactNumber, region, accountType);

        // Get a reference to the "users" node in the database
        DatabaseReference usersRef = mDatabase.child("users");

        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Pushes data to unique id
        usersRef.child(currentUser.getUid()).setValue(user);

    }


    private FirebaseUser getCurrentUser() {

        return FirebaseAuth.getInstance().getCurrentUser();
    }
}