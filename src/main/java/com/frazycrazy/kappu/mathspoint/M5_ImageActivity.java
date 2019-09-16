package com.frazycrazy.kappu.mathspoint;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.Objects;


public class M5_ImageActivity extends AppCompatActivity {

    ImageView imageView ;
    DatabaseReference mDatabase_takeLink;
    String imgUrl=null,tag,subtag,act;
    ProgressDialog progressDialog_m5;
    TextView et_m5_tag,et_m5_subtag;

    Button btn_m5_like;
    private int flag1=0;

    private DatabaseReference current_db;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m5__image);


        et_m5_tag = findViewById(R.id.textView_m5_tag);
        et_m5_subtag = findViewById(R.id.textView_m5_subtag);
        imageView = findViewById(R.id.imageView);
        progressDialog_m5 = new ProgressDialog(this);

        btn_m5_like = findViewById(R.id.button_m5_like);

        isAdmin();

        //-----------like
        FirebaseAuth mAuth_submit_like = FirebaseAuth.getInstance();
        DatabaseReference mDatabase_like = FirebaseDatabase.getInstance().getReference().child("Users_likes");

        uid = Objects.requireNonNull(mAuth_submit_like.getCurrentUser()).getUid();
        current_db = mDatabase_like.child(uid);

        mDatabase_like.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)){
                    current_db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                            Map<String,Object> map = (Map<String,Object>)dataSnapshot2.getValue();

                            if(map != null) {
                                if (Objects.requireNonNull(map).containsKey(tag) && map.containsValue(subtag)) {
                                    btn_m5_like.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                                    flag1 = 1;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //-----------like/

        //------ recive data--------
        Bundle b =this.getIntent().getExtras();
        tag = Objects.requireNonNull(b).getString("tag");
        subtag = b.getString("subtag");
        act = b.getString("activity");
        if (act.equals("m7")){
            findViewById(R.id.button_m5_delete).setVisibility(View.INVISIBLE);
        }
        //------ recive data--------//

        et_m5_tag.setText(tag);
        et_m5_subtag.setText(subtag);

        mDatabase_takeLink = FirebaseDatabase.getInstance().getReference().child("TagList").child(tag).child(subtag);

       // mDatabase_takeLink.child(tag).child(subtag);
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(tag)) {
//            progressDialog_m5.setMessage("Loading Image ...");
//            progressDialog_m5.show();

            mDatabase_takeLink.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        imgUrl = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                        try {

                            Picasso.get().load(imgUrl).into(imageView);
                            // progressDialog_m5.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            // progressDialog_m5.dismiss();
                        }//
                        // Picasso.get().load(imgUrl).resize(100,100).centerCrop().into(imageView);
                    }else if(act.equals("m7")){
                        isDataDeleted();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(M5_ImageActivity.this, "Try Again....", Toast.LENGTH_SHORT).show();
                   // progressDialog_m5.dismiss();
                }
            });
        }else {
            Toast.makeText(this, "Go back and try again... ", Toast.LENGTH_SHORT).show();
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(M5_ImageActivity.this);
                @SuppressLint("InflateParams") View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout_m5,null);
                PhotoView photoView = mView.findViewById(R.id.imageView_dialog);

                Picasso.get().load(imgUrl).into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });



      //  Picasso.get().load("").resize(50,50).centerCrop().into(imageView);
    }


    public void isDataDeleted(){
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(M5_ImageActivity.this);
        builder2.setMessage("These data is erased by Admin..! ")
                .setPositiveButton("Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DatabaseReference mDatabase_like , current_db;
                FirebaseAuth mAuth_submit_like;

                mAuth_submit_like = FirebaseAuth.getInstance();
                mDatabase_like = FirebaseDatabase.getInstance().getReference().child("Users_likes");

                String uid = Objects.requireNonNull(mAuth_submit_like.getCurrentUser()).getUid();
                current_db = mDatabase_like.child(uid);

                current_db.child(tag).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
            }
        }).setCancelable(false);
        AlertDialog alertDialog = builder2.create();
        alertDialog.show();

    }


    public void do_m5_delete(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(M5_ImageActivity.this);
        builder.setMessage("Are you sure want to delete !")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference mDatabase_remove = FirebaseDatabase.getInstance().getReference().child("TagList").child(tag);
                        mDatabase_remove.child(subtag).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });
                    }
                }).setNegativeButton("No",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    public void do_m5_like(View view) {
        if (flag1==0) {
            current_db.child(tag).setValue(subtag).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    btn_m5_like.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                    flag1 = 1;
                }
            });
        }else if (flag1==1) {
            current_db.child(tag).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    btn_m5_like.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                    flag1 = 0;
                }
            });
        }
    }

    public void do_m5_share(View view) {
        Intent share_intent = new Intent(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        String shareBody = "\t"+tag+"( "+subtag+" )\n\n"+imgUrl;
        String shareSub = getResources().getString(R.string.app_name);
        share_intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        share_intent.putExtra(Intent.EXTRA_TEXT,shareBody);
        startActivity(Intent.createChooser(share_intent,"Share using..."));
    }


    private void isAdmin(){

        FirebaseAuth mAuth_admin = FirebaseAuth.getInstance();
        final String user_id = Objects.requireNonNull(mAuth_admin.getCurrentUser()).getUid();
        DatabaseReference db_admin = FirebaseDatabase.getInstance().getReference().child("admin").child("id");
        db_admin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                   String id = Objects.requireNonNull(dataSnapshot.getValue()).toString();

                    if (user_id.equals(id)) {
                        Log.d("------TTTT", "onDataChange: ");
                        findViewById(R.id.button_m5_delete).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(M5_ImageActivity.this, "dataBase Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
