package work.technie.virtuapay;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import work.technie.virtuapay.bluetooth.BluetoothCommon;
import work.technie.virtuapay.bluetooth.ManagerBluetooth;
import work.technie.virtuapay.utils.ManagerPaymentInterface;
import work.technie.virtuapay.utils.Note;

public class ManagerAccept extends AppCompatActivity implements ManagerPaymentInterface {
    private ManagerBluetooth manager;
    private BluetoothCommon common;
    private TextView tv_status;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_accept);
        tv_status = (TextView) findViewById(R.id.manager_status);
        manager = new ManagerBluetooth(this, this);
        common = new BluetoothCommon();
        Boolean isBluetoothEnabled = common.init(this);
        if (isBluetoothEnabled != null && isBluetoothEnabled == false) {
            showAlert("Bluetooth Cannot Be Enabled", true);
        }
        if (isBluetoothEnabled != null &&
                isBluetoothEnabled == true) {
            flashMessage("Bluetooth Enabled");
            afterEnablingBluetooth();
        }
        if (isBluetoothEnabled == null) {
            flashMessage("Enabling Bluetooth");
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void afterEnablingBluetooth() {
        if (!manager.init(common)) {
            showAlert("Bluetooth Cannot Start Service", true);
            return;
        }
        manager.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(manager!=null)
            manager.close();
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
                                if (afterThisClose)
                                    ManagerAccept.this.finish();
                            }
                        })
                        .setPositiveButton("Ok", null)
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

    private int validateFromServer(ArrayList<Note> notes) {
        int sum = 0;
        for (Note note : notes)
            sum += note.getAmount();
        return sum;
    }

    @Override
    public ArrayList<Note> validate(String sender, ArrayList<Note> notes) {
        flashMessage("Accepting Notes from [" + sender + "]");
        return new ArrayList<>();
    }

    @Override
    public boolean acceptNotes(final String sender, ArrayList<Note> notes) {
        //Again Validate
        final int sum = validateFromServer(notes);
        flashMessage("Notes Accepted from [" + sender + "]");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add("Amount "+sum+" from [" + sender + "]");
            }
        });
        return true;
    }

    @Override
    public void clientConnected(String name, int uid) {
        flashMessage(name + " connected!!");
    }

    @Override
    public void msgWaitingForClient() {
        flashMessage("Waiting for Payments..");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothCommon.REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK && common.isEnabled()) {
                flashMessage("Bluetooth Enabled");
                afterEnablingBluetooth();
                return;
            } else {
                showAlert("Bluetooth Cannot Be Enabled", true);
            }
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ManagerAccept Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
