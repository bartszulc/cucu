package pl.bartszulc.cucu.service.auth;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import pl.bartszulc.cucu.api.user.LoginUserRequest;
import pl.bartszulc.cucu.api.user.LoginUserResponse;
import pl.bartszulc.cucu.service.domain.user.UsersService;

/**
 * Created with IntelliJ IDEA.
 * User: bart
 * Date: 11/6/13
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CucuAuthenticator implements Authenticator<BasicCredentials, LoginUserResponse> {
    private final UsersService usersService;

    public CucuAuthenticator(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public Optional<LoginUserResponse> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        final LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername(basicCredentials.getUsername());
        loginUserRequest.setPassword(basicCredentials.getPassword());
        final LoginUserResponse loginUserResponse = usersService.login(loginUserRequest);
        if (loginUserResponse != null) {
            return Optional.of(loginUserResponse);
        }
        return Optional.absent();
    }
}
