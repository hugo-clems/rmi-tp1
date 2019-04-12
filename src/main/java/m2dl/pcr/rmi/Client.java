package m2dl.pcr.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Client {

    private static Message stub;

    private Client() {}

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            stub = (Message) registry.lookup("Message");

            Ihm dialog = new Ihm();
            dialog.pack();
            dialog.setVisible(true);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static void sendMessage(String msg) {
        try {
            stub.sendMessage(msg);
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static List<String> getMessages() {
        try {
            return stub.getMessages();
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }

}
