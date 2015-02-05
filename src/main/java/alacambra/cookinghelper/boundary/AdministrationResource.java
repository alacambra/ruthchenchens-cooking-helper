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
@Path("/admin")
@Stateless
public class AdministrationResource {

    @PersistenceContext
    EntityManager em;

    @GET
    public Response getDbBackUp(){

        em.createNativeQuery("BACKUP2 TO backup.zip").executeUpdate();
        return null;
    }

}
