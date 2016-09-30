package work.technie.virtuapay.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import java.util.UUID;

/**
 * Created by scopeinfinity on 30/9/16.
 */

public class BluetoothCommon {
    public static final String NAME_SDP =   "VirtuaPay";
    public static final UUID UUID_SDP   =   UUID.fromString("630d27d0-8732-11e6-ae22-56b6b6499611");

    public static final int REQUEST_ENABLE_BT  =   1001;

    private BluetoothAdapter adapter;


    /**
     * Get Bluetooth Adapter
     * returns null, to check again
     * @return isConnectionPossible
     */
    public Boolean init(Activity activity) {
        adapter = BluetoothAdapter.getDefaultAdapter();

        if (adapter == null)
            return false;

        if(!adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return null;
        }
        return true;
    }

    public boolean isEnabled() {
        return adapter.isEnabled();
    }

    public void enabledDiscoverabilityIfNeeded(Activity activity) {
        if(adapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
            return;

        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        activity.startActivity(discoverableIntent);
    }

    public BluetoothAdapter getAdapter() {
        return adapter;
    }
}
