package com.example.saurav.maths;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by saurav on 28-Aug-18.
 */

public class Mysingleton {

    private static Mysingleton myInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private  Mysingleton(Context context){

        mCtx = context;
        requestQueue =getRequestQueue();
    }

    public  RequestQueue getRequestQueue(){

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public  static  synchronized  Mysingleton getInstance(Context context){

        if(myInstance == null){
            myInstance = new Mysingleton(context);
        }
        return  myInstance;
    }


    public<T> void addToRequestque(Request<T> request){
        requestQueue.add(request);
    }
}
