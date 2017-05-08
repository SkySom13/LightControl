package com.example.lightcontrol;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

//         ____        _ _      _                  _____
//        |  _ \  __ _| (_)    / \   _ __  _ __   |_   _|__ _ __ ___  _ __
//        | | | |/ _` | | |   / _ \ | '_ \| '_ \    | |/ _ \ '_ ` _ \| '_ \
//        | |_| | (_| | | |  / ___ \| |_) | |_) |   | |  __/ | | | | | |_) |
//        |____/ \__,_|_|_| /_/   \_\ .__/| .__/    |_|\___|_| |_| |_| .__/
//                                  |_|   |_|                        |_|
//        +-------------------------+
//        |Dali Connection App Temp |
//        |   Coder: Marcel Becks   |
//        +-------------------------+


public class TcpClientModule {

	private static TcpClientModule _instance = null;

	private BufferedReader bufferedReader;
	private  Socket socket;
	private DataOutputStream dataOutputStream;
	private boolean serverRunActive;
    private boolean runMyCode  = true;

	// ---------------------------------------------------------------------------------------

	public static TcpClientModule getInstance() {
		if (_instance == null) {
			_instance = new TcpClientModule();
		}
		return _instance;
	}

	// ---------------------------------------------------------------------------------------

	public TcpClientModule() {
		printLog("created...");
		init();

	}

	// ---------------------------------------------------------------------------------------

	private void printLog (final String text){
		Log.i("DALITEMP", "[" +getClass().getName()+  "]>>\t" + text);
	}

	// ---------------------------------------------------------------------------------------

	private void init() {
	//print("init...");

		try {

			serverRunActive = false;
			socket.close();
			bufferedReader.close();
			dataOutputStream.close();

			bufferedReader = null;
			socket = null;
			dataOutputStream = null;

		} catch (Exception e) {
			
		}

	}

	// ---------------------------------------------------------------------------------------

	public void connectToServer(final String ip, final int port) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					serverRunActive = true;
					socket = new Socket(ip, port);
					dataOutputStream = new DataOutputStream(socket.getOutputStream());
					bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					if (!socket.isConnected()) {
						eventCanNotConnected();
					}

					readFromServer();
					eventConnected();

				} catch (Exception e) {
					eventCanNotConnected();
				}
			}
		}).start();


	}

	// ---------------------------------------------------------------------------------------

	private void readFromServer() {

	//	print("start recv...");

		new Thread(new Runnable() {

			@Override
			public void run() {

				while (serverRunActive) {

					try {

						if (!socket.isConnected())
							break;

						String recvData = bufferedReader.readLine();
						printLog("RECVDATA: " + recvData);
                        if(runMyCode){
                            DataSenderModule.getInstance().sendBroadcastToLightControl("TCP",recvData);
                        }
						if (recvData == null) {
							break;
						}

					} catch (Exception e) {
						//print("error: " + e.toString());
					}

				}
				init();
				eventDisconnected();
			}
		}).start();

	}

	// ---------------------------------------------------------------------------------------

	public void sendDataToServer(final String data) {
		printLog("sendDataToServer: " + data);

        if(data.contains("list")){
            runMyCode = false;
        }
        else {
            runMyCode = true;
        }

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					dataOutputStream.writeBytes(data + '\r'+'\n');
				} catch (Exception e) {

				}
			}
		}).start();


	}

	// ---------------------------------------------------------------------------------------

	public void disconnection() {
		printLog("disconnection...");
		init();

	}

	// ---------------------------------------------------------------------------------------

	private void eventConnected() {
		printLog("eventConnected...");
		DataSenderModule.getInstance().sendBroadcastToMainActivity("Socket","connected...");

	}

	// ---------------------------------------------------------------------------------------

	private void eventDisconnected() {
		printLog("eventDisconnected...");
	}

	// ---------------------------------------------------------------------------------------

	private void eventCanNotConnected() {

		printLog("eventCanNotConnected...");
	}

	// ---------------------------------------------------------------------------------------
}
