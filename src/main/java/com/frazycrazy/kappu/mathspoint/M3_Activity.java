package com.frazycrazy.kappu.mathspoint;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class M3_Activity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText et_tag;
    ProgressDialog mProgress;
    List<String> l = new ArrayList<>();
    List<String> keyList = new ArrayList<>();
    DatabaseReference mDatabase_readTag;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m3_);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("MyTags");
        et_tag = findViewById(R.id.editText_tag);
        listView = findViewById(R.id.listView_m3);

        mProgress = new ProgressDialog(this);


        mDatabase_readTag = FirebaseDatabase.getInstance().getReference().child("MyTags");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, l);


        mDatabase_readTag.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue(String.class);
                String key = dataSnapshot.getKey();
                keyList.add(key);
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
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {

                final String itemvalue = (String)listView.getItemAtPosition(j);
                final String itemKey = keyList.get(j);


                AlertDialog.Builder builder = new AlertDialog.Builder(M3_Activity.this);
                builder.setMessage("Are you sure want to delete !")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mDatabase_readTag.child(itemKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(M3_Activity.this, itemvalue +" is removed...", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(getIntent());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(M3_Activity.this, "Try Again...", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).setNegativeButton("No",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();



            }
        });

    }

    public void do_tag(View view) {

        String myTag = et_tag.getText().toString().trim();

        if (TextUtils.isEmpty(myTag)) {

            Toast.makeText(this, "fill the data first", Toast.LENGTH_SHORT).show();

        } else {

            mProgress.setMessage("Tag is uploading ...");
            mProgress.show();


            mDatabase.push().setValue(myTag).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(M3_Activity.this, "Tag uploaded", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    } else {
                        Toast.makeText(M3_Activity.this, "Data Not Inserted...", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(M3_Activity.this, "Unable to process ...", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            });


        }
    }

}
