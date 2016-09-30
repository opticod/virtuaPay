package work.technie.virtuapay.bluetooth;

import java.io.Serializable;
import java.util.ArrayList;

import work.technie.virtuapay.utils.Note;

/**
 * Created by scopeinfinity on 30/9/16.
 */

public class Message implements Serializable {
    static final long serialVersionUID = 664624519783291626L;
    public static final int MESSAGE_LIST = 0;
    public static final int MESSAGE_REPLY_SUCCESS = 1;
    public static final int MESSAGE_REPLY_FAIL = 1;

    /**
     * For Client to Manager, list of notes
     * For Manager to Client, list of invalid notes
     */
    private ArrayList<Note> list;
    private String sender;

    //For Manager to Client
    private int messageBit;

    Message(String sender, ArrayList<Note> list) {
        this.sender = sender;
        this.list = list;
    }

    Message(String sender, int type, ArrayList<Note> list) {
        this.sender = sender;
        if(list == null)
            this.list = new ArrayList<>();
        else
            this.list = list;
    }

    public ArrayList<Note> getList() {
        return list;
    }

    public String getSender() {
        return sender;
    }
}
