package alacambra.cookinghelper;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by alacambra on 1/12/15.
 */
@Named
@SessionScoped
public class Navigation implements Serializable{

    public String goToSearch(){
        return "searchresults";
    }
}
