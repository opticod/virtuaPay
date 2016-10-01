package work.technie.virtuapay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import work.technie.virtuapay.utils.Profile;

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
        final EditText editText = new EditText(MainActivity.this);
        editText.setHint("Enter Amount(Multiple of 10)");
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Enter amount")
                .setView(editText)
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this,ClientPay.class);
                        String amt = editText.getText().toString().trim();
                        try {
                            int amount = Integer.parseInt(amt);
                            if(amount%10 !=0 )
                                throw new RuntimeException("Div by 10 required");
                            intent.putExtra(ClientPay.INTENT_DATA_AMOUNT_KEY, amount);
                            startActivity(intent);
                        }catch (Exception e) {
                            Toast.makeText(MainActivity.this,
                                    "Please enter an Integer, which is multiple of 10",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();

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
        tv_account_balance.setText("None");
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
            case R.id.action_logout:
                Profile.logout(this);
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
