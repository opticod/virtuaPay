package work.technie.virtuapay;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import work.technie.virtuapay.bluetooth.BluetoothCommon;
import work.technie.virtuapay.bluetooth.ManagerBluetooth;
import work.technie.virtuapay.data.Contract;
import work.technie.virtuapay.data.Db;
import work.technie.virtuapay.data.SyncKeys;
import work.technie.virtuapay.utils.MainActivityInterface;
import work.technie.virtuapay.utils.ManagerPaymentInterface;
import work.technie.virtuapay.utils.Note;
import work.technie.virtuapay.utils.Profile;

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

    /**
     * Returns null is success
     * @param notes
     * @return message
     */
    private String attemptTransferUsingServer(ArrayList<Note> notes) {
        if(notes == null)
            return "Notes not Received";
        int sum = 0;
        for (Note note : notes)
            sum += note.getAmount();
        ArrayList<String> codes = new ArrayList<>();
        for (Note note : notes) {
            codes.add(note.getCode());
            Log.i("***********", "attemptTransferUsingServer: "+note.getCode());
        }
        Transfer transfer = new Transfer(this,sum,codes);
        transfer.execute();
        if(transfer.lastmessage!=null)
            showAlert(transfer.lastmessage,false);
        else
            return String.valueOf(sum);
        return transfer.lastmessage;
    }

    private boolean isInteger(String x) {
        try {
            Integer.parseInt(x);
            return true;
        }catch (Exception e) {
        }
        return false;
    }

    @Override
    public boolean acceptNotes(final String sender, ArrayList<Note> notes) {
        //Again Validate
        Log.i("***********", "acceptNotes: notes is null "+(notes==null));
        final String message = attemptTransferUsingServer(notes);
        if(isInteger(message)) {
            flashMessage("Notes Accepted from [" + sender + "]");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.add("Amount "+message+" from [" + sender + "] Received");
                }
            });
        }
        else  {
            flashMessage("Notes Rejected from [" + sender + "]");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.add("Amount from [" + sender + "] REJECTED\n"+message);
                }
            });
        }
        return true;
    }

    @Override
    public void clientConnected(final String name, int uid) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add("Attempt from  [" + name + "]");
            }
        });
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

    class Transfer extends AsyncTask<String, Void, Void> {

        public final String LOG_TAG = Transfer.class.getSimpleName();
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        private String resultJsonStr = null;
        private Context mContext;

        private int t_sum;
        private ArrayList<String> codes;
        private String lastmessage;

        public Transfer(Context context,int t_sum,ArrayList<String> codes) {
            mContext = context;
            this.t_sum = t_sum;
            this.codes = codes;
        }

        public String getLastmessage() {
            return lastmessage;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        private boolean saveDataFromJson(String resultJsonStr)
                throws JSONException {
            lastmessage = "Some Error Occured!";

            try {
                JSONObject jsonObject = new JSONObject(resultJsonStr);
                lastmessage = jsonObject.getString("message");
                if(!jsonObject.getString("status").equals("Done")) {
                    Log.e("Log**********", "saveDataFromJson: "+ jsonObject.getString("message"));
                    return false;
                }
                lastmessage = null;
                return true;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected Void doInBackground(String... params) {

            boolean result = false;
            try {
                final String BASE_URL = LoginActivity.SERVER_URL+"web/shop.php?";
                final String UID_PARAM = "uid";
                final String ST_PARAM = "sumtransfer";
                final String NOTES_PARAM = "notes";

                Profile profile = Profile.getInstance(mContext);

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(UID_PARAM, ""+profile.getUid())
                        .appendQueryParameter(ST_PARAM, t_sum+"")
                        .appendQueryParameter(NOTES_PARAM, new JSONArray(codes).toString())
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                resultJsonStr = buffer.toString();
                saveDataFromJson(resultJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
}
