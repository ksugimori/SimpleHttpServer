package ksugimori.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ksugimori.http.message.Status;

public class SimpleHttpServer {
  public static final int PORT = 8080;
  private static final String documentRoot;
  private static final Map<String, String> mimeTypes;
  private static final Map<Status, Path> errorPages;
  private static final byte[] EMPTY_BYTE_ARRAY = {};

  static {
    documentRoot = Paths.get(System.getProperty("user.dir"), "files", "www").toString();

    mimeTypes = new HashMap<>();
    mimeTypes.put("html", "text/html");
    mimeTypes.put("css", "text/css");
    mimeTypes.put("js", "application/js");
    mimeTypes.put("png", "image/png");

    errorPages = new HashMap<>();
    errorPages.put(Status.BAD_REQUEST, Paths.get(documentRoot, "error/400.html"));
    errorPages.put(Status.NOT_FOUND, Paths.get(documentRoot, "error/404.html"));
  }

  public static String getDocumentRoot() {
    return documentRoot;
  }

  /**
   * 拡張子に対応した Content-Type を返します
   * 
   * @param ext 拡張子
   * @return Content-Type
   */
  public static String extensionToContentType(String ext) {
    return mimeTypes.getOrDefault(ext, "");
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

      ExecutorService executor = Executors.newCachedThreadPool();

      while (true) {
        Socket socket = server.accept();
        executor.submit(new WorkerThread(socket));
      }

    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  //
}
