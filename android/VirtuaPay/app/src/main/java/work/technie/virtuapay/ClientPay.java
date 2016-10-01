package work.technie.virtuapay;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import work.technie.virtuapay.bluetooth.BluetoothCommon;
import work.technie.virtuapay.bluetooth.ClientBluetooth;
import work.technie.virtuapay.utils.ClientPaymentInterface;
import work.technie.virtuapay.utils.Note;

public class ClientPay extends AppCompatActivity implements ClientPaymentInterface {
    public static final String INTENT_DATA_AMOUNT_KEY = "amount";
    private BluetoothCommon common;
    private TextView tv_status;
    private ListView listView;
    private CustomAdapter adapter;
    private ClientBluetooth clientBluetooth;
    private CashManager cashManager;
    private int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_pay);
        showProgress(false);
        Intent intent = getIntent();
        amount = -1;
        if(intent.hasExtra(INTENT_DATA_AMOUNT_KEY))
            amount = intent.getIntExtra(INTENT_DATA_AMOUNT_KEY,-1);
        if(amount==-1) {
            showAlert("Some Intent Error!",true);
            return;
        }
        if(amount%10!=0) {
            showAlert("Please Give Amount a multiple of 10",true);
            return;
        }
        if(amount<=0) {
            showAlert("Amount should be greater than 0",true);
            return;
        }

        tv_status = (TextView) findViewById(R.id.client_status);
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomAdapter(this);
        listView.setAdapter(adapter);
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
        cashManager = CashManager.getInstance(this);
    }

    private void showProgress(boolean toshow){
        int vis1 = View.VISIBLE, vis2 = View.GONE;
        if(!toshow) {vis2=View.VISIBLE ; vis1 = View.GONE;}

        ((ProgressBar)findViewById(R.id.progress)).setVisibility(vis1);
        ((ProgressBar)findViewById(R.id.progress)).setVisibility(vis2);

    }

    private void flashMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_status.setText(message);
            }
        });
    }
    private void showAlert(final String message, final boolean afterThisClose) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(ClientPay.this)
                        .setMessage(message)
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if(afterThisClose)
                                    ClientPay.this.finish();
                            }
                        })
                        .setPositiveButton("Ok",null)
                        .show();
            }
        });

    }

    private void afterEnablingBluetooth() {
        clientBluetooth = new ClientBluetooth(common,this,this);
        clientBluetooth.listPairedDevices();
    }

    @Override
    public void addBluetoothDevice(BluetoothDevice bd) {
        adapter.add(bd);
    }

    class CustomAdapter extends ArrayAdapter<BluetoothDevice> {
        private Activity activity;
        public CustomAdapter(Activity activity) {
            super(activity, R.layout.item_bluetooth);
            this.activity = activity;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v == null)
                v = activity.getLayoutInflater().inflate(R.layout.item_bluetooth,null);
            TextView name = (TextView) v.findViewById(R.id.bt_name);
            TextView mac = (TextView) v.findViewById(R.id.bt_mac);
            ImageButton send = (ImageButton) v.findViewById(R.id.bt_send);
            final BluetoothDevice bd = getItem(position);
            name.setText(bd.getName());
            mac.setText(bd.getAddress());
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread("Client Payment") {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_status.setText("Transaction Begin!");
                                }
                            });
                            final String status = sendMoney(bd);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_status.setText(status);
                                }
                            });
                        }
                    }.start();

                }
            });
            return v;

        }
    }

    private String sendMoney(BluetoothDevice bd) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(true);
            }
        });
        ArrayList<Note> list = cashManager.getNotes(amount);
        ArrayList<Note> falseNote = clientBluetooth.sendMoney(bd,list);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
            }
        });
        //For Now, either accept all or none..
        if(falseNote == null) {
            showAlert("Transaction Failed!!",false);
            return "Transaction Failed!!";
        }
        if(falseNote.size() > 0) {
            showAlert("False Notes Found! Transaction Cancelled",false);
            return "False Notes Found! Transaction Cancelled";
        }
        showAlert("Transaction Successful",true);
        return "Transaction Successful";

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
