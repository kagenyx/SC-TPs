import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class myServer{

    private File recieve = new File("recieve.txt");
    private File usersfile = new File("users.txt");

    public static void main(String[] args) {
        System.out.println("servidor: main");
        myServer server = new myServer();
        server.startServer();
    }

    public void startServer (){
        ServerSocket sSoc = null;
        
        try {
            sSoc = new ServerSocket(23456);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
         
        while(true) {
            try {
                Socket inSoc = sSoc.accept();
                ServerThread newServerThread = new ServerThread(inSoc);
                newServerThread.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        //sSoc.close();
    }

    //Threads utilizadas para comunicacao com os clientes
    class ServerThread extends Thread {

        private Socket socket = null;

        ServerThread(Socket inSoc) {
            socket = inSoc;
            System.out.println("thread do server para cada cliente");
        }
 
        public void run(){
            try {
                DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream inStream = new DataInputStream(socket.getInputStream());

                String user = null;
                String passwd = null;
                
                try {
                    StringBuilder sb = new StringBuilder();
                    user = inStream.readUTF();
                    sb.append(user + ":");
                    passwd = inStream.readUTF();
                    sb.append(passwd);
                    System.out.println("thread: depois de receber a password e o user");
                    if(!checkUsers(user, passwd)) {
                        System.out.println("User inválido!");
                        return; 
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                
                //TODO: refazer
                //este codigo apenas exemplifica a comunicacao entre o cliente e o servidor
                //nao faz qualquer tipo de autenticacao
                System.out.println("user : " + user + "\npwd : " + passwd);
                if (user.length() != 0){
                    String caralhete = "caralhete, deu";
                    outStream.writeUTF(caralhete);

                    FileOutputStream fos = new FileOutputStream(recieve);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                    fos.close();
                }
                else {

                }

                outStream.close();
                inStream.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkUsers(String user, String pwd) {
        Scanner sc = null;
        try {
            sc = new Scanner(usersfile);
            while(sc.hasNextLine()) {
                String[] user_check = sc.nextLine().split(":");
                if(user.equals(user_check[0])) {
                    if(!pwd.equals(user_check[1])) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
            writeUser(user,pwd);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }

    private void writeUser(String user, String pwd) {
        StringBuilder sb = new StringBuilder();
        sb.append(user + ":" + pwd);
        try {
            Files.write(Paths.get("users.txt"), sb.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
