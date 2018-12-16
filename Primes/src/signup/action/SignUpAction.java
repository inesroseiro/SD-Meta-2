package signup.action;


import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import signup.model.SignUpBean;

import java.util.Map;


public class SignUpAction extends ActionSupport implements SessionAware {

    private String username=null;
    private String password=null ;
    private Map<String, Object> session;

    public String execute(){

        // any username is accepted without confirmation (should check using RMI)

        if(this.username != null && !username.equals("")) {
            this.getSignUpBean().setUsername(this.username);
            this.getSignUpBean().setPassword(this.password);
            System.out.println(this.username);
            System.out.println(this.password);
            if(this.getSignUpBean().getRegister()){
                return SUCCESS;
            }
            return ERROR;
        }
        else
            return LOGIN;
    }

    public SignUpBean getSignUpBean() {
        if(!session.containsKey("signUpBean"))
            this.setSignUpBean(new SignUpBean());
        return (SignUpBean) session.get("signUpBean");
    }

    public void setSignUpBean(SignUpBean signUpBean) {
        this.session.put("signUpBean", signUpBean);
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