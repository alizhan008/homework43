import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerHttp {
    public static HttpServer makeServer() throws IOException{
        String host = "localhost";
        InetSocketAddress address = new InetSocketAddress(host,9889);
        String msg = "��������� ������ �� ������" + " http://%s:%s%n";
        System.out.printf(msg,address.getHostName(),address.getPort());
        HttpServer server = HttpServer.create(address,50);
        System.out.println("������!");
        return server;
    }
}
