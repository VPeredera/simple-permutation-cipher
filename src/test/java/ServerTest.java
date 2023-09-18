import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {

    private Client client;
    private static int port;

    @BeforeAll
    public static void start() throws InterruptedException, IOException {

        // Take an available port
        ServerSocket s = new ServerSocket(0);
        port = s.getLocalPort();
        s.close();

        Executors.newSingleThreadExecutor()
                .submit(() -> new Server().start(port));
        Thread.sleep(500);
    }

    @BeforeEach
    public void init() {
        client = new Client();
        client.start("127.0.0.1", port);
    }

    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() {
        String response = client.sendMessage("Hello");
        assertEquals("Hello, client!", response);
    }

    @AfterEach
    public void finish() {
        client.stop();
    }
}
