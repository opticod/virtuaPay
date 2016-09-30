package work.technie.virtuapay;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import work.technie.virtuapay.bluetooth.BluetoothCommon;
import work.technie.virtuapay.bluetooth.ManagerBluetooth;
import work.technie.virtuapay.utils.ManagerPaymentInterface;
import work.technie.virtuapay.utils.Note;

public class ManagerAccept extends AppCompatActivity implements ManagerPaymentInterface {
    private ManagerBluetooth manager;
    private BluetoothCommon common;
    private TextView tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_accept);
        tv_status = (TextView) findViewById(R.id.manager_status);
        manager = new ManagerBluetooth(this);
        common = new BluetoothCommon();
        Boolean isBluetoothEnabled = common.init(this);
        if(isBluetoothEnabled!=null && isBluetoothEnabled == false) {
            showAlert("Bluetooth Cannot Be Enabled",true);
        }
        if(isBluetoothEnabled!=null &&
                isBluetoothEnabled == true) {
            flashMessage("Bluetooth Enabled");
            afterEnablingBluetooth();
        }
        if(isBluetoothEnabled == null) {
            flashMessage("Enabling Bluetooth");
        }

    }

    private void afterEnablingBluetooth() {
        if(!manager.init(common)) {
            showAlert("Bluetooth Cannot Start Service",true);
            return;
        }
        manager.start();
    }

    private void showAlert(final String message, final boolean afterThisClose) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(ManagerAccept.this)
                        .setMessage(message)
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if(afterThisClose)
                                    ManagerAccept.this.finish();
                            }
                        })
                        .setPositiveButton("Ok",null)
                        .show();
            }
        });

    }

    private void flashMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_status.setText(message);
            }
        });
    }

    @Override
    public ArrayList<Note> validate(String sender, ArrayList<Note> notes) {
        return null;
    }

    @Override
    public boolean acceptNotes(String sender, ArrayList<Note> notes) {
        return false;
    }

    @Override
    public void clientConnected(String name, int uid) {
        flashMessage(name+" connected!!");
    }

    @Override
    public void msgWaitingForClient() {
        flashMessage("Waiting for Payments..");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == BluetoothCommon.REQUEST_ENABLE_BT) {
            if(resultCode == RESULT_OK && common.isEnabled()) {
                flashMessage("Bluetooth Enabled");
                afterEnablingBluetooth();
                return;
            }
            else {
                showAlert("Bluetooth Cannot Be Enabled",true);
            }
        }

    }
}
