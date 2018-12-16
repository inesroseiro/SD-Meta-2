/**
 * Raul Barbosa 2014-11-07
 */
package primes.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.apache.struts2.interceptor.SessionAware;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import primes.model.PrimesBean;

public class PrimesAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;
	private String username;
	private String password;

	@Override
	public String execute() throws Exception {
		// you could execute some business logic here, but for now
		// the result is "success" and struts.xml knows what to do

		if(username.equals("admin")&& username.equals("admin")){
			return SUCCESS;

		}
		else
			return LOGIN;
	}

	public PrimesBean getPrimesBean() throws RemoteException, NotBoundException, MalformedURLException {
		if(!session.containsKey("primesBean"))
			this.setPrimesBean(new PrimesBean());
		return (PrimesBean) session.get("primesBean");
	}

	public void setPrimesBean(PrimesBean primesBean) {
		this.session.put("primesBean", primesBean);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
