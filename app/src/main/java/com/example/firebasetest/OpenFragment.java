package com.example.firebasetest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.firebasetest.Model.Job;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class OpenFragment extends Fragment {

    private FirebaseAuth mAuth;

    private Button submitJobs;
    private RecyclerView jobRecycler;

    private DatabaseReference mDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_open, container, false);

        submitJobs = myview.findViewById(R.id.submitJobs);
        jobRecycler = myview.findViewById(R.id.images_recycler_view);

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("jobs").child(uid);

        //Recycler
        LinearLayoutManager layoutManagerJobs = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        layoutManagerJobs.setStackFromEnd(true);
        layoutManagerJobs.setReverseLayout(true);
        jobRecycler.setHasFixedSize(true);
        jobRecycler.setLayoutManager(layoutManagerJobs);

        submitJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SubmitJobActivity.class);
                startActivity(intent);
            }
        });
        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Job> options =
                new FirebaseRecyclerOptions.Builder<Job>()
                        .setQuery(mDatabase, Job.class)
                        .setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<Job, JobViewHolder> jobAdapter = new FirebaseRecyclerAdapter<Job, JobViewHolder>(options) {
            @NonNull
            @Override
            public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new JobViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull JobViewHolder holder, int position, @NonNull Job model) {

                holder.setImage(model.getJobsImageUrl());


                holder.setJobTitle(model.getJobTitle());
                holder.setLocation(model.getJobLocation());
                holder.setTrade(model.getTrade());

            }
        };
        jobRecycler.setAdapter(jobAdapter);
        jobAdapter.startListening();
    }



        public static class JobViewHolder extends RecyclerView.ViewHolder{

            View mJobView;

            public JobViewHolder(@NonNull View itemView) {
                super(itemView);
                mJobView = itemView;
            }

            public void setJobTitle(String jobTitle){

                TextView title = mJobView.findViewById(R.id.JobTitle);
                title.setText(jobTitle);
            }

            public void setLocation(String location){

                TextView mLocation = mJobView.findViewById(R.id.location);
                mLocation.setText(location);
            }

            public void setTrade(String trade){
                TextView mTrade = mJobView.findViewById(R.id.trade);
                mTrade.setText(trade);

            }


            public ImageView setImage(String imageUrl) {
                ImageView jobImage = mJobView.findViewById(R.id.jobImage);
                Glide.with(itemView.getContext()).load(imageUrl).into(jobImage);
                return jobImage;
            }
        }
}