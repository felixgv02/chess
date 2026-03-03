package passoff.server;
import server.dataaccess.*;
import service.*;
import org.junit.jupiter.api.*;

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
        UserService.RegisterResult res = userService.register(new UserService.RegisterRequest("test", "pass", "email"));
        Assertions.assertNotNull(res.authToken());
    }
    @Test
    public void registerFailTaken() throws DataAccessException {
        userService.register(new UserService.RegisterRequest("test", "pass", "email"));
        Assertions.assertThrows(DataAccessException.class, () ->
                userService.register(new UserService.RegisterRequest("test", "pass2", "email2")));
    }

    @Test
    public void loginSuccess() throws DataAccessException {
        userService.register(new UserService.RegisterRequest("test", "pass", "email"));
        UserService.LoginResult res = userService.login(new UserService.LoginRequest("test", "pass"));
        Assertions.assertNotNull(res.authToken());
    }
    @Test
    public void loginFailWrongPassword() throws DataAccessException {
        userService.register(new UserService.RegisterRequest("test", "pass", "email"));
        Assertions.assertThrows(DataAccessException.class, () ->
                userService.login(new UserService.LoginRequest("test", "wrong")));
    }
    @Test
    public void logoutSuccess() throws DataAccessException {
        UserService.RegisterResult reg = userService.register(new UserService.RegisterRequest("test", "pass", "email"));
        Assertions.assertDoesNotThrow(() -> userService.logout(reg.authToken()));
    }
    @Test
    public void logoutFailInvalidToken() throws DataAccessException {
        userService.register(new UserService.RegisterRequest("test", "pass", "email"));
        Assertions.assertThrows(DataAccessException.class, () -> userService.logout("invalid-token"));
    }
}
