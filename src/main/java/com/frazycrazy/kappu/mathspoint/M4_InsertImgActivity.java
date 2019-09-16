package com.frazycrazy.kappu.mathspoint;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class M4_InsertImgActivity extends AppCompatActivity  {

    private ImageButton mSelectImage;
    private EditText mSubtag;
    private Uri mImageUri = null;
    private static  final int GALLERY_REQUEST = 1;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private String mySubtag="",myTag="";
    private String photoShareLink;
    private Spinner spinTag;
    private List<String> l = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m4__insert_img);

        ActivityCompat.requestPermissions(M4_InsertImgActivity.this, new String[]{Manifest.permission.CAMERA}, 1);


        //__________ Get Element _________
        mProgress = new ProgressDialog(this);
        Button do_insert = findViewById(R.id.button_insert);
        spinTag = findViewById(R.id.spinner_tag);
        mSelectImage = findViewById(R.id.imageButton);
        mSubtag = findViewById(R.id.editText_subtag4);

        //__________ Get DB ____________
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("TagList");
        DatabaseReference mDatabase_readTag = FirebaseDatabase.getInstance().getReference().child("MyTags");


        //------------------------------

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, l);
        mDatabase_readTag.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue(String.class);
                l.add(value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //------------------------------

        //__________ Add Spinner ________________

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTag.setAdapter(adapter);

        do_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startInserting();
            }
        });

    }


    private void startInserting(){

        Log.d("___________1_>>>>>>>>>", "startInserting: "+mSubtag.getText().toString());

        mProgress.setMessage("Inserting...");
        mProgress.show();

        if(spinTag.getSelectedItem() != null) {
            myTag =  spinTag.getSelectedItem().toString();
        }
        mySubtag = mSubtag.getText().toString().trim();


        if (!TextUtils.isEmpty(myTag) && !TextUtils.isEmpty(mySubtag) && mImageUri != null){
            StorageReference filepath = mStorage.child("TagImage").child(Objects.requireNonNull(mImageUri.getLastPathSegment()));

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> downloadUrl = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl();

                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoShareLink = uri.toString();

                            DatabaseReference newPost = mDatabase.child(myTag);
                            newPost.child(mySubtag).setValue(photoShareLink);

                            //-------------
                            Toast.makeText(M4_InsertImgActivity.this, "Data Inserted...", Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(M4_InsertImgActivity.this, "Data Inserted  failed ...", Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(M4_InsertImgActivity.this, "Data Inserted  failed ...", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            });
        }else{
            //------------
            Toast.makeText(M4_InsertImgActivity.this, "Fill the Dara first ...", Toast.LENGTH_SHORT).show();
            mProgress.dismiss();
        }

        Log.d("___________2_>>>>>>>>>", "startInserting: "+spinTag.getSelectedItem());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
                if (requestCode == GALLERY_REQUEST ) {
                    Uri imaUri = data.getData();
                    CropImage.activity(imaUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .setBorderCornerLength(5)
                            .start(this);
        //            mImageUri = data.getData();
        //            mSelectImage.setImageURI(mImageUri);
                }else if (resultCode == REQUEST_CAMERA){
                    Uri imUri = data.getData();
                    CropImage.activity(imUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .setBorderCornerLength(5)
                            .start(this);
                }

                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

                    CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    mImageUri = result.getUri();
                    mSelectImage.setImageURI(mImageUri);
                }
            }


    }

    public void do_uploadImage(View view) {
       SelectImage();
    }
    Integer REQUEST_CAMERA = 1;

    private void SelectImage(){

        final CharSequence[] item = {"Camera","Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(M4_InsertImgActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(item[i].equals("Camera")){

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUEST_CAMERA);

                }else if(item[i].equals("Gallery")){


                    //
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent,GALLERY_REQUEST);
                    //
//                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    //startActivityForResult(intent.createChooser(intent,"Select File ;- "),SELECT_FILE);
//                    startActivityForResult(intent,SELECT_FILE);

                }else if(item[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

//
//    private boolean isConn(){
//        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        return (networkInfo!=null && networkInfo.isConnected());
//    }

}
