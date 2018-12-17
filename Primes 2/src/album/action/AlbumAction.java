package album.action;


import album.model.AlbumBean;
import com.opensymphony.xwork2.ActionSupport;
import dropsrc.src.Album;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;


public class AlbumAction extends ActionSupport implements SessionAware {

    private String name;
    private String description ;
    private String date;
    private String genre;
    private String artist;
    private Map<String, Object> session;

    private String oldname;
    private String olddescription;
    private String olddate;
    private String oldgenre;
    private ArrayList<String> listaMusicas;
    private ArrayList<String> artistas;
    private ArrayList<String> albuns;


    public Map<String, Object> getSession() {
        return session;
    }

    public String execute(){
        // any username is accepted without confirmation (should check using RMI)
        String usernameEleitor = (String) getSession().get("username");
        //System.out.println("sou o username eleitor " + usernameEleitor);
        this.getInsertAlbumBean().setName(this.name);
        this.getInsertAlbumBean().setDescription(this.description);
        this.getInsertAlbumBean().setArtist(this.artist);
        this.getInsertAlbumBean().setDate(this.date);
        this.getInsertAlbumBean().setGenre(this.genre);

        if(this.getInsertAlbumBean().getInsertAlbum()){
            return SUCCESS;
        }

        else
            return ERROR;

    }

    public String execute2(){

        this.getEditAlbumBean().setName(name);
        this.getEditAlbumBean().setGenre(genre);
        this.getEditAlbumBean().setDate(date);
        this.getEditAlbumBean().setArtist(artist);
        this.getEditAlbumBean().setDescription(description);
        this.getEditAlbumBean().setOlddescription(olddescription);
        this.getEditAlbumBean().setOldname(oldname);


        if(this.getEditAlbumBean().getEditAlbum()){
            return SUCCESS;
        }

        else
            return ERROR;

    }

    public String executeRemove(){

        this.getRemoveAlbumBean().setName(name);
        this.getRemoveAlbumBean().setArtist(artist);

        if(this.getRemoveAlbumBean().getRemoveAlbum()){
            return SUCCESS;
        }
        else
            return ERROR;

    }

    public String executeViewAlbumDetails(){


        this.getViewAlbumDetailsBean().setName(this.name);
        this.getViewAlbumDetailsBean().setArtist(this.artist);

        String message = this.getViewAlbumDetailsBean().getViewAlbumDetails();

        String[] splitStringAll = message.split(";");
        String[] splitStringName = splitStringAll[1].split("\\|");
        String artist = splitStringName[1];
        System.out.println("Artist name: " + artist);
        getSession().put("artist", artist);

        String[] splitString2 = splitStringAll[3].split("\\|");
        String description = splitString2[1];
        System.out.println("Description: " + description);
        getSession().put("description", description);

        String[] splitString3 = splitStringAll[4].split("\\|");
        String genre = splitString3[1];
        System.out.println("Musical Genre: " + genre);
        getSession().put("genre", genre);

        String[] splitString4 = splitStringAll[5].split("\\|");
        String date = splitString4[1];
        System.out.println("Release Date: " + date);
        getSession().put("date",date);

        int j = 6, size = splitStringAll.length;
        listaMusicas = new ArrayList<String>();


        if(size > 2) {
            System.out.println("Songs:");
            while (j < size) {
                String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                String music = splitStringAlbumName[1];
                System.out.println("Music Name: " + music);
                listaMusicas.add(music);
                j++;
            }
        }
        getSession().put("listaMusicas", listaMusicas);

        System.out.println(listaMusicas);
        return SUCCESS;

    }

    public String executeSearchByAlbum(){
        this.getSearchbyAlbumBean().setName(this.name);
        String message = this.getSearchbyAlbumBean().searchByAlbum();

        if (message.equals("type|search_album_name;error in search_album_name")) {
            return ERROR;
        }

        else{
            artistas = new ArrayList<String>();
            albuns = new ArrayList<String>();

            System.out.println("\t-Results-");
            //Aqui correu tudo bem
            String[] splitStringAll = message.split(";");
            //get item count
            String[] splitString1 = splitStringAll[1].split("\\|");
            int itemCount = Integer.parseInt(splitString1[1]);
            //get albums
            int j = 2, size = splitStringAll.length;
            while (j < size) {
                String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                String album = splitStringAlbumName[1];
                System.out.println("Album Name: " + album);
                albuns.add(album);
                String[] splitStringArtistName = splitStringAll[j + 1].split("\\|");
                String artist = splitStringArtistName[1];
                System.out.println("Artist Name: " + artist);
                artistas.add(artist);
                j += 2;
            }
            getSession().put("albuns", albuns);
            getSession().put("artistas", artistas);



        }

        return SUCCESS;

    }







    public AlbumBean getInsertAlbumBean() {
        if(!session.containsKey("insertAlbumBean"))
            this.setInsertAlbumBean(new AlbumBean());
        return (AlbumBean) session.get("insertAlbumBean");
    }

    public void setInsertAlbumBean(AlbumBean insertAlbumBean) {
        this.session.put("insertAlbumBean", insertAlbumBean);
    }


    public AlbumBean getEditAlbumBean() {
        if(!session.containsKey("editAlbumBean"))
            this.setEditAlbumBean(new AlbumBean());
        return (AlbumBean) session.get("editAlbumBean");
    }

    public void setEditAlbumBean(AlbumBean editAlbumBean) {
        this.session.put("editAlbumBean", editAlbumBean);
    }


    public AlbumBean getRemoveAlbumBean() {
        if(!session.containsKey("removeAlbumBean"))
            this.setRemoveAlbumBean(new AlbumBean());
        return (AlbumBean) session.get("removeAlbumBean");
    }

    public void setRemoveAlbumBean(AlbumBean removeAlbumBean) {
        this.session.put("removeAlbumBean", removeAlbumBean);
    }


    public AlbumBean getViewAlbumDetailsBean() {
        if(!session.containsKey("viewDetailsAlbumBean"))
            this.viewDetailsAlbumBean(new AlbumBean());
        return (AlbumBean) session.get("viewDetailsAlbumBean");
    }

    public void viewDetailsAlbumBean(AlbumBean viewDetailsAlbumBean) {
        this.session.put("viewDetailsAlbumBean", viewDetailsAlbumBean);
    }

    public AlbumBean getSearchbyAlbumBean() {
        if(!session.containsKey("viewSearchByAlbum"))
            this.setSearchbyAlbumBean(new AlbumBean());
        return (AlbumBean) session.get("viewSearchByAlbum");
    }

    public void setSearchbyAlbumBean(AlbumBean viewSearchByAlbum) {
        this.session.put("viewSearchByAlbum", viewSearchByAlbum);
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

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }


}