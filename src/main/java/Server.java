import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("stop")) {
                    System.out.println("Server is down");
                    break;
                }
                if(inputLine.startsWith("#changeKey ")) {
                    Encoder.changeKey(inputLine.substring(11));
                    out.println("Encoder updated");
                    continue;
                }
                if (inputLine.startsWith("#whitespaces")) {
                    Encoder.setPositions(inputLine.substring(12));
                    out.println("Encoder updated");
                    continue;
                }
                if (inputLine.startsWith("#garbage")) {
                    Encoder.setGarbageLength(Integer.parseInt(inputLine.substring(8)));
                    out.println("Encoder updated");
                    continue;
                }
                System.out.println("Received encoded message from client: " + inputLine);
                String decoded = Encoder.decode(inputLine);
                Encoder.clearGarbage();
                System.out.println("Decoded message from client: " + decoded);
                out.println(decoded);
            }
            stop();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(8080);
    }
}