package login.model;


import dropsrc.src.RMIClient;
import model.UserBean;

import java.rmi.RemoteException;

public class LoginBean extends UserBean {

    private String username;
    private String password;

    public LoginBean(){
        super();
    }

    public String getAuthentication() {

        String result = "";
        try {
            result = server.checkLogin(username,password);//authenticastes voter
            System.out.println(result);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public boolean changeUserRights() {

        boolean result = true;
        try {
            result = server.checkUserRights(username);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(result);

        System.out.println("sou o result" + result);

        return result;
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
