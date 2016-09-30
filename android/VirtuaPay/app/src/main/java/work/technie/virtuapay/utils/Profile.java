package work.technie.virtuapay.utils;

import android.content.Context;

/**
 * Created by scopeinfinity on 1/10/16.
 */

public class Profile {
    public static final String mPREFERENCES = "mPrefs";
    private String name;
    private String email;
    private int uid;

    Profile(int uid,String name,String email,int type) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    /**
     * Return Object of Current user profile
     *
     * @return object
     */
    public static Profile getInstance(Context context) {
        /*
        SharedPreferences preferences = context.getSharedPreferences(mPREFERENCES, Context.MODE_PRIVATE);

        final String UID = "uid";
        final String KEY = "key";
        String uid = preferences.getString(UID, "-1");
        String name = preferences.getString(KEY, "-1");

        Profile profile = new Profile();*/
        return null;
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

    public void save(Context context) {

    }
}
