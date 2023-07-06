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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class updateTeacher extends AppCompatActivity {
    private ImageView updateTeacherImage;
    private int req = 1;
    private Bitmap bitmap=null;
    private String downloadUrl,uniqueKey,category;
    private EditText updateTeacherName;
    private EditText textEmailAddress, updateTeacherPost;
    private Button updateT, deleteT;
    private String name, email, image, post;
    private StorageReference storageRef;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);
        reference= FirebaseDatabase.getInstance().getReference().child("teacher");
        storageRef= FirebaseStorage.getInstance().getReference();
//        pd=new ProgressDialog(this);
        updateTeacherImage=findViewById(R.id.updateTeacherImage);
        updateTeacherName = findViewById(R.id.updateTeacherName);
        updateTeacherPost = findViewById(R.id.updateTeacherPost);
        textEmailAddress = findViewById(R.id.updateTeacherEmail);
        updateT = findViewById(R.id.updateTeacherButton);
        deleteT = findViewById(R.id.deleteTeacherButton);
         category=getIntent().getStringExtra("category");
         uniqueKey=getIntent().getStringExtra("key");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        post = getIntent().getStringExtra("post");
        image = getIntent().getStringExtra("image");
        try {
            Picasso.get().load(image).into(updateTeacherImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTeacherName.setText(name);
        textEmailAddress.setText(email);
        updateTeacherPost.setText(post);
        updateTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        updateT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=updateTeacherName.getText().toString();
                email=textEmailAddress.getText().toString();
                post=updateTeacherPost.getText().toString();
                checkValidation();

            }
        });
        deleteT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
    }

    private void deleteData() {
        reference.child(category).child(uniqueKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(updateTeacher.this, "Teacher deleted successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(updateTeacher.this, updateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
                public void onFailure(@NonNull Exception e) {
                Toast.makeText(updateTeacher.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkValidation() {
        if(name.isEmpty())
        {
            updateTeacherName.setError("Empty");
            updateTeacherName.requestFocus();
        }
        if(post.isEmpty())
        {
            updateTeacherPost.setError("Empty");
            updateTeacherPost.requestFocus();
        }
        if(email.isEmpty())
        {
            textEmailAddress.setError("Empty");
            textEmailAddress.requestFocus();
        }
        if(bitmap==null)
        {
            updateData(image);
        }
        else {
            uploadImage();
        }
    }

    private void uploadImage() {
        {

            ByteArrayOutputStream bao=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,bao);
            byte[] finalImage=bao.toByteArray();
            final StorageReference filepath;
            filepath=storageRef.child("teacher").child(finalImage+"jpg");
            final UploadTask uploadTask=filepath.putBytes(finalImage);
            uploadTask.addOnCompleteListener(updateTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                        updateData(downloadUrl);
                                    }
                                });
                            }
                        });
                    }
                    else
                    {
//                        pd.dismiss();
                        Toast.makeText(updateTeacher.this,"Something went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void updateData(String s) {

        HashMap hp=new HashMap();
        hp.put("name",name);hp.put("email",email);hp.put("post",post);hp.put("image",s);

        reference.child(category).child(uniqueKey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(updateTeacher.this, "Teacher updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(updateTeacher.this, updateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(updateTeacher.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == req && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateTeacherImage.setImageBitmap(bitmap);
        }
    }
}
