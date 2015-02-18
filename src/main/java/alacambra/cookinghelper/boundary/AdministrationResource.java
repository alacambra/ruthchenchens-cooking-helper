package alacambra.cookinghelper.boundary;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by alacambra on 2/6/15.
 */
@Path("/backup")
@Stateless
public class AdministrationResource {

    @PersistenceContext
    EntityManager em;

    @GET
    public Integer getDbBackUp(){

        int ret = em.createNativeQuery("BACKUP TO '/opt/jboss/wildfly/backup/backup.zip'").executeUpdate();
        return ret;
    }

}
