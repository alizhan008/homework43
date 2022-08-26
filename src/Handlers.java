import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class Handlers {
    public static void initRoutes(HttpServer server){
        server.createContext("/",Handlers::handleRootRequest);
        server.createContext("/apps",Handlers::handleApps);
        server.createContext("/apps/profile",Handlers::handleProfile);
    }

    private static void handleRootRequest(HttpExchange exchange){
        try {
            exchange.getResponseHeaders().add("Content-Type","text/plain; charset=utf-8");

            int response = 200;
            int length = 0;
            exchange.sendResponseHeaders(response,length);

            try(PrintWriter writer = getWriterFrom(exchange)){
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String cxtPath = exchange.getHttpContext().getPath();

                write(writer,"HTTP Метод",method);
                write(writer,"Запрос",uri.toString());
                write(writer,"Обработан через корневой",cxtPath);
                writeHeaders(writer,"Заголовки запроса",exchange.getResponseHeaders());
                writeData(writer,exchange);
                writer.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void handleApps(HttpExchange exchange){
        try {
            exchange.getResponseHeaders().add("Content-Type","text/plain; charset=utf-8");
            exchange.getResponseHeaders().add("Server","HttpServer from JAVA");
            String date = String.valueOf(LocalDate.now());
            exchange.getResponseHeaders().add("Expires",date);
            int response = 201;
            int length = 0;
            exchange.sendResponseHeaders(response,length);

            try(PrintWriter writer = getWriterFrom(exchange)){
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String cxtPath = exchange.getHttpContext().getPath();
                String protocol = exchange.getProtocol();
                String localHost = String.valueOf(exchange.getLocalAddress());

                write(writer,"HTTP Метод",method);
                write(writer,"Запрос приложения",uri.toString());
                write(writer,"Обработан через",cxtPath);
                write(writer,"Использует протокол",protocol);
                write(writer,"Локальный хост",localHost);
                writeHeaders(writer,"Заголовки запроса",exchange.getResponseHeaders());
                writeData(writer,exchange);
                writer.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void handleProfile(HttpExchange exchange){
        try {
            exchange.getResponseHeaders().add("Content-Type","text/plain; charset=utf-8");
            exchange.getResponseHeaders().add("Server","HttpServer from JAVA");
            String date = String.valueOf(LocalDate.now());
            exchange.getResponseHeaders().add("Expires",date);

            int response = 202;
            int length = 0;
            exchange.sendResponseHeaders(response,length);

            try(PrintWriter writer = getWriterFrom(exchange)){
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String cxtPath = exchange.getHttpContext().getPath();
                String protocol = exchange.getProtocol();
                String localHost = String.valueOf(exchange.getLocalAddress());
                String remoteHost = String.valueOf(exchange.getRemoteAddress());

                write(writer,"HTTP Метод",method);
                write(writer,"Запрос профиля",uri.toString());
                write(writer,"Обработан через",cxtPath);
                write(writer,"Использует протокол",protocol);
                write(writer,"Локальный хост",localHost);
                write(writer,"Удаленный хост",remoteHost);

                writeHeaders(writer,"Заголовки запроса",exchange.getResponseHeaders());
                writeData(writer,exchange);
                writer.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void writeHeaders(Writer writer, String type, Headers headers) {
        write(writer,type,"");
        headers.forEach((k,v) -> write(writer,"\t" + k, v.toString()));
    }

    private static void write(Writer writer, String msg, String method) {
        String data = String.format("%s: %s%n%n",msg,method);

        try {
            writer.write(data);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static PrintWriter getWriterFrom(HttpExchange exchange) {
        OutputStream output = exchange.getResponseBody();
        Charset charset = StandardCharsets.UTF_8;
        return new PrintWriter(output,false,charset);
    }

    private static BufferedReader getReader(HttpExchange exchange){
        InputStream input = exchange.getRequestBody();
        Charset charset = StandardCharsets.UTF_8;
        InputStreamReader isr = new InputStreamReader(input,charset);
        return new BufferedReader(isr);
    }

    private static void writeData(Writer writer,HttpExchange exchange){
        try(BufferedReader reader = getReader(exchange)){
            if (!reader.ready()) return;

            write(writer,"Блок данных","");
            reader.lines().forEach(e -> write(writer,"\t",e));
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
