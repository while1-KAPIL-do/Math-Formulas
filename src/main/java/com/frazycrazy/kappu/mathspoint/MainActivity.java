package com.frazycrazy.kappu.mathspoint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recycler_view;
//
    DrawerLayout drawerLayout ;
    ActionBarDrawerToggle toggle;
    NavigationView nav_view;

    //logout---
    FirebaseAuth logout_mAuth;
    FirebaseAuth.AuthStateListener mAuthlistener;


    //----/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Drawer--
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        logout_mAuth = FirebaseAuth.getInstance();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        //----------------------nav header----------------------
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        View  headerView = nav_view.inflateHeaderView(R.layout.header_for_drawer);
        final TextView tv_header_name = headerView.findViewById(R.id.textView_profile_username);

        final CircleImageView iv_header_profile = headerView.findViewById(R.id.imageView_userprofile);
        DatabaseReference mDatabase_read = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(logout_mAuth.getCurrentUser()).getUid());
        mDatabase_read.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("---1->>>>", "onDataChange: " + dataSnapshot);
                Log.d("---2->>>>", "onDataChange: " + dataSnapshot.getValue());
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                //  Log.d("---keySet->>>>", "onDataChange: "+map.keySet().toArray());

                if (map != null) {
                    String nm = map.get("name").toString();

                    String im = map.get("image").toString();

                    tv_header_name.setText(nm);

                    Log.d("---link->>>>", "onDataChange: " + iv_header_profile);

                    if (im.compareTo("null") != 0) {
                        try {
                            Picasso.get().load(im).into(iv_header_profile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
             }
        });

        // tv_header_name.setText("Kapil");

        //----------------------nav header----------------------
        //----------------
        //logpout------

        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(MainActivity.this,M1_Activity.class));
                }
            }
        };
        //--------/

        //Define recycleview
        recycler_view = findViewById(R.id.recycler_Expand);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        //Initialize your Firebase app
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Reference to your entire Firebase database
        DatabaseReference parentReference = database.getReference().child("TagList");

        //reading data from firebase
        parentReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<ParentList> Parent = new ArrayList<>();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()){

                    final String ParentKey = Objects.requireNonNull(snapshot.getKey());
                    Log.d("________>>>>>>>>>>>>", "onDataChange: "+snapshot);
                    //Log.d("________>>>>>>>>>>>>", "onDataChange: "+snapshot.child());

                    DatabaseReference childReference = FirebaseDatabase.getInstance().getReference().child("TagList").child(ParentKey);

                    childReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final List<ChildList> Child = new ArrayList<>();

                            for (DataSnapshot snapshot1:dataSnapshot.getChildren()) {

                                 final String ChildKey = Objects.requireNonNull(snapshot1.getKey());
                                 //snapshot1.child("titre").getValue();
                                 Child.add(new ChildList(ChildKey));
                            }

                            Parent.add(new ParentList(ParentKey, Child));
                            DocExpandableRecyclerAdapter adapter = new DocExpandableRecyclerAdapter(Parent);
                            recycler_view.setAdapter(adapter);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Failed to read value
                             System.out.println("Failed to read value." + error.toException());
                        }
                    });

                }//end or foe loop
            }//end of On data Change

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public class DocExpandableRecyclerAdapter extends ExpandableRecyclerViewAdapter<MyParentViewHolder,MyChildViewHolder> {


        DocExpandableRecyclerAdapter(List<ParentList> groups) {
            super(groups);
        }

        @Override
        public MyParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent, parent, false);
            return new MyParentViewHolder(view);
        }

        @Override
        public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
            return new MyChildViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(MyChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

            final ChildList childItem = ((ParentList) group).getItems().get(childIndex);
            holder.onBind(childItem.getTitle());

            final String TitleChild=group.getTitle();


            holder.listChild2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("__________>>>>>>>>>", "ChildViewHolder : "+ TitleChild);
//                    Toast toast = Toast.makeText(getApplicationContext(), "\nparent : "+childItem.getTitle()+"\nchild : "+TitleChild, Toast.LENGTH_SHORT);
//                    toast.show();

                    Bundle b = new Bundle();
                    b.putString("subtag",childItem.getTitle());
                    b.putString("tag",TitleChild);
                    b.putString("activity","main");

                    Intent j = new Intent(MainActivity.this,M5_ImageActivity.class);
                    j.putExtras(b);
                    startActivity(j);

                }

            });

        }

        @Override
        public void onBindGroupViewHolder(MyParentViewHolder holder, int flatPosition, final ExpandableGroup group) {
            holder.setParentTitle(group);

            if(group.getItems()==null)
            {
                holder.listGroup.setOnClickListener(  new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("__________>>>>>>>>>", "GroupViewHolder : "+ group.toString());
                        Toast toast = Toast.makeText(getApplicationContext(), group.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            }
        }


    }


//Drawer-------------
//


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.dra_ac_info:
                Log.d("------>>>>>>>>>", "onOptionsItemSelected: 1");
                startActivity(new Intent(MainActivity.this,M6_UserActivity.class));
                return true;
            case R.id.dra_fav:
                Log.d("------>>>>>>>>>", "onOptionsItemSelected: 2");
                startActivity(new Intent(MainActivity.this,M7_FevActivity.class));
                return true;
            case R.id.dra_about:
                Log.d("------>>>>>>>>>", "onOptionsItemSelected: 5");
                return true;
            case R.id.dra_logout:
                Log.d("------>>>>>>>>>", "onOptionsItemSelected: 6");
                logout_mAuth.signOut();
                return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//--------------


    //on back press

    private Boolean exit = false;
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
            //System.exit(0);
        } else {
            Toast.makeText(this,"Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2 * 1000);

        }

    }
    protected void onStart() {
        super.onStart();
        logout_mAuth.addAuthStateListener(mAuthlistener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        final MenuItem dataItem = menu.findItem(R.id.action_data);
        final MenuItem tagItem = menu.findItem(R.id.action_tag);

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
                        dataItem.setVisible(true);
                        tagItem.setVisible(true);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "dataBase Error", Toast.LENGTH_SHORT).show();
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_data:
                startActivity(new Intent(MainActivity.this,M4_InsertImgActivity.class));
                return true;
            case R.id.action_tag:
                startActivity(new Intent(MainActivity.this,M3_Activity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
