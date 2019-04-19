package m2dl.pcr.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server implements IServer {

    private List<String> messages = new ArrayList<String>();

    private List<IClient> clients = new ArrayList<IClient>();

    public Server() {}

    public static void main(String args[]) {
        try {
            Server obj = new Server();
            IServer stubMsg = (IServer) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("IServer", stubMsg);

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
        for (IClient client : clients) {
            client.notifier();
        }
    }

    public void subscribe(UUID id) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry(null);
        try {
            IClient stub = (IClient) registry.lookup(id.toString());
            clients.add(stub);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

}
