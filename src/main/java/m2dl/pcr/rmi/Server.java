package m2dl.pcr.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server implements Message {

    private List<String> messages = new ArrayList<String>();

    public Server() {}

    public static void main(String args[]) {
        try {
            Server obj = new Server();
            Message stubMsg = (Message) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Message", stubMsg);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public List<String> getMessages() throws RemoteException {
        return messages;
    }

    public void sendMessage(String message) throws RemoteException {
        this.messages.add(message);
    }

}
