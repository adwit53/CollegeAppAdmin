package com.examplek.collegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UploadPdf extends AppCompatActivity {
    private CardView addPDF;
    private final int req=1;
    private Bitmap bitmap;
    private Uri pdfData;
    private ImageView noticeImageView;
    private String title;
    private EditText PDFTitle;
    private Button uploadPDFBtn;
    private String pdfName;
    private DatabaseReference dataReference;
    private StorageReference storageRef;
    String downloadUrl="";
    private ProgressDialog pd;
    private TextView pdfTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        dataReference= FirebaseDatabase.getInstance().getReference();
        pd=new ProgressDialog(this);
        storageRef= FirebaseStorage.getInstance().getReference();
        addPDF=findViewById(R.id.addPDF);
        uploadPDFBtn=findViewById(R.id.UploadPDFBtn);
        PDFTitle=findViewById(R.id.PDFTitle);
        pdfTextView=findViewById(R.id.pdfTextView);
//        noticeImageView=findViewById(R.id.noticeImageView);
//        uploadPDFBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(PDFTitle.getText().toString().isEmpty())
//                {
//                    PDFTitle.setError("Empty");
//                    PDFTitle.requestFocus();
//                }
//                else if(bitmap==null)
//                {
//                    uploadP();
//                }
//                else
//                {
//                    uploadImage();
//                }
//            }
//        });
        addPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        uploadPDFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=PDFTitle.getText().toString();
                if(title.isEmpty())
                {
                    PDFTitle.setError("Empty");
                    PDFTitle.requestFocus();
                }
                else if(pdfData==null)
                {
                    Toast.makeText(UploadPdf.this,"Error",Toast.LENGTH_SHORT).show();
                }
                else {
                        uploadPDF();
                }
            }
        });
    }

    private void uploadPDF() {
        pd.setTitle("Please Wait...");
        pd.setMessage("Uploading Pdf...");
        pd.show();
        StorageReference reference=storageRef.child("pdf/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri uri=uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UploadPdf.this,"Something Went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String valueOf) {
        String uniqueKey= dataReference.child("pdf").push().getKey();
        HashMap data=new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",downloadUrl);
        dataReference.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadPdf.this,"PDF uploaded successfully",Toast.LENGTH_SHORT);
                PDFTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadPdf.this,"Failed to upload Pdf",Toast.LENGTH_SHORT);
            }
        });
    }

    private void openGallery() {
Intent intent=new Intent();
intent.setType("pdf/docs/ppt");
intent.setAction(Intent.ACTION_GET_CONTENT);
startActivityForResult(Intent.createChooser(intent,"Select PDF file"),req);
    }
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==req && resultCode==RESULT_OK)
        {
             pdfData=data.getData();
             if(pdfData.toString().startsWith("content://")){
                Cursor cursor=null;
                cursor=UploadPdf.this.getContentResolver().query(pdfData,null,null,null,null);
                if(cursor!=null && cursor.moveToFirst())
                {
                    pdfName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
             }
             else if(pdfData.toString().startsWith("file://"))
             {
                 pdfName=new File(pdfData.toString()).getName();
             }
             pdfTextView.setText(pdfName);
//            Toast.makeText(this,""+pdfData,Toast.LENGTH_SHORT);
        }
    }
}