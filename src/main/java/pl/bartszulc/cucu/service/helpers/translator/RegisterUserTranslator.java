package pl.bartszulc.cucu.service.helpers.translator;

import pl.bartszulc.cucu.api.user.RegisterUserRequest;
import pl.bartszulc.cucu.service.core.user.User;

/**
 * User: bart
 * Date: 11/6/13
 * Time: 6:14 PM
 */
public class RegisterUserTranslator {
    public static User to(RegisterUserRequest registerUserRequest) {
        final User registerUser = new User();
        registerUser.setEmail(registerUserRequest.getEmail());
        registerUser.setPassword(registerUserRequest.getPassword());
        registerUser.setUsername(registerUserRequest.getUsername());
        return registerUser;
    }
}
