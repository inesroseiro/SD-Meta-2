import dropsrc.src.RMIClient;
import model.UserBean;

import java.rmi.RemoteException;

public class LogoutBean extends UserBean {

    private String username;
    private String password;

    public LogoutBean(){
        super();
    }

    public boolean logout(){

        try {
            server.removeLoggedUsers(username);
            return true;

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
