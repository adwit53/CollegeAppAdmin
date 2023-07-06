package com.examplek.collegeapp.notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.CollegeApp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class deleteNotice extends AppCompatActivity {
    private RecyclerView deleteNoticeRecycler;
    private ProgressBar progressBar;
    private ArrayList<noticeData> list;
    private noticeAdapter adapter;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);
        deleteNoticeRecycler=findViewById(R.id.deleteNoticeRecycler);
        reference= FirebaseDatabase.getInstance().getReference().child("Notice");
        progressBar= findViewById(R.id.progressBar);
        deleteNoticeRecycler.setLayoutManager(new LinearLayoutManager(this));
        deleteNoticeRecycler.setHasFixedSize(true);
        getNotice();
    }

    private void getNotice() {
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            list=new ArrayList<>();
            for(DataSnapshot snap:snapshot.getChildren()){
                noticeData data=snap.getValue(noticeData.class);
                list.add(data);

            }
            adapter=new noticeAdapter(deleteNotice.this,list);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            deleteNoticeRecycler.setAdapter(adapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            progressBar.setVisibility(View.GONE);

            Toast.makeText(deleteNotice.this,error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }
}