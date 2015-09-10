/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.mb.crud;

import com.opendomotic.model.entity.AbstractEntityFile;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author jaques
 * @param <T>
 */
public abstract class AbstractFileUpload<T extends AbstractEntityFile> extends AbstractSelectableCRUD<T> {
    
    private static final Logger LOG = Logger.getLogger(AbstractFileUpload.class.getName());
    
    public void handleFileUpload(FileUploadEvent event) throws IOException {  
        LOG.info(event.getFile().toString());
  
        FacesMessage msg = new FacesMessage("Success", event.getFile().getFileName());  
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        //TO-DO: Usar configuracao de path
        String path = FacesContext.getCurrentInstance().getExternalContext()  
                .getRealPath("/resources/images/" + event.getFile().getFileName());  
        
        byte[] conteudo = event.getFile().getContents();  
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(conteudo);
        }
        
        if (entity.getId() != null) {
            entity.setFileName(event.getFile().getFileName());
            getDAO().save(entity);
        }
    }
    
}
