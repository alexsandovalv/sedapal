package pe.com.inventarios.model;

import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Created by sandovlu on 22/11/2015.
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User{

    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public Role getRole() {
        return user.getRole();
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                "} " + super.toString();
    }
}
