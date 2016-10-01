package work.technie.virtuapay.utils;

import java.util.ArrayList;

/**
 * Created by scopeinfinity on 30/9/16.
 */

public interface ManagerPaymentInterface{

    /**
     * Accepts all notes
     * @param sender
     * @param notes
     * @return isAccepted
     */
    boolean acceptNotes(String sender, ArrayList<Note> notes);

    /**
     * For Updating Frontend
     * @param name
     * @param uid
     */
    void clientConnected(String name,int uid);

    /**
     * Push message in UI
     */
    void msgWaitingForClient();
}
