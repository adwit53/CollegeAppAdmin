package com.examplek.collegeapp.Faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.CollegeApp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class addTeacher extends AppCompatActivity {
private ImageView addTeacherImage;
private EditText TeacherName;private EditText TeacherEmail;private EditText TeacherPost;
private Button add_teacher_btn;
private Spinner teacher_category;
private Bitmap bitmap=null;
private String category;
private String name,email,post,downloadUrl="";
private ProgressDialog pd;
private StorageReference storageRef;
private DatabaseReference reference,dbRef;
private final int req=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        addTeacherImage=findViewById(R.id.addTeacherImage);
        TeacherName=findViewById(R.id.addTeacherName);
        TeacherPost=findViewById(R.id.addTeacherPost);
        TeacherEmail=findViewById(R.id.addTeacherEmail);
        teacher_category=findViewById(R.id.add_teacher_category);
        add_teacher_btn=findViewById(R.id.AddTeacherBtn);
        reference= FirebaseDatabase.getInstance().getReference().child("teacher");
        storageRef= FirebaseStorage.getInstance().getReference();
        pd=new ProgressDialog(this);

        String[] items=new String[]{"Select Category","CS","Mechanical","Electrical"};
        teacher_category.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));
        teacher_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=teacher_category.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        add_teacher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        name=TeacherName.getText().toString();email=TeacherEmail.getText().toString();post=TeacherPost.getText().toString();
        if(name.isEmpty())
        {
            TeacherName.setError("Empty");
            TeacherName.requestFocus();
        }
        if(post.isEmpty())
        {
            TeacherPost.setError("Empty");
            TeacherPost.requestFocus();
        }
        if(email.isEmpty())
        {
            TeacherEmail.setError("Empty");
            TeacherEmail.requestFocus();
        }
        if(category=="Select Category")
        {
            Toast.makeText(this,"Please Provide category",Toast.LENGTH_SHORT).show();
        }
        if(bitmap==null)
        {
            insertData();
        }
        else {
            pd.setMessage("Uploading...");
            pd.show();
            insertImage();
        }
    }

    private void insertImage() {
        {

            ByteArrayOutputStream bao=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,bao);
            byte[] finalImage=bao.toByteArray();
            final StorageReference filepath;
            filepath=storageRef.child("teacher").child(finalImage+"jpg");
            final UploadTask uploadTask=filepath.putBytes(finalImage);
            uploadTask.addOnCompleteListener(addTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadUrl=String.valueOf(uri);
                                        insertData();
                                    }
                                });
                            }
                        });
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(addTeacher.this,"Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void insertData()
        {
            dbRef=reference.child(category);
            final String uniqueKey=dbRef.push().getKey();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                TeacherData teacherData=new TeacherData(name,email,post,downloadUrl,uniqueKey);
                dbRef.child(uniqueKey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        Toast.makeText(addTeacher.this,"Faculty profile uploaded",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(addTeacher.this,"Something went wrong",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }



    private void openGallery() {
        Intent pickImage =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,req);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==req && resultCode==RESULT_OK)
        {
            Uri uri=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            addTeacherImage.setImageBitmap(bitmap);
        }
    }
}