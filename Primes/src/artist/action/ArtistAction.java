package artist.action;


import artist.model.ArtistBean;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ArtistAction extends ActionSupport implements SessionAware {

    private String oldname;
    private String name;
    private String olddescription;
    private String description;

    List<String> listaInformacoes;
    List<String> listaAlbuns;


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

    public Map<String, Object> getSession() {
        return session;
    }

    private Map<String, Object> session;


    public String execute(){
        // any username is accepted without confirmation (should check using RMI)

            this.getInsertArtistBean().setName(this.name);
            this.getInsertArtistBean().setDescription(this.description);

            if(this.getInsertArtistBean().getInsertArtist()){
                return SUCCESS;
            }


        else
            return ERROR;
    }

    public String execute2(){
        // any username is accepted without confirmation (should check using RMI)

        this.getEditArtistBean().setName(this.name);
        this.getEditArtistBean().setDescription(this.description);
        this.getEditArtistBean().setOldname(this.oldname);
        this.getEditArtistBean().setOlddescription(this.olddescription);

        if(this.getEditArtistBean().getEditArtist()){
            return SUCCESS;
        }

        else
            return ERROR;
    }

    public String executeRemove(){
        this.getRemoveArtistBean().setName(this.name);

        if(this.getRemoveArtistBean().getRemoveArtist()){
            return SUCCESS;
        }

        else
            return ERROR;

    }

    public String executeViewArtistDetails(){


        this.getViewArtistDetailsBean().setName(this.name);
        String message = this.getViewArtistDetailsBean().getViewArtistDetails();


        String[] splitStringAll = message.split(";");
        String[] splitStringName = splitStringAll[1].split("\\|");
        String name = splitStringName[1];
        System.out.println("Artist name: " + name);
        getSession().put("name", name);


        String[] splitStringDescription = splitStringAll[2].split("\\|");
        String description = splitStringDescription[1];
        System.out.println("Artist Biography: " + description);
        getSession().put("description", description);

        int j = 3, size = splitStringAll.length;
        listaInformacoes = new ArrayList<String>();


        if(size > 0) {
            while (j < size) {
                String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                String album = splitStringAlbumName[1];
                System.out.println("Album: " + album);
                listaInformacoes.add(album);
                j++;
            }

            getSession().put("listaInformacoes", listaInformacoes);

        }
        return SUCCESS;

    }

    public String executeSearchByArtist(){
        this.getSearchbyArtistBean().setName(this.name);
        String message = this.getSearchbyArtistBean().searchByArtist();

        if (message.equals("type|search_album_artist;error in search_album_artist")) {
            return ERROR;
        }

        else{
            listaAlbuns = new ArrayList<String>();

            String[] splitStringAll = message.split(";");

            System.out.println("\t-Results-");

            String[] splitStringName = splitStringAll[1].split("\\|");
            String artist = splitStringName[1];
            System.out.println("Artist name: " + artist);
            getSession().put("artist", artist);

            int j = 3, size = splitStringAll.length;
            while (j < size) {
                String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                String album = splitStringAlbumName[1];
                System.out.println("Album Name: " + album);
                listaAlbuns.add(album);
                j++;
            }
            getSession().put("listaAlbuns", listaAlbuns);

        }

        return SUCCESS;

    }



    public List<String> getListaInformacoes() {
        return listaInformacoes;
    }

    public void setListaInformacoes(List<String> listaInformacoes) {
        this.listaInformacoes = listaInformacoes;
    }

    public ArtistBean getInsertArtistBean() {
        if(!session.containsKey("insertArtistBean"))
            this.setInsertArtistBean(new ArtistBean());
        return (ArtistBean) session.get("insertArtistBean");
    }

    public void setInsertArtistBean(ArtistBean insertArtistBean) {
        this.session.put("insertArtistBean", insertArtistBean);
    }

    public ArtistBean getEditArtistBean() {
        if(!session.containsKey("editArtistBean"))
            this.setEditArtistBean(new ArtistBean());
        return (ArtistBean) session.get("editArtistBean");
    }

    public void setEditArtistBean(ArtistBean editArtistBean) {
        this.session.put("editArtistBean", editArtistBean);
    }

    public ArtistBean getRemoveArtistBean() {
        if(!session.containsKey("removeArtistBean"))
            this.setRemoveArtistBean(new ArtistBean());
        return (ArtistBean) session.get("removeArtistBean");
    }

    public void setRemoveArtistBean(ArtistBean removeArtistBean) {
        this.session.put("removeArtistBean", removeArtistBean);
    }

    public ArtistBean getViewArtistDetailsBean() {
        if(!session.containsKey("viewDetailsArtistBean"))
            this.setViewArtistDetailsBean(new ArtistBean());
        return (ArtistBean) session.get("viewDetailsArtistBean");
    }

    public void setViewArtistDetailsBean(ArtistBean viewDetailsArtistBean) {
        this.session.put("viewDetailsArtistBean", viewDetailsArtistBean);
    }

    public ArtistBean getSearchbyArtistBean() {
        if(!session.containsKey("viewSearchByArtist"))
            this.setSearchbyArtistBean(new ArtistBean());
        return (ArtistBean) session.get("viewSearchByArtist");
    }

    public void setSearchbyArtistBean(ArtistBean viewSearchByArtist) {
        this.session.put("viewSearchByArtist", viewSearchByArtist);
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

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }


}