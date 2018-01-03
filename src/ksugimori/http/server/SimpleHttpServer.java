package ksugimori.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import ksugimori.http.message.Status;

public class SimpleHttpServer {
  public static final int PORT = 8080;
  public static final Path documentRoot;
  private static final Map<String, String> mimeTypes;
  private static final Map<Status, Path> errorPages;
  private static final byte[] EMPTY_BYTE_ARRAY = {};

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
    
    errorPages = new HashMap<>();
    errorPages.put(Status.NOT_FOUND, documentRoot.resolve("error/404.html"));
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

  public static byte[] readErrorPage(Status status) {
    if (!errorPages.containsKey(status)) {
      return EMPTY_BYTE_ARRAY;
    }

    byte[] ret;
    try {
      Path path = errorPages.get(status);
      ret = Files.readAllBytes(path);
    } catch (IOException e) {
      ret = EMPTY_BYTE_ARRAY;
    }

    return ret;
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
