package artist.model;


import model.UserBean;

import java.rmi.RemoteException;
import java.util.List;

public class ArtistBean extends UserBean {

    private String name;
    private String description;

    private String oldname;
    private String olddescription;

    List<String> listaInformacoes;
    List<String> listaAlbuns;

    public ArtistBean(){
        super();
    }

    public boolean getInsertArtist() {
        //System.out.println("im hereeeeeeeeee");

        Boolean result = false;
        try {
            result = server.checkArtist(name,description);
            System.out.println(result);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public boolean getEditArtist() {

        Boolean result = false;
        try {
            result = server.checkEditArtist(oldname,name,description);
            System.out.println(result);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public boolean getRemoveArtist() {

        Boolean result = false;
        try {
            result = server.checkRemoveArtist(name);
            System.out.println(result);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public String getViewArtistDetails() {

        String result = null;
        try {
            result = server.checkViewArtistDetails("user",name);

        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public String searchByArtist() {

        String result = null;
        try {
            result = server.checkFromArtistName("user",name);

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
}
