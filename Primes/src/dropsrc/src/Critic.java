package dropsrc.src;

import java.io.Serializable;


public class Critic implements Serializable{

    private static final long serialVersionUID = 1L;
    private int rating;
    private String username;
    private String descCritic;

    Critic(){
    };

    public Critic(int rating, String username, String descCritic) {
        this.rating = rating;
        this.username = username;
        this.descCritic = descCritic;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescCritic() {
        return descCritic;
    }

    public void setDescCritic(String descCritic) {
        this.descCritic = descCritic;
    }

    @Override
    public String toString() {
        return "\nRating:" + rating + "\nUsername:" + username + "\nDescCritic:" + descCritic;
    }
}
