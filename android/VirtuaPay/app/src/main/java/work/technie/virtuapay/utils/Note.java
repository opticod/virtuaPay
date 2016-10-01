package work.technie.virtuapay.utils;

import java.io.Serializable;

/**
 * Created by scopeinfinity on 30/9/16.
 */

public class Note implements Serializable   {
    static final long serialVersionUID = 578144039354189973L;

    private String code;
    private int vc_id;
    private int amount;
    private boolean is_valid;

    public Note(int vc_id, String code, int amount, boolean is_valid) {
        this.vc_id = vc_id;
        this.code = code;
        this.amount = amount;
        this.is_valid = is_valid;
    }

    public int getAmount() {
        return amount;
    }

    public int get_id() {
        return vc_id;
    }

    public String getCode() {
        return code;
    }

    public boolean isValid() {
        return is_valid;
    }

    public void setInvalid(){
        is_valid = false;
    }
}
