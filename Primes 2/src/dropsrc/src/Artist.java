package dropsrc.src;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

public class Artist implements Serializable{

    private static final long serialVersionUID = 1L;
    private String artistName;
    private String descArtist;
    private CopyOnWriteArrayList<Album> albums;
    private CopyOnWriteArrayList<Notification> userChanges;

    Artist(){};

    public Artist(String artistName, String descArtist) {
        this.artistName = artistName;
        this.descArtist = descArtist;
        this.albums = new CopyOnWriteArrayList<>();
        this.userChanges = new CopyOnWriteArrayList<>();

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDescArtist() {
        return descArtist;
    }

    public void setDescArtist(String descArtist) {
        this.descArtist = descArtist;
    }

    public CopyOnWriteArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(CopyOnWriteArrayList<Album> albums) {
        this.albums = albums;
    }

    public void addAlbums( Album album) {
        this.albums.add(album);
    }
    public void removeAlbum( Album album) {
        this.albums.remove(album);
    }

    public CopyOnWriteArrayList<Notification> getUserChanges() {
        return userChanges;
    }

    public void setUserChanges(CopyOnWriteArrayList<Notification> userChanges) {
        this.userChanges = userChanges;
    }


    @Override
    public String toString() {
        return "\nArtist:" + artistName + "\nDescArtist:" + descArtist + "\nAlbums:" + albums;
    }

}
