package ksugimori.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SimpleHttpServer {
  public static final int PORT = 8080;
  public static final Path documentRoot;
  private static final Map<String, String> mimeTypes;

  static {
    documentRoot = Paths.get(System.getProperty("user.dir"), "files", "www");

    mimeTypes = new HashMap<>();
    mimeTypes.put(".html", "text/html");
    mimeTypes.put(".htm", "text/html");
    mimeTypes.put(".css", "text/css");
    mimeTypes.put(".js", "application/js");
    mimeTypes.put(".json", "application/json");
    mimeTypes.put(".png", "image/png");
    mimeTypes.put(".jpeg", "image/jpeg");
    mimeTypes.put(".jpg", "image/jpeg");
    mimeTypes.put(".gif", "image/gif");
  }

  /**
   * ファイルの拡張子に対応した Content-Type を返します
   * @param fileName
   * @return
   */
  public static String fileNameToContentType(String fileName) {
    for (Map.Entry<String, String> e : mimeTypes.entrySet()) {
      if (fileName.endsWith(e.getKey()))
        return e.getValue();
    }

    return "";
  }

  public static void main(String[] args) {
    try (ServerSocket server = new ServerSocket(PORT)) {
      System.out.println("SERVER START: ");
      System.out.println("LISTENING ON: " + server.getLocalSocketAddress());

      while (true) {
        Socket socket = server.accept();

        WorkerThread worker = new WorkerThread(socket);
        worker.start();
      }

    } catch (IOException e) {
      System.out.println("CANNOT START SERVER");
    }

  }
}
