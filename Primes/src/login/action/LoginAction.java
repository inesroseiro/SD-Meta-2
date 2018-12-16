package login.action;


import com.opensymphony.xwork2.ActionSupport;
import login.model.LoginBean;
import org.apache.struts2.interceptor.SessionAware;

import java.io.IOException;
import java.util.Map;


public class LoginAction extends ActionSupport implements SessionAware {

    private String username;
    private String password ;
    private Map<String, Object> session;

    public Map<String, Object> getSession() {
        return session;
    }

    public String execute(){

        if(this.username != null && !username.equals("")) {
            this.getLoginBean().setUsername(this.username);
            this.getLoginBean().setPassword(this.password);

            if(this.getLoginBean().getAuthentication().equals("editor")){
                getSession().put("username", getUsername());

                return SUCCESS;
            }
            if(this.getLoginBean().getAuthentication().equals("user")){
                return LOGIN;
            }

            return ERROR;
        }
        else
            return ERROR;
    }

    public String execute2(){
        this.getChangeUserRightsBean().setUsername(this.username);
        if(this.getChangeUserRightsBean().changeUserRights()){
                return SUCCESS;
            }
            return ERROR;
    }

    public LoginBean getLoginBean() {
        if(!session.containsKey("loginBean"))
            this.setLoginBean(new LoginBean());
        return (LoginBean) session.get("loginBean");
    }

    public void setLoginBean(LoginBean loginBean) {
        this.session.put("loginBean", loginBean);
    }

    public LoginBean getChangeUserRightsBean() {
        if(!session.containsKey("changeUserRights"))
            this.setChangeUserRightsBean(new LoginBean());
        return (LoginBean) session.get("changeUserRights");
    }

    public void setChangeUserRightsBean(LoginBean changeUserRights) {
        this.session.put("changeUserRights", changeUserRights);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }


}