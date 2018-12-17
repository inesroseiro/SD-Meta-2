import com.opensymphony.xwork2.ActionSupport;
import login.model.LoginBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;


public class LogoutAction extends ActionSupport implements SessionAware {


    private Map<String, Object> session;

    public String execute(){

        session.remove("username");
        System.out.println("Logout");
        return "LOGOUT";
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}