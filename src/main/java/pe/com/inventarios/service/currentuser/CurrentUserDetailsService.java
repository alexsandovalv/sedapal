package pe.com.inventarios.service.currentuser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.com.inventarios.model.CurrentUser;
import pe.com.inventarios.model.User;
import pe.com.inventarios.service.UserService;

/**
 * Created by sandovlu on 22/11/2015.
 */
@Service
public class CurrentUserDetailsService  implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserDetailsService.class);
    private final UserService userService;

    @Autowired
    public CurrentUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CurrentUser loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Authenticating user with email={}", email.replaceFirst("@.*", "@***"));
        User user = userService.getUserByEmail(email);
        if(user == null)
            throw  new UsernameNotFoundException(String.format("User with email=%s was not found", email));
        return new CurrentUser(user);
    }

}
