import java.io.*;
import java.net.*;

public class myClient {
    public static void main(String[] args) {
        String serverHostname = "localhost";
        int serverPort = 23456;
        File send = new File("send.txt");

        try (
            Socket clientSocket = new Socket(serverHostname, serverPort);
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to server " + serverHostname + " on port " + serverPort);

            // Prompt user for username and password
            System.out.print("Enter username: ");
            String username = userInput.readLine();
            System.out.print("Enter password: ");
            String password = userInput.readLine();

            // Send username and password to the server
            out.writeUTF(username);
            out.writeUTF(password);

            // Read response from the server
            String serverResponse = in.readUTF();
            System.out.println("Server says: " + serverResponse);

            FileInputStream fin = new FileInputStream(send);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            fin.close();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverHostname);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("I/O error occurred when connecting to the server " + serverHostname);
            e.printStackTrace();
        }
    }
}
