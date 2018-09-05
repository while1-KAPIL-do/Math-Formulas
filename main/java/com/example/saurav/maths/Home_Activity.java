package com.example.saurav.maths;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home_Activity extends AppCompatActivity {

    DatabaseHelper mydb;
    String[] tag_list,title_list,data_list;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Movie> my_arrayList = new ArrayList<>();
    private String[] s1,s2,s3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mydb = new DatabaseHelper(this);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeUp();
            }
        });
        onSwipeUp();
    }



     //+++++++++++++++++++++++++++++ SQL -> Lite / On Up Swipe +++++++++++++++++++++++++++++



    public void onSwipeUp() {
        if (isConn()) {

            my_arrayList = new ArrayList<>();
            String json_url="https://appbykapil.000webhostapp.com/viewbyjson.php";

// taking data from WAMP ( MYSQL )

            BackgroundTask backgroundTask = new BackgroundTask(Home_Activity.this,json_url);
            my_arrayList = backgroundTask.getMyLists();

// putting data in Strin array

            for (int i = 0; i < my_arrayList.size(); i++) {

                tag_list[i] = my_arrayList.get(i).getYear();
                title_list[i] =my_arrayList.get(i).getTitle();
                data_list[i] = my_arrayList.get(i).getGenre();

            }

            String TAG="msg";
            Log.d(TAG, " \n\n"+"onSwipeUp: "+my_arrayList.size()+" \n\n");

            if(putInLite()){
                Toast.makeText(this, "putInLite()", Toast.LENGTH_SHORT).show();
                if(add_into_Lite()){

                    Toast.makeText(this, "add_into_Lite()", Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(this, "add_into_Lite()", Toast.LENGTH_SHORT).show();
                }

            }else {

                Toast.makeText(this, "NOT putInLite()", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Your Internet conn is OFF", Toast.LENGTH_LONG).show();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }


   public boolean putInLite(){
        int count=0,j;
        for (j=0;j<my_arrayList.size();j++) {

            count += mydb.insertData(title_list[j], data_list[j], tag_list[j]);

        }
       return count == j-1;
    }

    public boolean add_into_Lite() {
        int count=0,i;
        String stitle,sdata,stag;

        mydb.deleteAllData();

        for (i=0  ; i < title_list.length ; i++) {

            stitle = title_list[i];
            sdata = data_list[i];
            stag = tag_list[i];

            count += mydb.insertData(stitle, sdata, stag);
        }
        return count == i;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    public  boolean Wait_until_complete(){
       for (int iWait=0;iWait<10;iWait++){
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
               break;
           }
           if (my_arrayList.isEmpty()){
               return  true;
           }
       }
       return false;
    }

    // checking internet connection
    private boolean isConn(){
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

// buttons---------------------
    public void btn_showAll(View view) {
        Cursor cursor = mydb.getAllData();
        Cursor cursor_count = mydb.getAllData();
        int i=0;

        while(cursor_count.moveToNext()) {i++;}

        String st1[]=new String[i];
        String st2[]=new String[i];
        String st3[]=new String[i];
        i=0;
        while(cursor.moveToNext()){
            st3[i] = cursor.getString(0);
            st2[i] = cursor.getString(1);
            st1[i] = cursor.getString(2);
            i++;
        }
        Bundle b = new Bundle();
        b.putStringArray("string1",st1);
        b.putStringArray("string2",st2);
        b.putStringArray("string3",st3);
        b.putString("myTag","All DATA");
        Intent j = new Intent(Home_Activity.this,Tag_Activity.class);
        j.putExtras(b);
        startActivity(j);

    }
    public void btn_tag1(View view) {
        String tagm = "Trignometry";
        Cursor cursor = mydb.getDataByTag(tagm);
        Cursor cursor_count = mydb.getDataByTag(tagm);
        int i=0;

        while(cursor_count.moveToNext()) {i++;}

        String st1[]=new String[i];
        String st2[]=new String[i];
        String st3[]=new String[i];
        i=0;
        while(cursor.moveToNext()){
            st3[i] = cursor.getString(0);
            st2[i] = cursor.getString(1);
            st1[i] = cursor.getString(2);
            i++;
        }

        Bundle b = new Bundle();
        b.putStringArray("string1",st1);
        b.putStringArray("string2",st2);
        b.putStringArray("string3",st3);
        b.putString("myTag",tagm);
        Intent j = new Intent(Home_Activity.this,Tag_Activity.class);
        j.putExtras(b);
        startActivity(j);
    }
    public void btn_tag2(View view) {
        String tagm = "Diffrential";
        Cursor cursor = mydb.getDataByTag(tagm);
        Cursor cursor_count = mydb.getDataByTag(tagm);
        int i=0;

        while(cursor_count.moveToNext()) {i++;}

        String st1[]=new String[i];
        String st2[]=new String[i];
        String st3[]=new String[i];
        i=0;
        while(cursor.moveToNext()){
            st3[i] = cursor.getString(0);
            st2[i] = cursor.getString(1);
            st1[i] = cursor.getString(2);
            i++;
        }
        Bundle b = new Bundle();
        b.putStringArray("string1",st1);
        b.putStringArray("string2",st2);
        b.putStringArray("string3",st3);
        b.putString("myTag",tagm);
        Intent j = new Intent(Home_Activity.this,Tag_Activity.class);
        j.putExtras(b);
        startActivity(j);

    }
    public void btn_tag3(View view) {
        String tagm = "Intigration";
        Cursor cursor = mydb.getDataByTag(tagm);
        Cursor cursor_count = mydb.getDataByTag(tagm);
        int i=0;

        while(cursor_count.moveToNext()) {i++;}

        String st1[]=new String[i];
        String st2[]=new String[i];
        String st3[]=new String[i];
        i=0;
        while(cursor.moveToNext()){
            st3[i] = cursor.getString(0);
            st2[i] = cursor.getString(1);
            st1[i] = cursor.getString(2);
            i++;
        }
        Bundle b = new Bundle();
        b.putStringArray("string1",st1);
        b.putStringArray("string2",st2);
        b.putStringArray("string3",st3);
        b.putString("myTag",tagm);
        Intent j = new Intent(Home_Activity.this,Tag_Activity.class);
        j.putExtras(b);
        startActivity(j);

    }
    public void btn_tag4(View view) {

       // String[] tagm = getResources().getStringArray(R.array.tag_list);
        String tagm = "Probability";
        Cursor cursor = mydb.getDataByTag(tagm);
        Cursor cursor_count = mydb.getDataByTag(tagm);
        int i=0;

        while(cursor_count.moveToNext()) {i++;}

        String st1[]=new String[i];
        String st2[]=new String[i];
        String st3[]=new String[i];
        i=0;
        while(cursor.moveToNext()){
            st3[i] = cursor.getString(0);
            st2[i] = cursor.getString(1);
            st1[i] = cursor.getString(2);
            i++;
        }
        Bundle b = new Bundle();
        b.putStringArray("string1",st1);
        b.putStringArray("string2",st2);
        b.putStringArray("string3",st3);
        b.putString("myTag",tagm);
        Intent j = new Intent(Home_Activity.this,Tag_Activity.class);
        j.putExtras(b);
        startActivity(j);

    }

    public void do_btn_add(View view) {
        Toast.makeText(this, "add btn", Toast.LENGTH_SHORT).show();
       // startActivity(new Intent(Home_Activity.this,Insert_Activity.class));
    }

   
}
