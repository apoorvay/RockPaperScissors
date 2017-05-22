package com.example.apoorva.rockpaperscissors;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by Shuo on 11/3/2016.
 */

public class CommHandler implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    MainActivity mainActivity;
    GoogleApiClient googleApiClient;

    public static final String mMOVE = "mobile move";
    public static final String wMOVE = "watch move";

    static Handler UIHandler=null;
    public CommHandler(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        googleApiClient = new GoogleApiClient.Builder(mainActivity)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
        UIHandler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0){
                    mainActivity.getMove((int)msg.obj);
                }
                super.handleMessage(msg);
            }
        };
    }

    public void sendMove(int move){
        new NumberSenderAsync().execute(move);
    }
    private class NumberSenderAsync extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/" + mMOVE);
            putDataMapRequest.getDataMap().putInt(mMOVE, params[0]);
            PutDataRequest putDataRequest=putDataMapRequest.asPutDataRequest();
            Wearable.DataApi.putDataItem(googleApiClient,putDataRequest);

            return null;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(googleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

        for(DataEvent event:dataEventBuffer){
            if(event.getType()==DataEvent.TYPE_CHANGED){
                DataItem item=event.getDataItem();
                if(item.getUri().getPath().compareTo("/"+wMOVE)==0){
                    DataMap dataMap= DataMapItem.fromDataItem(item).getDataMap();
                    int move=dataMap.getInt(wMOVE);
                    Message msg=UIHandler.obtainMessage(0,move);
                    msg.sendToTarget();
                }
            }
        }
    }
}
