package com.frazycrazy.kappu.mathspoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class M6_UserActivity extends AppCompatActivity {

    CircleImageView mSelectImage;
    static  final int GALLERY_REQUEST = 1;
    Uri mImageUri = null;
    StorageReference mStorage;
    DatabaseReference mDatabase,mDatabase_read;
    private FirebaseAuth mAuth_submit;
    ProgressDialog progressDialog_m6 ;

    String photoShareLink;
    EditText et_m6_name,et_m6_phon;
    TextView tv_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m6__user);

        progressDialog_m6 = new ProgressDialog(this);

        mAuth_submit = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase_read = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(mAuth_submit.getCurrentUser()).getUid());
        mSelectImage = findViewById(R.id.imageButton_m6_profilepic);

        //-----------
        et_m6_name = findViewById(R.id.editText_m6_name);
        et_m6_phon = findViewById(R.id.editText_m6_phon);
        tv_email =findViewById(R.id.textView_m6_email);

        mDatabase_read.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("---1->>>>", "onDataChange: "+dataSnapshot);
                Log.d("---2->>>>", "onDataChange: "+dataSnapshot.getValue());
               Map<String,Object> map = (Map<String,Object>)dataSnapshot.getValue();

                   if (map != null){
                    Log.d("---2->>>>", "onDataChange: "+map);

                    String nm = map.get("name").toString();
                    String em =  map.get("email").toString();
                    String ph = map.get("phone").toString();
                    String im =  map.get("image").toString();

                    et_m6_name.setText(nm);
                    tv_email.setText(em);
                    if(ph.compareTo("null")!=0)
                        et_m6_phon.setText(ph);
                    if(im.compareTo("null")!=0) {
                        try {
                            Picasso.get().load(im).into(mSelectImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(M6_UserActivity.this, "Unable to load image try agin later... ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(M6_UserActivity.this, "dataBase Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void do_m6_takeImage(View view) {
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent,GALLERY_REQUEST);
        SelectImage();
    }

    Integer REQUEST_CAMERA = 1;

    private void SelectImage(){

        final CharSequence[] item = {"Camera","Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(M6_UserActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK){
            if (requestCode == GALLERY_REQUEST ) {
                Uri imaUri = data.getData();
                CropImage.activity(imaUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setBorderLineColor(Color.RED)
                        .setAspectRatio(1,1)
                        .setGuidelinesColor(Color.GREEN)
                        .setSnapRadius(5)
                        .start(this);
                //            mImageUri = data.getData();
                //            mSelectImage.setImageURI(mImageUri);
            }else if (resultCode == REQUEST_CAMERA){
                Uri imUri = data.getData();
                CropImage.activity(imUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setBorderLineColor(Color.RED)
                        .setAspectRatio(1,1)
                        .setGuidelinesColor(Color.GREEN)
                        .setSnapRadius(5)
                        .start(this);

            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                mImageUri = result.getUri();
                mSelectImage.setImageURI(mImageUri);
            }
        }

    }

    private void startInserting(){

//
//        mProgress.setMessage("Proccessing...");
//        mProgress.show();

        final String name = et_m6_name.getText().toString().trim();
        final String phon = et_m6_phon.getText().toString().trim();


        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phon) && mImageUri != null){

            progressDialog_m6.setMessage("Updating user data...");
            progressDialog_m6.show();
            StorageReference filepath = mStorage.child("ProfileImages").child(Objects.requireNonNull(mImageUri.getLastPathSegment()));

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> downloadUrl = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl();

                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoShareLink = uri.toString();

                            String uid = Objects.requireNonNull(mAuth_submit.getCurrentUser()).getUid();
                            DatabaseReference current_db = mDatabase.child(uid);

                            current_db.child("name").setValue(name);
                            current_db.child("phone").setValue(phon);
                            current_db.child("image").setValue(photoShareLink);

                            progressDialog_m6.dismiss();
                            //-------------
                            Toast.makeText(M6_UserActivity.this, "User info updated ...", Toast.LENGTH_SHORT).show();
                            finish();
                           // mProgress.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog_m6.dismiss();
                            Toast.makeText(M6_UserActivity.this, "Data Inserted  failed ...", Toast.LENGTH_SHORT).show();
                      //      mProgress.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog_m6.dismiss();
                    Toast.makeText(M6_UserActivity.this, "Data Inserted  failed ...", Toast.LENGTH_SHORT).show();
                  //  mProgress.dismiss();
                }
            });
        }else{
            //------------

            progressDialog_m6.dismiss();
            Toast.makeText(M6_UserActivity.this, "Fill the Dara first ...", Toast.LENGTH_SHORT).show();
         //   mProgress.dismiss();
        }

    }

    public void do_startInserting(View view) {
        startInserting();
    }
}
