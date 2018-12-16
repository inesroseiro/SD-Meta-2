package user.action;

import java.util.Map;

import user.model.UserBean;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;


public class UserAction extends ActionSupport implements SessionAware{
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;

    public String execute() {
        Boolean permission;
        permission = (Boolean) session.get("loggedin");
        if(permission != null && permission) {
            return SUCCESS;
        }
        else {
            return LOGIN;
        }
    }


    public UserBean getUserBean(){
        if(!session.containsKey("userBean"))
            this.setUserBean(new UserBean());
        return (UserBean) session.get("userBean");
    }

    public void setUserBean(UserBean userBean){
        this.session.put("userBean", userBean);
    }
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }


}
