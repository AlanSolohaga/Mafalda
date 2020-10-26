package com.project.mafalda.model;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;

import com.project.mafalda.R;

public class SonidoClick {
    private static SonidoClick mIntance;
    private MediaPlayer click;

    private static Context mContext;

    public SonidoClick(Context context) {
        mContext = context;
        click = MediaPlayer.create(mContext,R.raw.plop);
    }

    public static synchronized SonidoClick getInstance(Context context){
        if(mIntance == null){
                mIntance = new SonidoClick(context);
        }
        return mIntance;
    }

    public void play(){
        click.start();
    }


}
