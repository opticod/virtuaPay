package work.technie.virtuapay.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import work.technie.virtuapay.utils.Note;

import static android.R.attr.codes;

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

    public void truncate() {
        db.beginTransaction();
        db.execSQL("delete from "+Contract.Keys.TABLE_NAME);
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.i("LOG******", "truncate: "+getCount());
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
        Log.i("LOG******", "bulkInsert: "+getCount());
        return returnCount;
    }

    public void remove(ArrayList<String> codes) {
        db.beginTransaction();
        for (String code : codes)
            db.delete(Contract.Keys.TABLE_NAME,Contract.Keys.CODE+"=?",new String[]{code});
        db.setTransactionSuccessful();
        db.endTransaction();
        Log.i("LOG******", "remove: "+getCount());
    }

    public int countMoney() {
        db.beginTransaction();
        Cursor c = db.rawQuery("select sum("+Contract.Keys.AMOUNT+") from "+Contract.Keys.TABLE_NAME+";", null);
        int amount = 0;
        if(c.moveToFirst())
            amount = c.getInt(0);
        db.endTransaction();
        Log.i("LOG******", "countMoney: "+amount);
        return amount;
    }

    public ArrayList<Note> getNotes(int currencyNoteNeeded) {
        db.beginTransaction();
        Cursor cursor =  db.rawQuery("SELECT code FROM "+Contract.Keys.TABLE_NAME,null);
        ArrayList<Note> list = new ArrayList<>();
        try {
            int i=0;
            if(cursor.moveToFirst())
            while (cursor!=null && i<currencyNoteNeeded  ) {
                String code = cursor.getString(0);//cursor.getColumnIndex(Contract.Keys.CODE));
                Note note = new Note(0,code,10,true);
                Log.i("***********", "getNotes: i > "+code);
                list.add(note);
                i++;
                if(!cursor.moveToNext())break;
            }
            if(i!=currencyNoteNeeded) {
                Log.e("***********", "Notes not generateed all");
                return null;

            }
        }catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        finally {
            db.endTransaction();

        }
        return list;

    }
}
