package work.technie.virtuapay.data;

import android.provider.BaseColumns;

/**
 * @brief Contains database contracts for app.
 * <p>
 * Created by Anupam (opticod) on 1/10/16.
 */

public class Contract {

    public static final class Keys implements BaseColumns {

        public static final String TABLE_NAME = "keys";

        public static final String U_ID = "u_id";
        public static final String AMOUNT = "amount";
        public static final String CODE = "code";
        public static final String VALID = "valid";

    }
}
