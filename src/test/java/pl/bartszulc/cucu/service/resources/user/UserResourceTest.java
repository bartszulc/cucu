package pl.bartszulc.cucu.service.resources.user;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.testing.ResourceTest;
import com.yammer.dropwizard.validation.InvalidEntityException;
import org.junit.Test;
import pl.bartszulc.cucu.api.user.LoginUserRequest;
import pl.bartszulc.cucu.api.user.RegisterUserRequest;
import pl.bartszulc.cucu.api.user.UserResponse;
import pl.bartszulc.cucu.service.auth.CucuAuthenticator;
import pl.bartszulc.cucu.service.core.user.User;
import pl.bartszulc.cucu.service.domain.user.UserService;
import pl.bartszulc.cucu.service.helpers.translator.UserResponseTranslator;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * User: bart
 * Date: 11/6/13
 * Time: 8:06 PM
 */
public class UserResourceTest extends ResourceTest {
    private final User user = new User();
    private final UserService userService = mock(UserService.class);

    public UserResourceTest() {
        user.setEmail("user@user.pl");
        user.setUsername("user");
        user.setPassword("user");
    }

    @Override
    protected void setUpResources() {
        addResource(new UserResource(userService));
        addProvider(new BasicAuthProvider<>(new CucuAuthenticator(userService), "test-basic-auth"));
    }

    protected RegisterUserRequest buildRegisterFromUser(User user) {
        final RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail(user.getEmail());
        registerUserRequest.setPassword(user.getPassword());
        registerUserRequest.setUsername(user.getUsername());
        return registerUserRequest;
    }

    protected LoginUserRequest buildLoginFromUser(User user) {
        final LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUsername(user.getUsername());
        loginUserRequest.setPassword(user.getPassword());
        return loginUserRequest;
    }

    protected RegisterUserRequest buildRegisterEmpty() {
        final RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail("");
        registerUserRequest.setPassword("");
        registerUserRequest.setUsername("");
        return registerUserRequest;
    }

    protected UserResponse authenticate() {
        final LoginUserRequest loginUserRequest = buildLoginFromUser(user);
        final UserResponse userResponse = UserResponseTranslator.from(user);
        when(userService.login(loginUserRequest)).thenReturn(userResponse);
        client().addFilter(new HTTPBasicAuthFilter(user.getUsername(), user.getPassword()));
        return userResponse;
    }

    @Test
    public void validUserShouldRegister() {
        // Given
        final RegisterUserRequest registerUserRequest = buildRegisterFromUser(user);
        final UserResponse registerUserResponse = UserResponseTranslator.from(user);
        when(userService.register(registerUserRequest)).thenReturn(registerUserResponse);
        // When
        final UserResponse response = client().resource("/user/").type(MediaType.APPLICATION_JSON).post(UserResponse.class, registerUserRequest);
        // Then
        verify(userService).register(registerUserRequest);
        assertThat(response).isEqualTo(registerUserResponse);
    }

    @Test(expected = InvalidEntityException.class)
    public void invalidUserShouldNotRegister() {
        // Given
        final RegisterUserRequest registerUserRequest = buildRegisterEmpty();
        // When
        client().resource("/user/").type(MediaType.APPLICATION_JSON).post(UserResponse.class, registerUserRequest);
    }

    @Test
    public void usersShouldBeListed() {
        // Given
        final UserResponse registerUserResponse = UserResponseTranslator.from(user);
        final List<UserResponse> registerUserResponses = new ArrayList<>();
        registerUserResponses.add(registerUserResponse);
        when(userService.list()).thenReturn(registerUserResponses);
        authenticate();
        // When
        final List<UserResponse> response = client().resource("/user/").type(MediaType.APPLICATION_JSON).get(new GenericType<List<UserResponse>>() {
        });
        // Then
        assertThat(response).containsAll(registerUserResponses);
    }

    @Test
    public void noUsersShouldBeListed() {
        // Given
        final List<UserResponse> registerUserResponses = new ArrayList<>();
        when(userService.list()).thenReturn(registerUserResponses);
        authenticate();
        // When
        final List<UserResponse> response = client().resource("/user/").type(MediaType.APPLICATION_JSON).get(new GenericType<List<UserResponse>>() {
        });
        // Then
        assertThat(response).containsAll(registerUserResponses);
    }

    @Test(expected = UniformInterfaceException.class)
    public void userNotListedToUnauthenticated() {
        // Given
        final UserResponse registerUserResponse = UserResponseTranslator.from(user);
        final List<UserResponse> registerUserResponses = new ArrayList<>();
        registerUserResponses.add(registerUserResponse);
        when(userService.list()).thenReturn(registerUserResponses);
        // When
        client().resource("/user/").type(MediaType.APPLICATION_JSON).get(new GenericType<List<UserResponse>>() {
        });
    }

