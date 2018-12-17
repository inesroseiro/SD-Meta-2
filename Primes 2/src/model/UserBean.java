package model;

import dropsrc.src.RMI;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class UserBean implements Serializable {
    protected RMI server;

    public UserBean() {

        try {
            server = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");

            System.out.println("hiiiiiiii");
        }
        catch(NotBoundException | RemoteException | MalformedURLException e) {
            System.out.println(e.getMessage()); // what happens *after* we reach this line?
        }
    }

    private void lookupRMI() throws RemoteException, NotBoundException, MalformedURLException {
        server = (RMI) Naming.lookup("rmi://localhost:7000/DropMusic");
    }
}
