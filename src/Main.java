import com.sun.net.httpserver.HttpServer;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try{
            HttpServer server = ServerHttp.makeServer();
            server.start();
            Handlers.initRoutes(server);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}