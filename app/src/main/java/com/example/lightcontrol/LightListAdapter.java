package com.example.lightcontrol;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Aakash on 26.04.2017.
 */

public class LightListAdapter extends BaseAdapter{

    private Context context;
    public static ArrayList<String> lightListName;

    //------------------------------------------------------------------------
    public LightListAdapter(Context c){
        this.context = c;
        lightListName = new ArrayList<String>();
        for(int i = 0; i < 8; i++){
            lightListName.add("Light " + Integer.toString(i));
        }
    }

    //------------------------------------------------------------------------

    private void printLog (final String text){
        Log.i("DALITEMP", "[" +getClass().getName()+  "]>>\t" + text);
    }
    //------------------------------------------------------------------------
    @Override
    public int getCount() {
        return lightListName.size();
    }
    //------------------------------------------------------------------------
    @Override
    public Object getItem(int position) {
        return lightListName.get(position);
    }
    //------------------------------------------------------------------------
    @Override
    public long getItemId(int position) {
        return position;
    }
    //------------------------------------------------------------------------
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View myRowView = convertView;
        if(myRowView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myRowView = inflater.inflate(R.layout.listrow,parent,false);
        }

        TextView lightTitle = (TextView)myRowView.findViewById(R.id.lightName);
        lightTitle.setText(lightListName.get(position));


        Switch statusButton = (Switch)myRowView.findViewById(R.id.switchTheLight);
        statusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DataSenderModule.getInstance().sendBroadcastToLightControl("Address", Integer.toString(position));
                    DataSenderModule.getInstance().sendBroadcastToLightControl("State","on");
                }
                else {
                    DataSenderModule.getInstance().sendBroadcastToLightControl("Address", Integer.toString(position));
                    DataSenderModule.getInstance().sendBroadcastToLightControl("State","off");
                }
            }
        });

        return myRowView;
    }

}
