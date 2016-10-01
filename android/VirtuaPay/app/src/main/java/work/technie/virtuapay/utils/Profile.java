package work.technie.virtuapay.utils;

import android.content.Context;
import android.content.SharedPreferences;

import work.technie.virtuapay.MainActivity;

/**
 * Created by scopeinfinity on 1/10/16.
 */

public class Profile {
    public static final String mPREFERENCES = "mPrefs";
    private static final String UID = "uid";
    private static final String KEY = "key";
    private static final String EMAIL = "email";
    private static final String NAME = "name";


    private String name;
    private String email;
    private String key;
    private int uid;

    /*
     * @param uid
     * @param name
     * @param email
     * @param key
     */
    public Profile(int uid, String name, String email, String key) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.key = key;
    }

    /**
     * Return Object of Current user profile
     *
     * @return object
     */
    public static Profile getInstance(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(mPREFERENCES, Context.MODE_PRIVATE);

        int uid = preferences.getInt(UID, -1);
        if(uid == -1)
            return null;
        String name = preferences.getString(NAME, null);
        String email = preferences.getString(EMAIL, null);
        String key = preferences.getString(KEY, null);

        Profile profile = new Profile(uid,name,email,key);
        return profile;
    }

    public int getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public void save(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(mPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(UID, getUid());
        editor.putString(KEY, getKey());
        editor.putString(EMAIL, getEmail());
        editor.putString(NAME, getName());
        editor.apply();
    }

    public static void logout(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(mPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(UID, -1);
        editor.putString(KEY, null);
        editor.putString(EMAIL, null);
        editor.putString(NAME, null);
        editor.apply();
    }

    /**
     * Check if key is still valid
     * @return
     */
    public boolean checkOnline() {
        return true;
    }
}
