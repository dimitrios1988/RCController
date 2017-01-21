package dimecho.com.rccontroller;

import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.content.res.TypedArrayUtils;

import java.io.IOException;
import java.net.*;
import java.nio.*;

/**
 * Created by dimitrios1988 on 04/12/2016.
 */

public class UDPClient {

    private static UDPClient instance;
    private final InetAddress address;
    private final int port;
    private DatagramSocket clientSocket;
    private AsyncTask<int[], Void, Void> async_client;

    private UDPClient() throws UnknownHostException {
        this.address = InetAddress.getByName("192.168.51.1");
        this.port = 56890;
    }

    public static UDPClient getInstance() throws UnknownHostException {
        if (instance == null){
            synchronized (UDPClient.class){
                if (instance == null){
                    instance = new UDPClient();
                }
            }
        }
        return instance;
    }

    public void sendData(final int[] data) {
        async_client = new AsyncTask<int[], Void, Void>() {
            @Override
            protected Void doInBackground(int[]... ints) {
                ByteBuffer bf = ByteBuffer.allocate(12);
                for(int i=0 ;i <3; i++){
                    for (byte b : ByteBuffer.allocate(4).putInt(data[i]).array()){
                        bf.put(b);
                    }
                    //if(i!=2) bf.put((byte) '\0');
                }
                byte[] databytes = bf.array();
                DatagramPacket sendPacket = new DatagramPacket(databytes, databytes.length, address, port);
                try {
                    clientSocket = new DatagramSocket();
                    clientSocket.setBroadcast(true);
                    clientSocket.send(sendPacket);
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        if (Build.VERSION.SDK_INT>=11) async_client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else async_client.execute();
    }


}

