package m2dl.pcr.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Message extends Remote {

    List<String> getMessages() throws RemoteException;

    void sendMessage(String message) throws RemoteException;

}
