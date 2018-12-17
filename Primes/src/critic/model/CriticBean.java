package critic.model;


import model.UserBean;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class CriticBean extends UserBean {

    private String artistname;
    private String albumname;
    private String critic;
    private String username;

    private ArrayList<String> criticas;

    public ArrayList<String> getCriticas() {
        return criticas;
    }

    public void setCriticas(ArrayList<String> criticas) {
        this.criticas = criticas;
    }

    private int rate;

    public CriticBean(){
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }


    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getCritic() {
        return critic;
    }

    public void setCritic(String critic) {
        this.critic = critic;
    }

    public boolean getInsertCritic() {

        Boolean result = false;
        try {

            result = server.checkCritic(username,artistname,albumname,rate,critic);
            System.out.println(result);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public String getViewCritics() {

        String result = null;
        try {
            result = server.checkViewAlbumCritics("user",artistname,albumname);

        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }



}
