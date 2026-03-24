package client;

import static org.junit.jupiter.api.Assertions.*;
import client.facade.ResponseException;
import client.facade.ServerFacade;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;




public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @BeforeEach
    void clear() throws Exception {
        facade.clearDatabase();
    }
    @Test
    void registerSuccess() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertNotNull(authData.authToken());
        assertEquals("player1", authData.username());
    }

    @Test
    void registerFailDuplicate() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        assertThrows(ResponseException.class, () -> facade.register("player1", "pass", "email"));
    }
    @Test
    void loginSuccess() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        var authObj = facade.login("player1", "password");
        assertNotNull(authObj.authToken());
    }
    @Test
    void loginFailBadPassword() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        assertThrows(ResponseException.class, () -> facade.login("player1", "wrong"));
    }
    @Test
    void logoutSuccess() throws Exception {
        var auth = facade.register("player1", "password", "p1@email.com");
        assertDoesNotThrow(() -> facade.logout(auth.authToken()));
    }
    @Test
    void logoutFailBadToken() {
        assertThrows(ResponseException.class, () -> facade.logout("bogusToken"));
    }
    @Test
    void createGameSuccess() throws Exception {
        var auth = facade.register("player1", "password", "p1@email.com");
        var res = facade.createGame(auth.authToken(), "myGame");
        assertTrue(res.gameID() > 0);
    }


}
