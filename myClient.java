import java.io.*;
import java.net.*;

public class myClient {
    public static void main(String[] args) {
        String serverHostname = "localhost";
        int serverPort = 23456;

        try (
            Socket clientSocket = new Socket(serverHostname, serverPort);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to server " + serverHostname + " on port " + serverPort);

            // Prompt user for username and password
            System.out.print("Enter username: ");
            String username = userInput.readLine();
            System.out.print("Enter password: ");
            String password = userInput.readLine();

            // Send username and password to the server
            out.println(username);
            out.println(password);

            // Read response from the server
            String serverResponse = in.readLine();
            System.out.println("Server says: " + serverResponse);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverHostname);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("I/O error occurred when connecting to the server " + serverHostname);
            e.printStackTrace();
        }
    }
}