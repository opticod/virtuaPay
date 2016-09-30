package work.technie.virtuapay.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import work.technie.virtuapay.data.Contract.Keys;

/**
 * @brief Contains database util functions for app.
 * <p>
 * Created by Anupam (opticod) on 1/10/16.
 */

class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "keys.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE__TABLE = "CREATE TABLE " + Keys.TABLE_NAME + " (" +
                Keys._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Keys.U_ID + "  TEXT," +
                Keys.AMOUNT + "  TEXT," +
                Keys.CODE + "  TEXT," +
                Keys.VALID + "  TEXT)";

        sqLiteDatabase.execSQL(SQL_CREATE__TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Keys.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}