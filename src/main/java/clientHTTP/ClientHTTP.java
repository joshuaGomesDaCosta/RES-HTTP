package clientHTTP;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHTTP {

    static final Logger LOG = Logger.getLogger(ClientHTTP.class.getName());

    final static int BUFFER_SIZE = 1024;

    private Socket clientSocket;
    private OutputStream os;
    private InputStream is;
    private final String host;
    private final int port;

    public ClientHTTP(String host, int port){
        this.host = host;
        this.port = port;

        try {

            clientSocket = new Socket( this.host, this.port);
            os = clientSocket.getOutputStream();
            is = clientSocket.getInputStream();

            ByteArrayOutputStream responseBuffer = serverResponse();
            LOG.log(Level.INFO, "Response sent by the server: ");
            LOG.log(Level.INFO, responseBuffer.toString());

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "erreur de communication", ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHTTP.class.getName()).log(Level.SEVERE, "erreur fermeture d'inputStream", ex);
            }
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHTTP.class.getName()).log(Level.SEVERE, "erreur fermeture d'outputStream", ex);
            }
            try {
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHTTP.class.getName()).log(Level.SEVERE, "erreur fermeture du socket", ex);
            }
        }
    }

    private ByteArrayOutputStream serverResponse() throws IOException{
        ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int newBytes;
        while ((newBytes = is.read(buffer)) != -1) {
            responseBuffer.write(buffer, 0, newBytes);
        }
        return responseBuffer;
    }

    private void writeHeader(String type)  throws Exception {
        os.write((type + " / HTTP/1.1\n\r").getBytes());
        os.write(("Host: " + host + "\n\r").getBytes());
        //os.write("User-Agent: ... \n\r".getBytes());
        os.write("Accept: text/html \n\r".getBytes());
        os.write("Accept-Language: fr-CH \n\r".getBytes());
        os.write("Accept-Encoding: encoding_method;q=value \n\r".getBytes());
        //os.write("Cookie: ... \n\r".getBytes());
        os.write("Connection: keep-alive \n\r".getBytes());
        os.write("\n\r".getBytes());
    }

    public void get() throws Exception {
        writeHeader("GET");

        ByteArrayOutputStream responseBuffer = serverResponse();
        LOG.log(Level.INFO, "Response sent by the server: ");
        LOG.log(Level.INFO, responseBuffer.toString());
    }

    public ByteArrayOutputStream post(String data) throws Exception {
        writeHeader("POST");

        os.write("Data: ... \n\r".getBytes());

        ByteArrayOutputStream responseBuffer = serverResponse();
        LOG.log(Level.INFO, "Response sent by the server: ");
        LOG.log(Level.INFO, responseBuffer.toString());

        return responseBuffer;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ClientHTTP clientHTTP = new ClientHTTP("www.test.com", 80);
        while(true){
            try{
                clientHTTP.post("requete test");
                System.out.println("pas d'esception");
            }
            catch( Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}
