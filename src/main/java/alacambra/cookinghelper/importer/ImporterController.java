package alacambra.cookinghelper.importer;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by alacambra on 28/12/14.
 */
@Named("importerController")
@SessionScoped
public class ImporterController implements Serializable{
    @EJB
    Importer importer;

    public void beginImport(){
        importer.loadLines();
    }
}
