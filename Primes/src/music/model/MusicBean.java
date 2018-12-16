package music.model;


import model.UserBean;

import java.rmi.RemoteException;

public class MusicBean extends UserBean {

    private String name;
    private String genre;
    private String duration ;
    private String date;
    private String lyrics;
    private String album;
    private String artist;

    private String oldalbum;
    private String oldartist;
    private String oldname;
    private String oldgenre;
    private String oldduration ;
    private String olddate;
    private String oldlyrics;



    public MusicBean(){
        super();
    }

    public boolean getInsertMusic() {

        Boolean result = false;
        try {
            result = server.checkMusic(name,genre,duration,date,lyrics,artist,album);
            System.out.println(result);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public boolean getEditMusic() {

        Boolean result = false;
        try {
            result = server.checkEditSong(artist,album,oldname,name,genre,duration,date,lyrics);
            System.out.println(result);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }


    public boolean getRemoveMusic() {

        Boolean result = false;
        try {
            result = server.checkRemoveMusic(name,artist,album);
            System.out.println(result);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public String getViewMusicDetails() {

        String result = null;
        try {
            result = server.checkViewSongDetails("user",artist,album,name);

        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public String getOldalbum() {
        return oldalbum;
    }

    public void setOldalbum(String oldalbum) {
        this.oldalbum = oldalbum;
    }

    public String getOldartist() {
        return oldartist;
    }

    public void setOldartist(String oldartist) {
        this.oldartist = oldartist;
    }

    public String getOldname() {
        return oldname;
    }

    public void setOldname(String oldname) {
        this.oldname = oldname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public String getOldgenre() {
        return oldgenre;
    }

    public void setOldgenre(String oldgenre) {
        this.oldgenre = oldgenre;
    }

    public String getOldduration() {
        return oldduration;
    }

    public void setOldduration(String oldduration) {
        this.oldduration = oldduration;
    }

    public String getOlddate() {
        return olddate;
    }

    public void setOlddate(String olddate) {
        this.olddate = olddate;
    }

    public String getOldlyrics() {
        return oldlyrics;
    }

    public void setOldlyrics(String oldlyrics) {
        this.oldlyrics = oldlyrics;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
