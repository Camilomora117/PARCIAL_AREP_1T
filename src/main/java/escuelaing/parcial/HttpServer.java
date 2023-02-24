package main.java.escuelaing.parcial;

import main.java.escuelaing.parcial.reflection.ServiceChatGPT;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HttpServer {

    private Integer port = Integer.valueOf(System.getenv("PORT"));

    private static ServiceChatGPT serviceChatGPT = new ServiceChatGPT();

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 36000.");
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine, request="/simple", typeRequest="GET";
            boolean firstLine = true;

            while ((inputLine = in.readLine()) != null) {
                //System.out.println("Recibí: " + inputLine);
                if (firstLine) {
                    request = inputLine.split(" ")[1];
                    typeRequest = inputLine.split(" ")[0];
                    firstLine = false;
                }
                if (!in.ready()) {break; }
            }
            System.out.println("Request " + request);
            if (request.startsWith("/hello?name=")) {
                String service = request.split("name=")[1].replace(" ", "");
                System.out.println(service);
                if (service.startsWith("Class")) {
                    String command = request.split("\\(")[1].split("\\)")[0];
                    System.out.println("Command = " + command);
                    ArrayList<String> response = serviceChatGPT.getClass(command);
                    outputLine = getHtmlSimpleForm(getList(response));

                } else if (service.startsWith("unaryInvoke")) {
                    String command = request.split("\\(")[1].split("\\)")[0].replace(" ", "");
                    String className = command.split(",")[0];
                    String method = command.split(",")[1];
                    String type = command.split(",")[2];
                    String param = command.split(",")[3].replace("\"", "");
                    System.out.println(className + " metodo " + method + " type " + type + " param " + param);
                    ArrayList<String> response = serviceChatGPT.getInvoke(className, method, type, param);
                    outputLine = getHtmlSimpleForm(getList(response));
                } else if (service.startsWith("binaryInvoke")) {
                    String command = request.split("\\(")[1].split("\\)")[0].replace(" ", "");
                    String className = command.split(",")[0];
                    String method = command.split(",")[1];
                    String type = command.split(",")[2];
                    String param = command.split(",")[3].replace("\"", "");
                    System.out.println(className + " metodo " + method + " type " + type + " param " + param);
                    ArrayList<String> response = serviceChatGPT.getInvoke(className, method, type, param);
                    outputLine = getHtmlSimpleForm(getList(response));
                } else {
                    outputLine = getHeader() + getPageHtml();
                }
            } else {
                outputLine = getHeader() + getPageHtml();
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }

        serverSocket.close();
    }

    public static String getHeader()
    {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
    }

    public static String getPageHtml() {
        byte[] file;
        try {
            file = Files.readAllBytes(Paths.get("src/main/resources/page.html"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(file);
    }

    public static String getList(ArrayList<String> values) {
        String response = "";
        for(String v: values) {
            String etiqueta = "<h2> " + v + "</h2>";
            response += etiqueta;
            response += "\r\n";
        }
        return response;
    }

    public static String getHtmlSimpleForm(String text) {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Parcial</title>\n"
                + "</head>\n"
                + "<body>\n"
                +  text
                + "</body>\n"
                + "</html>\n";
    }


}