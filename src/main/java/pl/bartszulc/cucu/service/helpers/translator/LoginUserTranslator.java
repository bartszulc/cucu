package pl.bartszulc.cucu.service.helpers.translator;

import pl.bartszulc.cucu.api.user.LoginUserRequest;
import pl.bartszulc.cucu.service.core.user.User;

/**
 * User: bart
 * Date: 11/6/13
 * Time: 6:44 PM
 */
public class LoginUserTranslator {
    public static User to(LoginUserRequest loginUserRequest) {
        final User loginUser = new User();
        loginUser.setPassword(loginUserRequest.getPassword());
        loginUser.setUsername(loginUserRequest.getUsername());
        return loginUser;
    }
}
