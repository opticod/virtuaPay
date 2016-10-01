package work.technie.virtuapay.bluetooth;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import work.technie.virtuapay.utils.ManagerPaymentInterface;
import work.technie.virtuapay.utils.Note;
import work.technie.virtuapay.utils.Profile;

/**
 * Created by scopeinfinity on 30/9/16.
 */

public class ManagerBluetooth extends Thread {
    private BluetoothServerSocket mmServerSocket;
    private BluetoothCommon common;
    private ManagerPaymentInterface paymentPortal;
    private Context context;

    public ManagerBluetooth(Context context, ManagerPaymentInterface paymentPortal) {
        this.paymentPortal = paymentPortal;
        this.context = context;
    }

    public boolean init(BluetoothCommon common) {
        this.common = common;
        BluetoothServerSocket tmp = null;
        try {
            tmp = common.getAdapter().listenUsingRfcommWithServiceRecord(BluetoothCommon.NAME_SDP, BluetoothCommon.UUID_SDP);
        } catch (IOException e) {
            return false;
        }
        mmServerSocket = tmp;
        return true;
    }

    public void run() {
        BluetoothSocket socket = null;
        while (true) {
            try {
                paymentPortal.msgWaitingForClient();
                socket = mmServerSocket.accept();
            }catch (IOException e) {
                break;
            }
            if(socket!=null) {
                manageConnection(socket);
                if(socket.isConnected())
                    try {
                        socket.close();
                    } catch (IOException e) {
                    }
            }
        }
    }

    public void close() {
        if(mmServerSocket!=null)
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
    }

    private void manageConnection(BluetoothSocket socket) {
        if(socket == null)
            return;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        if(ois == null || oos == null)
            return;
        try {
            Message message = (Message) ois.readObject();
            Message reply = acceptMessage(message);
            oos.writeObject(reply);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
        }
    }

    /**
     * Perform Payment Transition Here
     * @param message
     * @return reply
     */
    private Message acceptMessage(Message message) {
        Message reply = null;
        String mineName = "Nobody";
        Profile profile = Profile.getInstance(context);
        if(profile!=null)
            mineName = profile.getName();
        ArrayList<Note> invalids = paymentPortal.validate(message.getSender(), message.getList());
        if(invalids.size() == 0) {
            if(paymentPortal.acceptNotes(message.getSender(), message.getList())) {
                reply = new Message(mineName,Message.MESSAGE_REPLY_SUCCESS, null);
            }
            else reply = new Message(mineName,Message.MESSAGE_REPLY_FAIL,null);
        }
        else
            reply = new Message(mineName,Message.MESSAGE_REPLY_FAIL, invalids);
        return reply;
    }
}
