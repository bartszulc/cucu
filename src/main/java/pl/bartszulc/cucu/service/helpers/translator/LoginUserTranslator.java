package pl.bartszulc.cucu.service.helpers.translator;

import pl.bartszulc.cucu.api.user.LoginUserRequest;
import pl.bartszulc.cucu.api.user.LoginUserResponse;
import pl.bartszulc.cucu.service.core.user.User;

/**
 * Created with IntelliJ IDEA.
 * User: bart
 * Date: 11/6/13
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginUserTranslator {
    public static User to(LoginUserRequest loginUserRequest) {
        final User loginUser = new User();
        loginUser.setPassword(loginUserRequest.getPassword());
        loginUser.setUsername(loginUserRequest.getUsername());
        return loginUser;
    }

    public static LoginUserResponse from(User loginUser) {
        final LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setEmail(loginUser.getEmail());
        loginUserResponse.setUsername(loginUser.getUsername());
        loginUserResponse.setId(loginUser.getId());
        return loginUserResponse;
    }
}
