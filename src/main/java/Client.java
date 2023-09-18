import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(String ip, int port) {
        try{
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public String sendMessage(String message) {
        try {
            out.println(message);
            return in.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return "";
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start("127.0.0.1",8080);

        Scanner sc = new Scanner(System.in);
        System.out.println("Input message:");

        String inputLine;
        while ((inputLine = sc.nextLine()) != null) {
            if(inputLine.equals("stop")) {
                client.sendMessage(inputLine);
                break;
            }
            if(inputLine.startsWith("#changeKey ")) {
                Encoder.changeKey(inputLine.substring(11));
                client.sendMessage(inputLine);
                continue;
            }
            String encoded = Encoder.encode(inputLine);

            List<Integer> whitespaces = Encoder.getWhitespaces();
            if(!whitespaces.isEmpty()) client.sendMessage("#whitespaces" + Encoder.sendPositions(whitespaces));
            if(Encoder.getGarbageLength() != 0) {
                client.sendMessage("#garbage" + Encoder.getGarbageLength());
                Encoder.clearGarbage();
            }

            String response = client.sendMessage(encoded);
            System.out.println("Response from server: " + response);
        }
        client.stop();
    }
}
