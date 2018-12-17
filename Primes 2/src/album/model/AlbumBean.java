package album.model;


import model.UserBean;

import java.rmi.RemoteException;

public class AlbumBean extends UserBean {

    private String name;
    private String description;
    private String date;
    private String genre;
    private String artist;

    private String oldname;
    private String olddescription;
    private String olddate;
    private String oldgenre;


    public AlbumBean(){
        super();
    }

    public boolean getInsertAlbum() {
        Boolean result = false;
        try {
            result = server.checkAlbum(name,description,genre,date,artist);
            System.out.println(result);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public boolean getEditAlbum() {
        Boolean result = false;
        try {
            result = server.checkEditAlbum(artist,oldname,name,description,genre,date);
            System.out.println(result);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public boolean getRemoveAlbum() {
        Boolean result = false;
        try {
            result = server.checkRemoveAlbum(artist,name);
            System.out.println(result);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public String getViewAlbumDetails() {

        String result = null;
        try {
            result = server.checkViewAlbumDetails("user",artist,name);

        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public String searchByAlbum() {

        String result = null;
        try {
            result = server.checkFromAlbumName("user",name);

        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public String getOldname() {
        return oldname;
    }

    public void setOldname(String oldname) {
        this.oldname = oldname;
    }

    public String getOlddescription() {
        return olddescription;
    }

    public void setOlddescription(String olddescription) {
        this.olddescription = olddescription;
    }

    public String getOlddate() {
        return olddate;
    }

    public void setOlddate(String olddate) {
        this.olddate = olddate;
    }

    public String getOldgenre() {
        return oldgenre;
    }

    public void setOldgenre(String oldgenre) {
        this.oldgenre = oldgenre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
