
package com.examplek.collegeapp.Faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.CollegeApp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class updateFaculty extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView csDepartment, mechanical,electrical;
    private LinearLayout csNoData,mechNoDta,elecNoData;
    private List<TeacherData> list1,list2,list3;
    private DatabaseReference reference,dbRef;
    private TeacherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);
        fab=findViewById(R.id.fac);
        csDepartment=findViewById(R.id.csDepartment);
        mechanical=findViewById(R.id.mechanicalDepartment);
        electrical=findViewById(R.id.eeeDepartment);
        csNoData=findViewById(R.id.csNoData);
        mechNoDta=findViewById(R.id.mechanicalNoData);
        elecNoData=findViewById(R.id.eeeNoData);
        reference= FirebaseDatabase.getInstance().getReference().child("teacher");
        csDepartment();
        mechanicalDepartment();
        electricalDepartment();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(updateFaculty.this,addTeacher.class));
            }
        });
    }

    private void csDepartment() {
        dbRef=reference.child("CS");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1=new ArrayList<>();
                if(!snapshot.exists())
                {
                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);
                }
                else {
                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snap:snapshot.getChildren())
                    {
                        TeacherData data=snap.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(updateFaculty.this));
                    adapter=new TeacherAdapter(list1,updateFaculty.this,"CS");
                    csDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(updateFaculty.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void electricalDepartment() {
        dbRef=reference.child("Electrical");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3=new ArrayList<>();
                if(!snapshot.exists())
                {
                    elecNoData.setVisibility(View.VISIBLE);
                    electrical.setVisibility(View.GONE);
                }
                else {
                    elecNoData.setVisibility(View.GONE);
                    electrical.setVisibility(View.VISIBLE);
                    for(DataSnapshot snap:snapshot.getChildren())
                    {
                        TeacherData data=snap.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    electrical.setHasFixedSize(true);
                    electrical.setLayoutManager(new LinearLayoutManager(updateFaculty.this));
                    adapter=new TeacherAdapter(list3,updateFaculty.this,"Electrical");
                    electrical.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(updateFaculty.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void mechanicalDepartment() {
        dbRef=reference.child("Mechanical");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2=new ArrayList<>();
                if(!snapshot.exists())
                {
                    mechNoDta.setVisibility(View.VISIBLE);
                    mechanical.setVisibility(View.GONE);
                }
                else {
                    mechNoDta.setVisibility(View.GONE);
                    mechanical.setVisibility(View.VISIBLE);
                    for(DataSnapshot snap:snapshot.getChildren())
                    {
                        TeacherData data=snap.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    mechanical.setHasFixedSize(true);
                    mechanical.setLayoutManager(new LinearLayoutManager(updateFaculty.this));
                    adapter=new TeacherAdapter(list2,updateFaculty.this,"Mechanical");
                    mechanical.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(updateFaculty.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }



}