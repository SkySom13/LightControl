package com.example.lightcontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*******************************************************
     *          Class Variables
     ********************************************************/
    private final String EVENT_RECV_KEY = "MainActivity";
    private EventReceiver eventReceiver;
    Fragment fragment;
    FragmentManager fm;
    private String fragmentkey;

    /*******************************************************
     *          OnCreate Method (Creates the App)
     ********************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        /* Putting Fragment Part in MainActivity Frame*/
        fm = getSupportFragmentManager();
        fragmentUpdate(fragmentkey);
    }

    /***********************************************************
     *       OnPause Method (When you press the Middle Button)
     ***********************************************************/

    @Override
    protected void onPause() //When we are out of the app temporarily
    {
        super.onPause();
    }

    private void printLog(final String text) {
        Log.i("DALITEMP", "[" + getClass().getName() + "]>>\t" + text);
    }


    //------------------------------------------------------------------------

    private void init() {
        printLog("init...");

        eventReceiver = new EventReceiver(new Handler());
        LocalBroadcastManager.getInstance(this).registerReceiver(eventReceiver ,new IntentFilter(EVENT_RECV_KEY));

        DataSenderModule.getInstance();
        DataSenderModule.getInstance().setContext(this);
    }

    //------------------------------------------------------------------------

    private void fragmentUpdate(String fragment_view) {
        printLog("fragmentUpdate...");

        printLog("FragmentCount " + fm.getBackStackEntryCount());
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            printLog("kill old fragment");
            fm.popBackStack();
        }

        FragmentTransaction ft = fm.beginTransaction();

        if(fragment_view.equals("singleLight")){
            fragment = new SingleLight();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(R.id.fragmentcontainer, fragment);
        }
        else if(fragment_view.equals("lightControl"))
        {
            fragment = new LightControl();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.replace(R.id.fragmentcontainer,fragment);
        }
        ft.commit();

    }

    //------------------------------------------------------------------------

    private void guiInit() {
        printLog("guiInit...");

    }
    //------------------------------------------------------------------------

    private class EventReceiver extends BroadcastReceiver {

        private final Handler handler;

        public EventReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    printLog("+++eventReceiver+++");
                    String key = intent.getStringExtra("Key");
                    String cmd = intent.getStringExtra("Command");

                    printLog("Got Key: " + key);
                    printLog("Got Command: " + cmd);
                    
                    if(key.contains("fragmentChange")){
                        fragmentkey = cmd;
                    }


                }
            });




        }
    }

    //------------------------------------------------------------------------

}