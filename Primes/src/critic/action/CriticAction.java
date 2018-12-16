package critic.action;


import com.opensymphony.xwork2.ActionSupport;
import critic.model.CriticBean;
import dropsrc.src.Critic;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;


public class CriticAction extends ActionSupport implements SessionAware {

    private Map<String, Object> session;

    private String artistname;
    private String albumname;
    private String critic;
    private String username;
    private int rate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getCritic() {
        return critic;
    }

    public void setCritic(String critic) {
        this.critic = critic;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String execute(){

        String username = (String) getSession().get("username");
        System.out.println("sou o username .... " + username);

        this.getInsertCriticBean().setAlbumname(albumname);
        this.getInsertCriticBean().setArtistname(artistname);
        this.getInsertCriticBean().setRate(rate);
        this.getInsertCriticBean().setCritic(critic);
        this.getInsertCriticBean().setUsername(username);

        if(this.getInsertCriticBean().getInsertCritic()){
            return SUCCESS;
        }
        else
            return ERROR;

    }

    public String executeViewCritics(){

        this.getViewCriticsBean().setUsername(this.username);
        this.getViewCriticsBean().setAlbumname(this.albumname);
        this.getViewCriticsBean().setArtistname(this.artistname);


        String message = this.getViewCriticsBean().getViewCritics();

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

    public CriticBean getInsertCriticBean() {
        if(!session.containsKey("insertCriticBean"))
            this.setInsertCriticBean(new CriticBean());
        return (CriticBean) session.get("insertCriticBean");
    }

    public void setInsertCriticBean(CriticBean insertCriticBean) {
        this.session.put("insertCriticBean", insertCriticBean);
    }

    public CriticBean getViewCriticsBean() {
        if(!session.containsKey("ViewCriticsBean"))
            this.setViewCriticsBean(new CriticBean());
        return (CriticBean) session.get("ViewCriticsBean");
    }

    public void setViewCriticsBean(CriticBean ViewCriticsBean) {
        this.session.put("ViewCriticsBean", ViewCriticsBean);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

}