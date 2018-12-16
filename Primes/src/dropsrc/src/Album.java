package dropsrc.src;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class Album implements Serializable {

    private static final long serialVersionUID = 1L;
    private String albumName;
    private String description;
    private String releaseDate;
    private String musicalGenre;
    CopyOnWriteArrayList<Integer> mediaRating;
    CopyOnWriteArrayList<Critic> critics;
    CopyOnWriteArrayList<Song> songs;
    CopyOnWriteArrayList<String> userChanges;

    Album(){};

    public Album(String albumName, String description, String releaseDate, String musicalGenre) {
        this.albumName = albumName;
        this.description = description;
        this.releaseDate = releaseDate;
        this.musicalGenre = musicalGenre;

        this.songs = new CopyOnWriteArrayList<Song>();
        this.critics = new CopyOnWriteArrayList<Critic>();
        this.mediaRating = new CopyOnWriteArrayList<Integer>();
    }

    public void setSongs(CopyOnWriteArrayList<Song> songs) {
        this.songs = songs;
    }

    public void addSongs( Song song) {
        this.songs.add(song);
    }

    public void removeSongs( Song song) {
        this.songs.remove(song);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public CopyOnWriteArrayList<Integer> getMediaRating() {
        return mediaRating;
    }

    public void setMediaRating(CopyOnWriteArrayList<Integer> mediaRating) {
        this.mediaRating = mediaRating;
    }

    public CopyOnWriteArrayList<Critic> getCritics() {
        return critics;
    }

    public void setCritics(CopyOnWriteArrayList<Critic> critics) {
        this.critics = critics;
    }

    public CopyOnWriteArrayList<Song> getSongs() {
        return songs;
    }

    public CopyOnWriteArrayList<String> getUserChanges() {
        return userChanges;
    }

    public void setUserChanges(CopyOnWriteArrayList<String> userChanges) {
        this.userChanges = userChanges;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMusicalGenre() {
        return musicalGenre;
    }

    public void setMusicalGenre(String musicalGenre) {
        this.musicalGenre = musicalGenre;
    }

    public void addAvgRate(Integer rate) {
        this.mediaRating.add(rate);
        System.out.println(mediaRating);
    }

    public void addCritics( Critic critic) {
        this.critics.add(critic);
    }
    public int seeCriticsUsers(String username){
        for (Critic c : critics){
            if(c.getUsername().equals(username)){
                return 0;
            }
        }
        return 1;
    }

    @Override
    public String toString() {
        return "\nAlbum:"+albumName+"\nDescArtist:"+description+"\nReleaseDate:"+releaseDate+"\nMusicalGenre:"+musicalGenre+"\nMedRatings:"+mediaRating+"\nCritics:"+critics+"\nSongs:"+songs;
    }
}