package work.technie.virtuapay.utils;

/**
 * Created by scopeinfinity on 1/10/16.
 */

public class Profile {
    private String name;
    private String email;
    private int type;
    private int uid;

    Profile(int uid,String name,String email,int type) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.type = type;
    }

    public int getType() {
        return type;
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

    /**
     * Return Object of Current user profile
     * @return object
     */
    public static Profile getInstance() {
        return null;
    }
}
