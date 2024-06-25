import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
  private static final List<ClientHandler> clients = new ArrayList<>();

  public static void main(String[] args) throws IOException {
      try (ServerSocket serverSocket = new ServerSocket(5000)) {
        System.out.println("Server started. Waiting for clients...");

          while (true) {
              Socket clientSocket = serverSocket.accept();
              System.out.println("Client connected: " + clientSocket);

              ClientHandler clientThread = new ClientHandler(clientSocket, clients);
              clients.add(clientThread);
              new Thread(clientThread).start();
          }
    }
  }
}

class ClientHandler implements Runnable {
  private final Socket clientSocket;
  private final List<ClientHandler> clients;
  private final PrintWriter out;
  private final BufferedReader in;

  public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
      this.clientSocket = socket;
      this.clients = clients;
      this.out = new PrintWriter(clientSocket.getOutputStream(), true);
      this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  }

  @Override
  public void run() {
      try {
          String inputLine;
          while ((inputLine = in.readLine()) != null) {
              for (ClientHandler aClient : clients) {
                  aClient.out.println(inputLine);
              }
          }
      } catch (IOException e) {
          System.out.println("An error occurred: " + e.getMessage());
      } finally {
          try {
              in.close();
              out.close();
              clientSocket.close();
          } catch (IOException e) {
          }
      }
  }
}