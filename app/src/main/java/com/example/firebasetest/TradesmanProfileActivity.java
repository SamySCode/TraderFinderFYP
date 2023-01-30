package com.example.firebasetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebasetest.Model.Tradesman;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class TradesmanProfileActivity extends AppCompatActivity {

    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mContactNumberEditText;
    private EditText mRegionEditText;
    private ImageView mPastJobsImageView;
    private ImageView mCertificationsImageView;
    private Button mSubmitButton;

    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;

    private static final int PAST_JOBS_IMAGE_REQUEST_CODE = 1;
    private static final int CERTIFICATIONS_IMAGE_REQUEST_CODE = 2;

    private Uri mPastJobsImageUri;
    private Uri mCertificationsImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradesman_profile);

        mFirstNameEditText = (EditText) findViewById(R.id.edit_text_first_name);
        mLastNameEditText = (EditText) findViewById(R.id.edit_text_last_name);
        mContactNumberEditText = (EditText) findViewById(R.id.edit_text_contact_number);
        mRegionEditText = (EditText) findViewById(R.id.edit_text_region);
        mPastJobsImageView = (ImageView) findViewById(R.id.image_view_past_jobs);
        mPastJobsImageView.setVisibility(View.VISIBLE);
        mPastJobsImageView.setContentDescription("Tap to add Past Jobs");
        mCertificationsImageView = (ImageView) findViewById(R.id.image_view_certifications);
        mCertificationsImageView.setVisibility(View.VISIBLE);
        mCertificationsImageView.setContentDescription("Tap to add Certifications");
        mSubmitButton = (Button) findViewById(R.id.button_submit);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();

        mPastJobsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, PAST_JOBS_IMAGE_REQUEST_CODE);
            }
        });

        mCertificationsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, CERTIFICATIONS_IMAGE_REQUEST_CODE);
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = mFirstNameEditText.getText().toString();
                String lastName = mLastNameEditText.getText().toString();
                String contactNumber = mContactNumberEditText.getText().toString();
                String region = mRegionEditText.getText().toString();
                String accountType = "tradesman";

                if (firstName.isEmpty() || lastName.isEmpty() || contactNumber.isEmpty() || region.isEmpty()) {
                    Toast.makeText(TradesmanProfileActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mPastJobsImageUri == null || mCertificationsImageUri == null) {
                    Toast.makeText(TradesmanProfileActivity.this, "Please select images for past jobs and certifications", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent signUpIntent = new Intent(TradesmanProfileActivity.this, AccountActivity.class);
                startActivity(signUpIntent);
                uploadImagesToFirebaseStorage(firstName, lastName, contactNumber, region, accountType);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAST_JOBS_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mPastJobsImageUri = data.getData();
            mPastJobsImageView.setImageURI(mPastJobsImageUri);
        }

        if (requestCode == CERTIFICATIONS_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mCertificationsImageUri = data.getData();
            mCertificationsImageView.setImageURI(mCertificationsImageUri);
        }
    }

    private void uploadImagesToFirebaseStorage(final String firstName, final String lastName, final String contactNumber, final String region, final String accountType) {
        StorageReference pastJobsRef = mStorage.getReference().child("tradesman_profiles").child(UUID.randomUUID().toString());
        StorageReference certificationsRef = mStorage.getReference().child("tradesman_profiles").child(UUID.randomUUID().toString());

        pastJobsRef.putFile(mPastJobsImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pastJobsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String pastJobsImageUrl = uri.toString();
                                certificationsRef.putFile(mCertificationsImageUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                certificationsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String certificationsImageUrl = uri.toString();
                                                        saveTradesmanInfo(firstName, lastName, contactNumber, region, pastJobsImageUrl, certificationsImageUrl, accountType);
                                                    }
                                                });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(TradesmanProfileActivity.this, "Error uploading certifications image", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TradesmanProfileActivity.this, "Error uploading past jobs image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveTradesmanInfo(String firstName, String lastName, String contactNumber, String region, String pastJobsImageUrl, String certificationsImageUrl, String accountType) {
        // Create a new tradesman object
        Tradesman tradesman = new Tradesman(firstName, lastName, contactNumber, region, pastJobsImageUrl, certificationsImageUrl, accountType);

        // Get a reference to the "tradesmen" node in the database
        DatabaseReference userRef = mDatabase.child("users");

        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Set the tradesman data with the current user's uid
        userRef.child(currentUser.getUid()).setValue(tradesman);
    }
}
