package dropsrc.src;

import java.io.Serializable;
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private boolean isNotified;

    Notification(){};

    public Notification(String username) {
        this.username = username;
        this.isNotified = isNotified;
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

    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "username='" + username + '\'' +
                ", isNotified=" + isNotified +
                '}';
    }
}
