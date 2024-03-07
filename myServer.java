import java.io.*;
import java.net.*;

public class myServer{

    private String adm_user = "user";
    private String adm_password = "123";

    private File recieve = new File("recieve.txt");

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
                    user = inStream.readUTF();
                    passwd = inStream.readUTF();
                    System.out.println("thread: depois de receber a password e o user");
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
}
