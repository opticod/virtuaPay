package work.technie.virtuapay;

import android.content.Context;

import work.technie.virtuapay.utils.Note;
import java.util.ArrayList;

/**
 * Created by scopeinfinity on 1/10/16.
 */

public class CashManager {
    public CashManager() {

    }

    public static CashManager getInstance(Context context) {
        return new CashManager();
    }

    ArrayList<Note> getNotes(int amount) {
        if(amount%10 !=0)
            return null;
        ArrayList<Note> list = new ArrayList<>();
        int counter = 0;
        for (int i=0;counter<amount/10 && true;i++) {
            Note note = new Note(1,"code1",10,true);
            if(note.isValid()) {
                list.add(note);
                counter++;
            }
        }

        return list;
    }
}
