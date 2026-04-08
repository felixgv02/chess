package service;

import server.dataaccess.*;
import org.junit.jupiter.api.*;
import service.request.LoginRequest;
import service.request.RegisterRequest;
import service.result.LoginResult;
import service.result.RegisterResult;

public class UserServiceTest {
    private UserService userService;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void registerSuccess() throws DataAccessException {
        RegisterResult res = userService.register(new RegisterRequest("test", "pass", "email"));
        Assertions.assertNotNull(res.authToken());
    }

    @Test
    public void registerFailTaken() throws DataAccessException {
        userService.register(new RegisterRequest("test", "pass", "email"));
        Assertions.assertThrows(DataAccessException.class,
                () -> userService.register(new RegisterRequest("test", "pass2", "email2")));
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        userService.register(new RegisterRequest("test", "pass", "email"));
        LoginResult res = userService.login(new LoginRequest("test", "pass"));
        Assertions.assertNotNull(res.authToken());
    }

    @Test
    public void loginFailWrongPassword() throws DataAccessException {
        userService.register(new RegisterRequest("test", "pass", "email"));
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(new LoginRequest("test", "wrong")));
    }

    @Test
    public void logoutSuccess() throws DataAccessException {
        RegisterResult reg = userService.register(new RegisterRequest("test", "pass", "email"));
        Assertions.assertDoesNotThrow(() -> userService.logout(reg.authToken()));
    }

    @Test
    public void logoutFailInvalidToken() throws DataAccessException {
        userService.register(new RegisterRequest("test", "pass", "email"));
        Assertions.assertThrows(DataAccessException.class, () -> userService.logout("invalid-token"));
    }
}
