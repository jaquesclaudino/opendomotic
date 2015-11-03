package com.opendomotic.mb;

import com.opendomotic.model.entity.Environment;
import com.opendomotic.service.dao.EnvironmentDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jaques
 */
@Named
@SessionScoped
public class HomeMB implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(HomeMB.class.getName());
    
    @Inject
    private EnvironmentDAO environmentDAO;
    
    private Integer idEnvironment;
    
    @PostConstruct
    public void init() {
        Environment environment = environmentDAO.findFirst();
        if (environment != null) {
            idEnvironment = environment.getId();
        }
        LOG.log(Level.INFO, "HomeMB init id={0}", idEnvironment);
    }
    
    public void checkParamName() {
        Map<String, String> params = getExternalContext().getRequestParameterMap();
        String paramName = params.get("name");
        if (paramName != null) {
            Environment environment = environmentDAO.findByName(paramName);
            if (environment != null) {
                idEnvironment = environment.getId();
            }
        }
    }
    
    public void valueChangeMethod(ValueChangeEvent e) throws IOException {
        LOG.info(e.getNewValue().toString());
        idEnvironment = (Integer) e.getNewValue();
        getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/user/index.jsf");
    }
    
    public Integer getIdEnvironment() {
        return idEnvironment;
    }

    public void setIdEnvironment(Integer idEnvironment) {
        this.idEnvironment = idEnvironment;
    }

    public Boolean getAdminLogged() {        
        return getExternalContext().isUserInRole("admin");
    }
 
    private ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }
    
}
