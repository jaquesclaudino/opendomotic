package com.opendomotic.service.rest;

import com.opendomotic.model.entity.DeviceConfig;
import com.opendomotic.model.entity.DeviceImage;
import com.opendomotic.model.entity.DevicePosition;
import com.opendomotic.model.entity.Environment;
import com.opendomotic.model.rest.DevicePositionRest;
import com.opendomotic.model.rest.EnvironmentRest;
import com.opendomotic.service.dao.EnvironmentDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jaques
 */
@Path("/environment")
public class EnvironmentRestService {
    
    private static final Logger LOG = Logger.getLogger(EnvironmentRestService.class.getName());
    private static final String IMAGE_PATH = "../resources/images/"; //TO-DO: Usar configuracao de path
    
    @Inject
    private EnvironmentDAO environmentDAO;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/get")
    public EnvironmentRest getEnvironmentRest(@QueryParam("id") int id) {
        Environment environment = environmentDAO.findById(id);
        if (environment != null) {
            EnvironmentRest e = new EnvironmentRest();            
            List<DevicePositionRest> list = new ArrayList<>();
            
            if (environment.getListDevicePosition() != null) {
                for (DevicePosition position : environment.getListDevicePosition()) {  
                    DeviceConfig config = position.getDeviceConfig();
                    if (config.isEnabled()) {
                        list.add(new DevicePositionRest(
                            position.getId(), 
                            position.getX(),
                            position.getY(),
                            position.getDeviceConfig().getName(), 
                            position.getDeviceConfig().getDeviceType().ordinal(),
                            position.getDeviceConfig().getCustomScript(),
                            getDeviceImageFileName(position.getDeviceConfig().getDeviceImageDefault()),
                            getDeviceImageFileName(position.getDeviceConfig().getDeviceImageSwitch())));
                    }
                }
            }
            
            e.setFileName(IMAGE_PATH + environment.getFileName()); 
            e.setListDevicePositionRest(list);
            return e;
        } else {
            LOG.log(Level.WARNING, "Environment not found: id={0}", id);
        }
        return null;
    }
    
    private String getDeviceImageFileName(DeviceImage deviceImage) {
        if (deviceImage == null)
            return null;
        return IMAGE_PATH + deviceImage.getFileName();
    }
    
}
