package work.technie.virtuapay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync:


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
