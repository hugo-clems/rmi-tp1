package m2dl.pcr.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote {

    void notifier() throws RemoteException;

}
