package com.example.lightcontrol;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Marcel on 05.04.2017.
 */

public class DataSenderModule {

    private static DataSenderModule _instance = null;

    private final String EVENT_KEY_MAINACTIVITY = "MainActivity" ;
    private final String EVENT_KEY_LIGHTCONTROL = "LightControl";
    private final String EVENT_KEY_SINGLELIGHT = "SingleLight";

    private Context context;

    //------------------------------------------------------------------------
    public static DataSenderModule getInstance(){
        if (_instance == null){
            _instance = new DataSenderModule();
        }
        return _instance;
    }

    //------------------------------------------------------------------------

    private DataSenderModule(){
        printLog("DataSenderModule created...");
    }

    //------------------------------------------------------------------------

    private void printLog (final String text){
        Log.i("DALITEMP", "[" +getClass().getName()+  "]>>\t" + text);
    }


    //------------------------------------------------------------------------

    private void init(){
        printLog("init...");
    }

    //------------------------------------------------------------------------


    public void setContext(Context context) {
        this.context = context;
        init();
    }

    //------------------------------------------------------------------------
    public void sendBroadcastToMainActivity(final String key, final String command){
        printLog("sendBroadcastToMainActivity: [Key]:" + key + "[Command]:" + command);
        Intent intent = new Intent(EVENT_KEY_MAINACTIVITY);
        intent.putExtra("Key", key);
        intent.putExtra("Command", command);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    //------------------------------------------------------------------------

    public void sendBroadcastToLightControl(final String key, final String command){
        printLog("sendBroadcastToLightControl: [Key]:" + key + "[Command]:" + command);
         Intent intent = new Intent(EVENT_KEY_LIGHTCONTROL);
        intent.putExtra("Key", key);
        intent.putExtra("Command", command);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    //------------------------------------------------------------------------

    public void sendBroadcastToSingleLight(final String key, final String command){
        printLog("sendBroadcastToSingleLight: [Key]:" + key + "[Command]:" + command);
        Intent intent = new Intent(EVENT_KEY_SINGLELIGHT);
        intent.putExtra("Key", key);
        intent.putExtra("Command", command);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    //------------------------------------------------------------------------

    public void sendBroadcastToAll(final String key, final String command){
        printLog("sendBroadcastToAll...");
        sendBroadcastToLightControl(key, command);
        sendBroadcastToMainActivity(key, command);
    }
    //------------------------------------------------------------------------
}
