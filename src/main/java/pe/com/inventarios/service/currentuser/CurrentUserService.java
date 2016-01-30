package pe.com.inventarios.service.currentuser;

import pe.com.inventarios.model.CurrentUser;

/**
 * Created by sandovlu on 22/11/2015.
 */
public interface CurrentUserService {

    boolean canAccessUser(CurrentUser currentUser, Long userId);

}
