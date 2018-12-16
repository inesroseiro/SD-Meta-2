package dropsrc.src;

import java.io.Serializable;
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private boolean rights;  //0->regular 1->editor
    private boolean isNotifiedRights;
    private boolean isNotifiedAlbum;
    private boolean isNotifiedArtist;

    User(){
    };

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public boolean isRights() {
        return rights;
    }

    public void setRights(boolean rights) {
        this.rights = rights;
    }

    public boolean isNotifiedRights() {
        return isNotifiedRights;
    }

    public void setNotifiedRights(boolean notifiedRights) {
        this.isNotifiedRights = notifiedRights;
    }

    public boolean isNotifiedAlbum() {
        return isNotifiedAlbum;
    }

    public void setNotifiedAlbum(boolean notifiedAlbum) {
        this.isNotifiedAlbum = notifiedAlbum;
    }

    public boolean isNotifiedArtist() {
        return isNotifiedArtist;
    }

    public void setNotifiedArtist(boolean notifiedArtist) {
        this.isNotifiedArtist = notifiedArtist;
    }

    @Override
    public String toString() {
        return "\nUsername:" + username + "\nPassword:" + password + "\nRights:" + rights;
    }
}