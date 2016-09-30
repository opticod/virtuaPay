package work.technie.virtuapay.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

/**
 * @brief Contains database util functions for app.
 * <p>
 * Created by Anupam (opticod) on 1/10/16.
 */
public class Db {

    private static final String EQUAL = " == ";
    private final DBHelper dbHelper;
    private SQLiteDatabase db;

    public Db(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public boolean isOpen() {
        return db.isOpen();
    }

    public void close() {
        dbHelper.close();
    }

    public Cursor getInfosCursor() {

        return db.query(
                Contract.Keys.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public Cursor getInfoCursorById(int id) {

        String selection = Contract.Keys._ID + EQUAL + id;

        return db.query(
                Contract.Keys.TABLE_NAME,
                null,
                selection,
                null,
                null,
                null,
                null
        );
    }

    public long getCount() {

        return DatabaseUtils.queryNumEntries(db,
                Contract.Keys.TABLE_NAME);
    }

    public int bulkInsert(@NonNull ContentValues[] values) {

        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {

                long _id = db.insert(Contract.Keys.TABLE_NAME, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return returnCount;
    }
}
