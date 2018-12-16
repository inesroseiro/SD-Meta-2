package signup.model;


import model.UserBean;

import java.rmi.RemoteException;

public class SignUpBean extends UserBean {

    private String username;
    private String password;


    public SignUpBean(){
        super();
    }

    public boolean getRegister() {

        boolean result = false;
        try {
            result = server.checkRegister(username,password);//authenticastes voter
            System.out.println(result);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
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
