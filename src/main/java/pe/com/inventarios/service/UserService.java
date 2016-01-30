package pe.com.inventarios.service;

import pe.com.inventarios.model.User;
import pe.com.inventarios.model.UserCreateForm;

import java.util.Collection;

/**
 * Created by sandovlu on 22/11/2015.
 */
public interface UserService {

    User getUserById(long id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    Collection<User> getAllUsers();

    User create(UserCreateForm form);
}
