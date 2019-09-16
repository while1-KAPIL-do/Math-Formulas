package com.frazycrazy.kappu.mathspoint;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class M7_FevActivity extends AppCompatActivity {


    List<String> l = new ArrayList<>();
    List<String> k = new ArrayList<>();

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m7__fev);

        //-----------like
        FirebaseAuth mAuth_submit_like = FirebaseAuth.getInstance();
        DatabaseReference mDatabase_like = FirebaseDatabase.getInstance().getReference().child("Users_likes");

        String uid = Objects.requireNonNull(mAuth_submit_like.getCurrentUser()).getUid();
        DatabaseReference current_db = mDatabase_like.child(uid);
        listView = findViewById(R.id.listView_m7);


        //-----------like/

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, l);
        current_db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue(String.class);
                String keys = dataSnapshot.getKey();
                l.add(value);
                k.add(keys);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String itemvalue = (String)listView.getItemAtPosition(i);
                String itemKey = k.get(i);
                Toast.makeText(M7_FevActivity.this, itemKey +"  :  "+itemvalue, Toast.LENGTH_SHORT).show();


                Bundle b = new Bundle();
                b.putString("subtag",itemvalue);
                b.putString("tag",itemKey);
                b.putString("activity","m7");

                Intent j = new Intent(M7_FevActivity.this,M5_ImageActivity.class);
                j.putExtras(b);
                startActivity(j);

            }
        });

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
