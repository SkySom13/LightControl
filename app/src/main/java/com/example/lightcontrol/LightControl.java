package com.example.lightcontrol;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LightControl extends Fragment implements AdapterView.OnItemLongClickListener, Button.OnClickListener{

    private final String EVENT_RECV_KEY = "LightControl";
    private EventReceiver eventReceiver;

    private ListView myLightList;
    private LightListAdapter mAdapter;

    private TextView myText;
    private Dialog dialog;
    private EditText mylightEdit;
    private Button saveBTN, cancelBTN;
    private int myPos;

    private View rootView;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printLog("created...");
        init();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_lightcontrol, container, false);

        guiInit(rootView);

        return rootView;
    }

    //------------------------------------------------------------------------

    private void printLog (final String text){
        Log.i("DALITEMP", "[" +getClass().getName()+  "]>>\t" + text);
    }


    //------------------------------------------------------------------------

    private void init(){
        printLog("init...");
        eventReceiver = new EventReceiver(new Handler());
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(eventReceiver ,new IntentFilter(EVENT_RECV_KEY));
        TcpClientModule.getInstance().connectToServer("192.168.0.140",4000);//5.5 for the Gateway
    }

    //------------------------------------------------------------------------

    private void guiInit(View view){
        printLog("guiInit...");

        myLightList = (ListView)view.findViewById(R.id.lightControlList);
        mAdapter = new LightListAdapter(getContext());
        myLightList.setAdapter(mAdapter);
        myLightList.setOnItemLongClickListener(this);


        dialog = new Dialog(getContext());
        dialog.setTitle("Light Configuration");
        dialog.setContentView(R.layout.light_edit_dialog);
        mylightEdit = (EditText) dialog.findViewById(R.id.light_name);
        mylightEdit.setHint("Enter Light Name here!");


        saveBTN =(Button) dialog.findViewById(R.id.sv_btn);
        cancelBTN = (Button) dialog.findViewById(R.id.cncl_btn);
        saveBTN.setOnClickListener(this);
        cancelBTN.setOnClickListener(this);

    }

    //------------------------------------------------------------------------

    private void guiUpdate(){
        printLog("guiUpdate...");

    }

    //------------------------------------------------------------------------

    @Override
    public void onDestroy() {
        //printLog ("destroyed...");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(eventReceiver);
        super.onDestroy();
    }
    //------------------------------------------------------------------------
    @Override
    /** THIS FUNCTION IS FOR LISTVIEW PRESSES**/
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        printLog("LongPress Successful!!!!!!!!!!!!!!!!!!!!!!!");
        myPos = position;
        dialog.show();

        return false;
    }
    //------------------------------------------------------------------------
    @Override
    /** THIS FUNCTION IS ONLY FOR BUTTON PRESSES**/
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sv_btn:
                LightListAdapter.lightListName.set(myPos, String.valueOf(mylightEdit.getText()+ " ("+myPos+")"));
                mylightEdit.setText("");
                dialog.hide();
            case R.id.cncl_btn:
                dialog.hide();
        }

    }


    //------------------------------------------------------------------------

    private class EventReceiver extends BroadcastReceiver {

        private final Handler handler;
        private int addr;

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

                    if(key.contains("Address")){
                        addr = Integer.parseInt(cmd);
                    }

                    if(key.contains("State")){
                        if(cmd.contains("on")){
                            TcpClientModule.getInstance().sendDataToServer("set light " + Integer.toString(addr) + " = 254");
                            Toast.makeText(getActivity(),"Light "+addr+" ON",Toast.LENGTH_SHORT).show();
                        }
                        if(cmd.contains("off")){
                            TcpClientModule.getInstance().sendDataToServer("set light "+ Integer.toString(addr) + " = 0");
                            Toast.makeText(getActivity(),"Light "+addr+" OFF",Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            });




        }
    }
}
