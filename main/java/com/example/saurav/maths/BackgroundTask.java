package com.example.saurav.maths;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by saurav on 29-Aug-18.
 */

class BackgroundTask {

    private Context context;
    private ArrayList<Movie> my_arrayList = new ArrayList<>();

    private String json_url;

    BackgroundTask(Context ctx, String json_url){
        this.context=ctx;
        this.json_url = json_url;
    }

    ArrayList<Movie> getMyLists() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST,json_url, (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int c = 0;
                        while(c<response.length()) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(c);
                                Movie arr = new Movie(jsonObject.getString("f_title"), jsonObject.getString("f_data"), jsonObject.getString("f_tag"));


                                Log.d("DATA", "\n"+"TAG : "+jsonObject.getString("f_tag")+
                                    "\n"+"TITLE : "+jsonObject.getString("f_title")+
                                    "\n"+"DATA : "+jsonObject.getString("f_data")+"\n");


                                my_arrayList.add(arr);
                                c++;
                            } catch (JSONException e) {
                                Toast.makeText(context, "Exception....!", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                        Log.d("checking DATA is Coming or not", " \n\n"+"onResponse: "+my_arrayList.size()+" \n\n");
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something went wrong....!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        Log.d("kk", " \n\n"+"onDown: "+my_arrayList.size()+" \n\n");

        Mysingleton.getInstance(context).addToRequestque(jsonArrayRequest);



       // jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(4000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        return my_arrayList;

    }

}
