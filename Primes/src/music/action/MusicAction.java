package music.action;

import com.opensymphony.xwork2.ActionSupport;
import music.model.MusicBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;


public class MusicAction extends ActionSupport implements SessionAware {

    private String name;
    private String genre;
    private String duration ;
    private String date;
    private String lyrics;
    private String album;
    private String artist;
    private Map<String, Object> session;

    private String oldalbum;
    private String oldartist;
    private String oldname;
    private String oldgenre;
    private String oldduration ;
    private String olddate;
    private String oldlyrics;


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



    public String execute(){
        // any username is accepted without confirmation (should check using RMI)
        this.getInsertMusicBean().setName(this.name);
        this.getInsertMusicBean().setGenre(this.genre);
        this.getInsertMusicBean().setDuration(this.duration);
        this.getInsertMusicBean().setDate(this.date);
        this.getInsertMusicBean().setLyrics(this.lyrics);
        this.getInsertMusicBean().setArtist(this.artist);
        this.getInsertMusicBean().setAlbum(this.album);
        this.getInsertMusicBean().setDate(this.date);

        this.getInsertMusicBean().setGenre(this.genre);

        if(this.getInsertMusicBean().getInsertMusic()){
            return SUCCESS;
        }
        else
            return ERROR;
    }

    public String execute2(){
        // any username is accepted without confirmation (should check using RMI)
        this.getEditMusicBean().setName(this.name);
        this.getEditMusicBean().setGenre(this.genre);
        this.getEditMusicBean().setDuration(this.duration);
        this.getEditMusicBean().setDate(this.date);
        this.getEditMusicBean().setLyrics(this.lyrics);
        this.getEditMusicBean().setArtist(this.artist);
        this.getEditMusicBean().setAlbum(this.album);
        this.getEditMusicBean().setDate(this.date);

        this.getEditMusicBean().setGenre(this.genre);
        this.getEditMusicBean().setOldname(this.oldname);
        this.getEditMusicBean().setOldalbum(this.oldalbum);
        this.getEditMusicBean().setOldartist(this.oldartist);

        if(this.getEditMusicBean().getEditMusic()){
            return SUCCESS;
        }
        else
            return ERROR;
    }
    public String executeRemove(){
        // any username is accepted without confirmation (should check using RMI)
        this.getRemoveMusicBean().setName(this.name);
        this.getRemoveMusicBean().setArtist(this.artist);
        this.getRemoveMusicBean().setAlbum(this.album);


        if(this.getRemoveMusicBean().getRemoveMusic()){
            return SUCCESS;
        }
        else
            return ERROR;
    }


    public String executeViewMusicDetails(){

        this.getViewMusicDetailsBean().setArtist(this.artist);
        this.getViewMusicDetailsBean().setAlbum(this.album);
        this.getViewMusicDetailsBean().setName(this.name);



        String message = this.getViewMusicDetailsBean().getViewMusicDetails();

        String[] splitString = message.split(";");

        System.out.println("\t-Song Details-");

        //aqui
        String [] splitString3 =splitString[3].split("\\|");
        String name = splitString3[1];
        System.out.println("Music Name: " + name);
        getSession().put("name", name);

        String [] splitString4 =splitString[4].split("\\|");
        String genre = splitString4[1];
        System.out.println("Music Genre: " +genre);
        getSession().put("genre", genre);

        String [] splitString5 =splitString[5].split("\\|");
        String duration = splitString5[1];
        System.out.println("Duration: " + duration);
        getSession().put("duration", duration);


        String [] splitString6 =splitString[6].split("\\|");
        String date = splitString6[1];
        System.out.println("Release Date: " + date);
        getSession().put("date", date);


        String [] splitString7 =splitString[7].split("\\|");
        String lyrics = splitString7[1];
        System.out.println("Lyrics: " + lyrics);
        getSession().put("lyrics", lyrics);

        return SUCCESS;

    }



    public MusicBean getInsertMusicBean() {
        if(!session.containsKey("insertMusicBean"))
            this.setInsertMusicBean(new MusicBean());
        return (MusicBean) session.get("insertMusicBean");
    }
    public void setInsertMusicBean(MusicBean insertMusicBean) {
        this.session.put("insertMusicBean", insertMusicBean);
    }


    public void setEditMusicBean(MusicBean editMusicBean) {
        this.session.put("editMusicBean", editMusicBean);
    }

    public MusicBean getEditMusicBean() {
        if(!session.containsKey("editMusicBean"))
            this.setEditMusicBean(new MusicBean());
        return (MusicBean) session.get("editMusicBean");
    }

    public void setRemoveMusicBean(MusicBean removeMusicBean) {
        this.session.put("removeMusicBean", removeMusicBean);
    }


    public MusicBean getRemoveMusicBean() {
        if(!session.containsKey("removeMusicBean"))
            this.setRemoveMusicBean(new MusicBean());
        return (MusicBean) session.get("removeMusicBean");
    }


    public MusicBean getViewMusicDetailsBean() {
        if(!session.containsKey("ViewMusicDetailsBean"))
            this.setViewMusicDetailsBean(new MusicBean());
        return (MusicBean) session.get("ViewMusicDetailsBean");
    }

    public void setViewMusicDetailsBean(MusicBean ViewMusicDetailsBean) {
        this.session.put("ViewMusicDetailsBean", ViewMusicDetailsBean);
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

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }


}