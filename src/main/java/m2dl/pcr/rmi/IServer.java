package m2dl.pcr.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface IServer extends Remote {

    List<String> getMessages() throws RemoteException;

    void sendMessage(String message) throws RemoteException;

    void subscribe(UUID id) throws RemoteException;

}
