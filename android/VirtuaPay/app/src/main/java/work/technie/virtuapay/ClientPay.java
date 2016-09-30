package work.technie.virtuapay;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ClientPay extends AppCompatActivity {
    public static final String INTENT_DATA_AMOUNT_KEY = "amount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_pay);
        Intent intent = getIntent();
        int amount = -1;
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
}
