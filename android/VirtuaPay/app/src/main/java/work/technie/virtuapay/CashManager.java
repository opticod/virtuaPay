package work.technie.virtuapay;

import android.content.Context;
import android.util.Log;

import work.technie.virtuapay.data.Db;
import work.technie.virtuapay.utils.Note;
import java.util.ArrayList;

/**
 * Created by scopeinfinity on 1/10/16.
 */

public class CashManager {
    private Context context;
    private String lastError = null;
    public CashManager(Context context) {
        this.context = context;
    }

    public String getLastError() {
        return lastError;
    }

    public ArrayList<Note> getNotes(int amount) {
        Db db = new Db(context);
        db.open();
        if(amount%10 !=0)
            return null;
        ArrayList<Note> list = new ArrayList<>();
        //Assuming 10 currency notes
        int currencyNoteNeeded = amount/10;

        if(db.getCount()<currencyNoteNeeded) {
            lastError = "Insufficient, Balance is "+db.getCount()*10+" Requested "+currencyNoteNeeded*10;
            return null;
        }

        list = db.getNotes(currencyNoteNeeded);

//        int counter = 0;
//        for (int i=0;counter<amount/10 && true;i++) {
//            Note note = new Note(1,"code1",10,true);
//            if(note.isValid()) {
//                list.add(note);
//                counter++;
//            }
//        }
        db.close();
        return list;
    }

    /**
     * Remove element Note from database who are present in list
     * @param list
     */
    public void removeCash(ArrayList<Note> list) {
        Db db = new Db(context);
        db.open();
        ArrayList<String> codes = new ArrayList<>();
        for (Note note : list)
            codes.add(note.getCode());
        db.remove(codes);
        db.close();

    }
}
