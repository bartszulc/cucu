package pl.bartszulc.cucu.service.domain.user;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import pl.bartszulc.cucu.api.user.LoginUserRequest;
import pl.bartszulc.cucu.api.user.RegisterUserRequest;
import pl.bartszulc.cucu.api.user.UserResponse;
import pl.bartszulc.cucu.service.core.user.User;
import pl.bartszulc.cucu.service.helpers.translator.RegisterUserTranslator;
import pl.bartszulc.cucu.service.helpers.translator.UserResponseTranslator;
import pl.bartszulc.cucu.service.jdbi.user.UserDAO;

import javax.annotation.Nullable;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: bart
 * Date: 11/6/13
 * Time: 5:21 PM
 */
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserResponse register(RegisterUserRequest registerUserRequest) {
        final User registeredUser = userDAO.create(RegisterUserTranslator.to(registerUserRequest));
        return UserResponseTranslator.from(registeredUser);
    }

    public List<UserResponse> list() {
        final List<User> registeredUsers = userDAO.list();
        return Lists.transform(registeredUsers, new Function<User, UserResponse>() {
            @Nullable
            @Override
            public UserResponse apply(@Nullable User user) {
                return UserResponseTranslator.from(user);
            }
        });
    }

    public UserResponse login(LoginUserRequest loginUserRequest) {
        final User loginUser = userDAO.find(loginUserRequest.getUsername(), loginUserRequest.getPassword());
        return UserResponseTranslator.from(loginUser);
    }

    public UserResponse find(long id) {
        final User foundUser = userDAO.find(id);
        return UserResponseTranslator.from(foundUser);
    }

    public UserResponse update(UserResponse userResponse, long id, RegisterUserRequest details) {
        assertThat(userResponse.getId()).isEqualTo(id);
        final User user = userDAO.find(id);
        user.setUsername(details.getUsername());
        user.setEmail(details.getEmail());
        final User updated = userDAO.update(user);
        return UserResponseTranslator.from(updated);
    }

    public void delete(UserResponse userResponse, long id) {
        assertThat(userResponse.getId()).isEqualTo(id);
        final User user = userDAO.find(id);
        userDAO.delete(user);
    }
}
