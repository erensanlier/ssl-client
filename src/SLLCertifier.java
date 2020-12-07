import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class SLLCertifier {

    String address;
    Integer port = 0;
    Socket socket;
    DataInputStream inputStream;
    PrintWriter outputStream;
    BufferedReader bufferedReader;
    boolean connected = false;
    boolean authenticated = false;


    public SLLCertifier(String address, Integer port){
        this.address = address;
        this.port = port;
    }

    public boolean getCertificate() {
        if (socket == null) {
            return false;
        }
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println(bufferedReader.readLine());
            while (!authenticated) {
                System.out.println("Username: ");
                String username = scanner.nextLine();
                System.out.println("Password: ");
                String password = scanner.nextLine();
                outputStream.println(username + "," + password);
                outputStream.flush();
                String response = bufferedReader.readLine();
                if(response.equals("Success")){
                    authenticated = true;
                    System.out.println("Authentication successful! Starting to receive certificate...");
                }else{
                    System.err.println("Authentication not successful! Please try again.");
                }
            }
            byte[] buffer = new byte[4096];
            int ct;
            try {
                FileOutputStream fileOutputStream = new FileOutputStream("server_crt.crt");
                while ((ct = this.inputStream.read(buffer)) > 0) {
                    if (buffer[ct - 1] == 26) { // end of file
                        fileOutputStream.write(buffer, 0, ct - 1);
                        break;
                    }
                    fileOutputStream.write(buffer, 0, ct);
                }
                System.out.println("Successfully received the certificate!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } catch (Exception e) {
            System.err.println("SLL receiving unsuccessful!");
            return false;
        }
    }

    public void connect(){
        if(this.connected){
            System.err.println("Already connected to server. Please disconnect first!");
            return;
        }
        assert address != null : this.port != null;
        try {
            this.socket = new Socket(this.address, this.port);
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.outputStream = new PrintWriter(socket.getOutputStream());
            this.connected = true;
        }catch(Exception e){
            System.err.println("Couldn't establish connection with SSL provider at " + this.address + "/" + this.port);
        }
    }

    public void disconnect() {
        if(!this.connected){
            System.err.println("Already disconnected. Please connect first!");
            return;
        }
        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
            if (bufferedReader != null) {
                bufferedReader.close();
                bufferedReader = null;

            }
            if (outputStream != null) {
                socket.getOutputStream().close();
                outputStream = null;
            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
            System.out.println("SSL Connection Closed");
        } catch (IOException e) {
            System.err.println("SSL Disconnect Error");
        }
    }

}
