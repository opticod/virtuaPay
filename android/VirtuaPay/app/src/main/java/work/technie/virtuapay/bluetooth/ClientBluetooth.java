package work.technie.virtuapay.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Set;

import work.technie.virtuapay.utils.ClientPaymentInterface;
import work.technie.virtuapay.utils.Note;
import work.technie.virtuapay.utils.Profile;

/**
 * Created by scopeinfinity on 1/10/16.
 */

public class ClientBluetooth {
    private BluetoothCommon common;
    private ClientPaymentInterface clientPaymentInterface;
    private Context context;

    public ClientBluetooth(BluetoothCommon common,ClientPaymentInterface clientPaymentInterface,Context context) {
        this.common = common;
        this.clientPaymentInterface = clientPaymentInterface;
        this.context = context;
    }

    public void listPairedDevices() {
        Set<BluetoothDevice> pairedDevices = common.getAdapter().getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                clientPaymentInterface.addBluetoothDevice(device);
            }
        }
    }

    public BluetoothSocket connect(BluetoothDevice device) {
        BluetoothSocket tmp = null;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(BluetoothCommon.UUID_SDP);
        } catch (IOException e) { }
        common.getAdapter().cancelDiscovery();
        try {
            tmp.connect();
        } catch (IOException e) {
            return null;
        }
        return tmp;
    }

    public ArrayList<Note> sendMoney(BluetoothDevice bd, ArrayList<Note> list) {
        BluetoothSocket socket = connect(bd);
        if(socket == null)
            return null;
        if(!socket.isConnected())
            return null;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            Profile profile = Profile.getInstance(context);
            String name = "None";
            if(profile!=null)
                name = profile.getName();
            Message message = new Message(name,list);
            oos.writeObject(message);
            Message reply = (Message) ois.readObject();
            return reply.getList();
        } catch (Exception e) {

        }
        return null;
    }
}
