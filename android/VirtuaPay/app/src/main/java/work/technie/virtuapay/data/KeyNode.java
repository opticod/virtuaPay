package work.technie.virtuapay.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @brief Model used to save key entries in database for app.
 * <p>
 * Created by Anupam (opticod) on 1/10/16.
 */
public class KeyNode implements Parcelable {
    public final Parcelable.Creator<KeyNode> CREATOR = new Parcelable.Creator<KeyNode>() {
        @Override
        public KeyNode createFromParcel(Parcel parcel) {
            return new KeyNode(parcel);
        }

        @Override
        public KeyNode[] newArray(int size) {
            return new KeyNode[size];
        }
    };
    private String uc_id;
    private String amount;
    private String code;
    private String valid;

    public KeyNode() {
    }

    public KeyNode(String uc_id, String amount, String code, String valid) {
        this.uc_id = uc_id;
        this.amount = amount;
        this.code = code;
        this.valid = valid;
    }

    private KeyNode(Parcel in) {
        this.uc_id = in.readString();
        this.amount = in.readString();
        this.code = in.readString();
        this.valid = in.readString();
    }

    public String getUc_id() {
        return uc_id;
    }

    public void setUc_id(String uc_id) {
        this.uc_id = uc_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Creator<KeyNode> getCREATOR() {
        return CREATOR;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uc_id);
        dest.writeString(amount);
        dest.writeString(code);
        dest.writeString(valid);
    }


}
