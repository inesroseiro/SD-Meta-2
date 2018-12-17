package dropsrc.src;

import java.rmi.*;
public interface ClientInterface extends Remote {

    String getUsername() throws RemoteException;
    void setUsername(String username) throws RemoteException;
    void notifyRights() throws RemoteException;



}
