package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.example.firebasetest.Model.Job;
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

public class SubmitJobActivity extends AppCompatActivity {

    private EditText mJobTitleEditText;
    private EditText mJobDescriptionEditText;
    private EditText mJobStartDateEditText;
    private EditText mJobEndDateEditText;
    private EditText mJobLocationEditText;
    private EditText mTradeEditText;
    private ImageView jobImages;
    private Button mSubmitButton;

    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;

    private static final int JOB_IMAGES_REQUEST_CODE = 1;

    private Uri JobsImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_job);

        mJobTitleEditText = (EditText) findViewById(R.id.edit_text_job_title);
        mJobDescriptionEditText = (EditText) findViewById(R.id.edit_text_job_description);
        mJobStartDateEditText = (EditText) findViewById(R.id.edit_text_job_start_date);
        mJobEndDateEditText = (EditText) findViewById(R.id.edit_text_job_end_date);
        mJobLocationEditText = (EditText) findViewById(R.id.edit_text_job_location);
        mTradeEditText = (EditText) findViewById(R.id.edit_text_trade);
        jobImages = (ImageView) findViewById(R.id.image_view_job_photos);
        mSubmitButton = (Button) findViewById(R.id.button_submit);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("jobs");
        mStorage = FirebaseStorage.getInstance();

        jobImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, JOB_IMAGES_REQUEST_CODE);
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jobTitle = mJobTitleEditText.getText().toString();
                String jobDescription = mJobDescriptionEditText.getText().toString();
                String jobStartDate = mJobStartDateEditText.getText().toString();
                String jobEndDate = mJobEndDateEditText.getText().toString();
                String jobLocation = mJobLocationEditText.getText().toString();
                String trade = mTradeEditText.getText().toString();
                String accountType = "general";

                if (jobTitle.isEmpty() || jobDescription.isEmpty() || jobStartDate.isEmpty() || jobEndDate.isEmpty() || jobLocation.isEmpty() || trade.isEmpty()) {
                    Toast.makeText(SubmitJobActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (jobImages == null) {
                    Toast.makeText(SubmitJobActivity.this, "Please select images", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent signUpIntent = new Intent(SubmitJobActivity.this, MainActivity.class);
                startActivity(signUpIntent);

                uploadImagesToFirebaseStorage(jobTitle, jobDescription, jobStartDate, jobEndDate, jobLocation, trade, accountType);

            }
        });
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == JOB_IMAGES_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
                JobsImageUri = data.getData();
                jobImages.setImageURI(JobsImageUri);
            }
        }

        private void uploadImagesToFirebaseStorage(final String jobTitle, final String jobDescription, final String jobStartDate, final String jobEndDate, final String jobLocation, final String trade, final String accountType) {
            StorageReference JobsImageRef = mStorage.getReference().child("job_images").child(UUID.randomUUID().toString());


            JobsImageRef.putFile(JobsImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            JobsImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String JobsImageUrl = uri.toString();
                                    saveJobInfo(jobTitle, jobDescription, jobStartDate, jobEndDate, jobLocation, trade, JobsImageUrl, accountType);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SubmitJobActivity.this, "Error uploading jobs image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    private void saveJobInfo(String jobTitle, String jobDescription, String jobStartDate, String jobEndDate, String jobLocation, String trade, String JobsImageUrl, String accountType) {
        Job jobs = new Job(jobTitle, jobDescription, jobStartDate, jobEndDate, jobLocation, trade, JobsImageUrl, accountType);

        // Get a reference to the "tradesmen" node in the database
        DatabaseReference userRef = mDatabase.child("jobs");

        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Set the jobs data with the current user's uid
        userRef.child(currentUser.getUid()).setValue(jobs);
    }
}