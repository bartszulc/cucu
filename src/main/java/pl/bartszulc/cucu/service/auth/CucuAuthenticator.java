package pl.bartszulc.cucu.service.auth;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import pl.bartszulc.cucu.api.user.LoginUserRequest;
import pl.bartszulc.cucu.api.user.UserResponse;
import pl.bartszulc.cucu.service.domain.user.UserService;

/**
 * Created with IntelliJ IDEA.
 * User: bart
 * Date: 11/6/13
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CucuAuthenticator implements Authenticator<BasicCredentials, UserResponse> {
    private final UserService userService;

    public CucuAuthenticator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<UserResponse> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        final LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername(basicCredentials.getUsername());
        loginUserRequest.setPassword(basicCredentials.getPassword());
        final UserResponse userResponse = userService.login(loginUserRequest);
        if (userResponse != null) {
            return Optional.of(userResponse);
        }
        return Optional.absent();
    }
}
