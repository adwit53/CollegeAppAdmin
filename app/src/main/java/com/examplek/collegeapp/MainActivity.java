package com.examplek.collegeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.CollegeApp.R;
import com.examplek.collegeapp.Faculty.updateFaculty;
import com.examplek.collegeapp.notice.UploadNotice;
import com.examplek.collegeapp.notice.deleteNotice;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView uploadNotice, addGalleryImage, addEbook,faculty,DeleteNotice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadNotice = findViewById(R.id.addNotice);
        uploadNotice.setOnClickListener(this);
        addGalleryImage=findViewById(R.id.addGalleryImage);
        addGalleryImage.setOnClickListener(this);
        addEbook=findViewById(R.id.addEbook);
        addEbook.setOnClickListener(this);
        faculty=findViewById(R.id.faculty);
        faculty.setOnClickListener(this);
        DeleteNotice=findViewById(R.id.deleteNotice);
        DeleteNotice.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        Intent intent;
        if (viewId == R.id.addNotice) {
            intent = new Intent(MainActivity.this, UploadNotice.class);
            startActivity(intent);
        }
        if (viewId == R.id.addGalleryImage) {
             intent = new Intent(MainActivity.this, UploadImage.class);
            startActivity(intent);
        }        if (viewId == R.id.addEbook) {
             intent = new Intent(MainActivity.this, UploadPdf.class);
            startActivity(intent);
        }           if (viewId == R.id.faculty) {
             intent = new Intent(MainActivity.this, updateFaculty.class);
            startActivity(intent);
        }
        if (viewId == R.id.deleteNotice) {
             intent = new Intent(MainActivity.this, deleteNotice.class);
            startActivity(intent);
        }
    }

}