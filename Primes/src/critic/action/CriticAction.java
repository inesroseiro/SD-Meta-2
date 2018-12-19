package critic.action;


import com.opensymphony.xwork2.ActionSupport;
import critic.model.CriticBean;
import dropsrc.src.Critic;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;


public class CriticAction extends ActionSupport implements SessionAware {

    private Map<String, Object> session;

    private String artistname;
    private String albumname;
    private String critic;
    private String username;
    private int rate;
    private ArrayList<String> criticas;
    private ArrayList<String> users;

    public ArrayList<String> getCriticas() {
        return criticas;
    }

    public void setCriticas(ArrayList<String> criticas) {
        this.criticas = criticas;
    }

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

        CriticBean ab = this.getInsertCriticBean();


        ab.setAlbumname(albumname);
        ab.setArtistname(artistname);
        ab.setRate(rate);
        ab.setCritic(critic);
        ab.setUsername(username);

        if(ab.getInsertCritic()){
            return SUCCESS;
        }
        else
            return ERROR;

    }

    public String executeViewCritics(){
        CriticBean ab = this.getInsertCriticBean();

        ab.setUsername(this.username);
        ab.setAlbumname(this.albumname);
        ab.setArtistname(this.artistname);


        String message = ab.getViewCritics();
        if (message.equals("type|view_album_critics;error in view_album_critics")) {
            return ERROR;
        }

        String[] splitStringAll = message.split(";");

        System.out.println("\t-Album critics-");

        String[] splitStringName = splitStringAll[1].split("\\|");
        System.out.println("Artist name: " + splitStringName[1]);
        getSession().put("artistname",splitStringName[1]);

        String [] splitString3 =splitStringAll[3].split("\\|");
        System.out.println("Average Rate: " + splitString3[1]);
        getSession().put("avgrate",splitString3[1]);

        criticas = new ArrayList<String>();
        users = new ArrayList<String>();




        if(splitStringAll.length > 3) {
            int j = 4, size = splitStringAll.length;
            while(j < size) {
                String[] splitStringArtistName = splitStringAll[j + 1].split("\\|");
                String[] splitStringAlbumName = splitStringAll[j].split("\\|");
                String critic = splitStringArtistName[1];
                System.out.println("Critic: "+critic);
                String user = splitStringAlbumName[1];
                System.out.println("Username: " + user);
                criticas.add(critic);
                users.add(user);
                j += 2;

            }
        }
        getSession().put("listacriticas",criticas);
        getSession().put("listausers",users);



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