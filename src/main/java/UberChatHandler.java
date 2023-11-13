import javax.swing.*;
import java.io.*;

public class UberChatHandler{
    private String userType, opUserType, opIp;
    private BufferedReader input;
    private PrintWriter output;
    private int userID;
    private boolean conToServer;
    public UberChatHandler(JFrame backFrame, String userType, int userID, int opUserID) {
        UberChatClientGUI uberChC = new UberChatClientGUI(backFrame, userType, userID, opUserID);
        conToServer = uberChC.getConnectedStatus();

        if (conToServer) {
            uberChC.setVisible(true);
        } else {
            uberChC.dispose();
            new UberChatServerGUI(backFrame);
        }
    }


    public static void main(String[] args) {
        new UberChatHandler(null, "Cliente", 1, 2);
    }

}
