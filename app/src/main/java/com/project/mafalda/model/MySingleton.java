package com.project.mafalda.model;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mIntance;
    private RequestQueue requestQueue;
    private static Context mCtx;


    private MySingleton(Context context){
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MySingleton getInstance(Context context){
        if(mIntance==null){
            mIntance = new MySingleton(context);
        }
        return mIntance;
    }
    public  RequestQueue getRequestQueue(){
        if(requestQueue ==null){
            requestQueue = Volley.newRequestQueue(mCtx);
        }
        return requestQueue;
    }

    public <T>void addToreques(Request<T> request){
        requestQueue.add(request);
    }
}
