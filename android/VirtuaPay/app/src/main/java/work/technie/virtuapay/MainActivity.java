package work.technie.virtuapay;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv_account_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        tv_account_balance = (TextView) findViewById(R.id.account_balance);

    }

    public void acceptPay(View b) {
        Intent intent = new Intent(this,ManagerAccept.class);
        startActivity(intent);
    }

    public void payOther(View b) {
        Intent intent = new Intent(this,ClientPay.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillFields();
    }

    /**
     * Fill up field for frontend
     */
    private void fillFields() {
        tv_account_balance.setText("Rs. 200");
    }


}
