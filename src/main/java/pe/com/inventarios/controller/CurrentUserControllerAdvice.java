package pe.com.inventarios.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pe.com.inventarios.model.CurrentUser;

/**
 * Created by sandovlu on 22/11/2015.
 */
@ControllerAdvice
public class CurrentUserControllerAdvice {


    @ModelAttribute("currentUser")
    public CurrentUser getCurrentUser(Authentication authentication) {
        return (authentication == null) ? null : (CurrentUser) authentication.getPrincipal();
    }

}