    @Test
    public void registeredUserShouldLogin() {
        // Given
        final UserResponse userResponse = authenticate();
        // When
        final UserResponse response = client().resource("/user/login/").type(MediaType.APPLICATION_JSON).get(UserResponse.class);
        // Then
        assertThat(userResponse).isEqualTo(response);
    }

    @Test(expected = UniformInterfaceException.class)
    public void unregisteredUserShouldNotLogin() {
        // When
        client().addFilter(new HTTPBasicAuthFilter("unknown", "unknown"));
        client().resource("/user/login/").type(MediaType.APPLICATION_JSON).get(UserResponse.class);
    }

    @Test(expected = UniformInterfaceException.class)
    public void noCredentialsShouldNotLogin() {
        // When
        client().resource("/user/login/").type(MediaType.APPLICATION_JSON).get(UserResponse.class);
    }

    @Test
    public void showDetailsOfLoggedUser() {
        // Given
        final UserResponse userResponse = authenticate();
        when(userService.find(1)).thenReturn(userResponse);
        // When
        final UserResponse response = client().resource("/user/1/").type(MediaType.APPLICATION_JSON).get(UserResponse.class);
        // Then
        assertThat(userResponse).isEqualTo(response);
    }

    @Test
    public void showDetailsOfRegisteredUser() {
        // Given
        final UserResponse loginUserResponse = authenticate();
        loginUserResponse.setUsername("another");
        when(userService.find(2)).thenReturn(loginUserResponse);
        // When
        final UserResponse response = client().resource("/user/2/").type(MediaType.APPLICATION_JSON).get(UserResponse.class);
        // Then
        assertThat(loginUserResponse).isEqualTo(response);
    }

    @Test(expected = UniformInterfaceException.class)
    public void notShowDetailsToUnauthenticated() {
        // Given
        final UserResponse userResponse = UserResponseTranslator.from(user);
        when(userService.find(1)).thenReturn(userResponse);
        // When
        client().resource("/user/1/").type(MediaType.APPLICATION_JSON).get(UserResponse.class);
    }

    @Test
    public void userShouldUpdateHimself() {
        // Given
        final RegisterUserRequest registerUserRequest = buildRegisterFromUser(user);
        registerUserRequest.setUsername("new");
        final UserResponse userResponse = authenticate();
        userResponse.setUsername("new");
        userResponse.setId(1L);
        when(userService.update(userResponse, userResponse.getId(), registerUserRequest)).thenReturn(userResponse);
        // When
        final UserResponse response = client().resource("/user/1").type(MediaType.APPLICATION_JSON).post(UserResponse.class, registerUserRequest);
        // Then
        assertThat(response).isEqualTo(userResponse);
    }

    @Test(expected = UniformInterfaceException.class)
    public void userShouldNotUpdateSomeoneElse() {
        // Given
        final RegisterUserRequest registerUserRequest = buildRegisterFromUser(user);
        registerUserRequest.setUsername("new");
        final UserResponse userResponse = authenticate();
        userResponse.setUsername("new");
        userResponse.setId(2L);
        when(userService.update(userResponse, userResponse.getId(), registerUserRequest)).thenReturn(userResponse);
        // When
        client().resource("/user/1").type(MediaType.APPLICATION_JSON).post(UserResponse.class, registerUserRequest);
    }

    @Test(expected = UniformInterfaceException.class)
    public void unauthorizedUserShouldNotUpdate() {
        // Given
        final RegisterUserRequest registerUserRequest = buildRegisterFromUser(user);
        registerUserRequest.setUsername("new");
        final UserResponse userResponse = UserResponseTranslator.from(user);
        userResponse.setUsername("new");
        userResponse.setId(2L);
        when(userService.update(userResponse, userResponse.getId(), registerUserRequest)).thenReturn(userResponse);
        // When
        client().resource("/user/1").type(MediaType.APPLICATION_JSON).post(UserResponse.class, registerUserRequest);
    }

    @Test
    public void userShouldDeleteHimself() {
        // Given
        final UserResponse userResponse = authenticate();
        // When
        client().resource("/user/1").type(MediaType.APPLICATION_JSON).delete();
        // Then
        verify(userService).delete(userResponse, 1L);
    }

    @Test(expected = UniformInterfaceException.class)
    public void userShouldNotDeleteSomeoneElse() {
        // Given
        final UserResponse userResponse = authenticate();
        doThrow(UniformInterfaceException.class).when(userService).delete(userResponse, 1);
        // When
        client().resource("/user/1").type(MediaType.APPLICATION_JSON).delete();
    }

    @Test(expected = UniformInterfaceException.class)
    public void unauthorizedUserShouldNotDelete() {
        // Given
        doThrow(UniformInterfaceException.class).when(userService).delete(any(UserResponse.class), anyLong());
        // When
        client().resource("/user/1").type(MediaType.APPLICATION_JSON).delete();
    }
}
