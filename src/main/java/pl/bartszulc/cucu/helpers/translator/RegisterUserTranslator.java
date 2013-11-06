package pl.bartszulc.cucu.helpers.translator;

import pl.bartszulc.cucu.api.user.RegisterUserRequest;
import pl.bartszulc.cucu.api.user.RegisterUserResponse;
import pl.bartszulc.cucu.service.core.user.User;

/**
 * Created with IntelliJ IDEA.
 * User: bart
 * Date: 11/6/13
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisterUserTranslator {
    public static User to(RegisterUserRequest registerUserRequest) {
        final User registerUser = new User();
        registerUser.setEmail(registerUserRequest.getEmail());
        registerUser.setPassword(registerUserRequest.getPassword());
        registerUser.setUsername(registerUserRequest.getUsername());
        return registerUser;
    }

    public static RegisterUserResponse from(User registeredUser) {
        final RegisterUserResponse registerUserResponse = new RegisterUserResponse();
        registerUserResponse.setEmail(registeredUser.getEmail());
        registerUserResponse.setUsername(registeredUser.getUsername());
        registerUserResponse.setId(registeredUser.getId());
        return registerUserResponse;
    }
}
