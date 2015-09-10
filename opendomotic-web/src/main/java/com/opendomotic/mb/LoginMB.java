package com.opendomotic.mb;

import java.io.Serializable;
import java.security.Principal;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jaques
 */
@Named
@SessionScoped
public class LoginMB implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(LoginMB.class.getName());
    private static final String COOKIE_AUTH_NAME = "opendomotic-auth";
        
    private String user;
    private String password;
    
    @PostConstruct
    public void init() {
        System.out.println("***** init");
        try {  
            Map<String, Object> cookies = getExternalContext().getRequestCookieMap();
            Cookie cookieAuth = (Cookie) cookies.get(COOKIE_AUTH_NAME);
            System.out.println("cookie="+cookieAuth);
            if (cookieAuth != null) {
                String values[] = cookieAuth.getValue().split("\\|");
                if (values.length == 2) {
                    HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();   
                    request.login(values[0], values[1]);
                }
            }
        } catch (ServletException ex) {
            LOG.warning(ex.toString());
        }
    }
    
    public String login() {
        System.out.println("***** login");
        try {
            HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
            request.login(user, password);
            
            HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();
            Cookie cookie = new Cookie(COOKIE_AUTH_NAME, String.format("%s|%s", user, password));
            cookie.setPath(request.getContextPath());
            cookie.setMaxAge(60*60*24*30); //30 dias. Expire time. -1 = by end of current session, 0 = immediately expire it, otherwise just the lifetime in seconds.
            response.addCookie(cookie);
            
            return "index";
        } catch (ServletException ex) {
            LOG.warning(ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login inválido", null));
            return "login";
        }        
    }

    public String logout() {
        HttpSession session = (HttpSession) getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "login";
    }
    
    public String getUserLogged() {
        Principal principal = getExternalContext().getUserPrincipal();
        if (principal == null)
            return null;        
        return principal.getName();
    }
    
    public boolean isAdminRole() {
        return getExternalContext().isUserInRole("admin");
    }
    
    public boolean isAuthenticated() {
        return getExternalContext().getUserPrincipal() != null;
    }
    
    private ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
