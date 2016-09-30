package work.technie.virtuapay.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by anupam on 1/10/16.
 */

public class SyncKeys extends AsyncTask<String, Void, Void> {

    private static final String mPREFERENCES = "mPrefs";
    public final String LOG_TAG = SyncKeys.class.getSimpleName();
    private final String mEmail;
    private final String mPassword;
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String resultJsonStr = null;
    private SharedPreferences sharedpreferences;
    private Context mContext;

    SyncKeys(Context context, String email, String password) {
        mContext = context;
        mEmail = email;
        mPassword = password;
    }

    private boolean saveDataFromJson(String resultJsonStr)
            throws JSONException {

        try {
            JSONObject jsonObject = new JSONObject(resultJsonStr);
            String amount = jsonObject.getString("amount");
            JSONArray validNotes = jsonObject.getJSONArray("validnotes");

            Vector<ContentValues> cVVector = new Vector<>(validNotes.length());

            for (int i = 0; i < validNotes.length(); i++) {

                String key = validNotes.get(i).toString();

                ContentValues InfoValues = new ContentValues();

                InfoValues.put(Contract.Keys.U_ID, "0");
                InfoValues.put(Contract.Keys.AMOUNT, "10");
                InfoValues.put(Contract.Keys.CODE, key);
                InfoValues.put(Contract.Keys.VALID, "1");
                cVVector.add(InfoValues);
            }
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                Db db = new Db(mContext);
                db.open();
                db.bulkInsert(cvArray);
                db.close();
            }
            return false;
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
            final String BASE_URL = "http://172.16.8.208/web/sync.php?";
            final String UID_PARAM = "uid";
            final String KEY_PARAM = "key";

            SharedPreferences preferences = mContext.getSharedPreferences(mPREFERENCES, Context.MODE_PRIVATE);

            final String UID = "uid";
            final String KEY = "key";

            String uid = preferences.getString(UID, "-1");
            String key = preferences.getString(KEY, "-1");

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(UID_PARAM, uid)
                    .appendQueryParameter(KEY_PARAM, key)
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