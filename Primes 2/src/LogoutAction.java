import com.opensymphony.xwork2.ActionSupport;
import login.model.LoginBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;


public class LogoutAction extends ActionSupport implements SessionAware {
    private String username;
    private String password;

    private Map<String, Object> session;

    public String execute(){
        this.getLogOutBean().setUsername(this.username);
        if(this.getLogOutBean().logout()){
            System.out.println("Logout");
            session.remove("username");
            return SUCCESS;
        }
        else
            return ERROR;


    }

    public void setLogOutBean(LogoutBean logout) {
        this.session.put("logOutBean", logout);
    }

    public LogoutBean getLogOutBean() {
        if(!session.containsKey("logOutBean"))
            this.setLogOutBean(new LogoutBean());
        return (LogoutBean) session.get("logOutBean");
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}