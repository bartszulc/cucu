package pl.bartszulc.cucu.service.domain.user;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.bartszulc.cucu.api.user.LoginUserRequest;
import pl.bartszulc.cucu.api.user.LoginUserResponse;
import pl.bartszulc.cucu.api.user.RegisterUserRequest;
import pl.bartszulc.cucu.api.user.RegisterUserResponse;
import pl.bartszulc.cucu.helper.translator.LoginUserTranslator;
import pl.bartszulc.cucu.helper.translator.RegisterUserTranslator;
import pl.bartszulc.cucu.service.core.user.User;
import pl.bartszulc.cucu.service.jdbi.user.UserDAO;

import javax.annotation.Nullable;
import java.util.List;

import static pl.bartszulc.cucu.helper.translator.RegisterUserTranslator.from;
import static pl.bartszulc.cucu.helper.translator.RegisterUserTranslator.to;

/**
 * Created with IntelliJ IDEA.
 * User: bart
 * Date: 11/6/13
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */
public final class UsersService {
    private final UserDAO userDAO;

    public UsersService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public final RegisterUserResponse register(RegisterUserRequest registerUserRequest) {
        final User registeredUser = userDAO.create(to(registerUserRequest));
        return from(registeredUser);
    }

    public final List<RegisterUserResponse> list() {
        final List<User> registeredUsers = userDAO.list();
        return Lists.transform(registeredUsers, new Function<User, RegisterUserResponse>() {
            @Nullable
            @Override
            public RegisterUserResponse apply(@Nullable User user) {
               return from(user);
            }
        });
    }

    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        final User loginUser = userDAO.find(loginUserRequest.getUsername(), loginUserRequest.getPassword());
        return LoginUserTranslator.from(loginUser);
    }
}
