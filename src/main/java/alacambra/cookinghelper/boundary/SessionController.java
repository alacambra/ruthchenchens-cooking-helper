package alacambra.cookinghelper.boundary;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * Created by alacambra on 1/20/15.
 */

@Named
@SessionScoped
public class SessionController implements Serializable{
    @Inject
    HttpSession session;

    public void closeSession(){
        session.invalidate();
    }


}
