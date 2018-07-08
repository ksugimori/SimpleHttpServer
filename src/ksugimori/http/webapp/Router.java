package ksugimori.http.webapp;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import ksugimori.http.webapp.controller.BasicHttpController;

/**
 * リクエストパスとコントローラの紐づけ
 */
public class Router {
  /**
   * コントローラを探索するパッケージ。サブパッケージは探索しません
   */
  public static final String CONTROLLER_PACKAGE_NAME = "ksugimori.http.webapp.controller";
  
  private static Router instance;
  private Map<String, Controller> routingTable;

  public static Router getInstance() {
    if (instance == null) {
      instance = new Router();
    }
    return instance;
  }

  private Router() {
    instance = this;
    routingTable = searchRoutedHandler();
  }

  /**
   * コントローラの探索。{@link ksugimori.http.webapp.Router.CONTROLLER_PACKAGE_NAME}
   * で指定されたパッケージのクラスで、{@link ksugimori.http.webapp.RequestMapping} アノテーションが付いたクラスを探し、リクエストパスと紐付ける。
   * 
   * @return
   */
  private Map<String, Controller> searchRoutedHandler() {
    Map<String, Controller> table = new HashMap<>();

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    assert classLoader != null;
    URL resource = classLoader.getResource(CONTROLLER_PACKAGE_NAME.replace('.', '/'));

    Path packageDir;
    try {
      packageDir = Paths.get(resource.toURI());
    } catch (URISyntaxException e) {
      return Collections.emptyMap();
    }
    
    try (DirectoryStream<Path> filespaths = Files.newDirectoryStream(packageDir, "*.class")) {
      for (Path filepath : filespaths) {
        String simpleName = filepath.getFileName().toString().replaceAll("\\.class$", "");
        String className = CONTROLLER_PACKAGE_NAME + "." + simpleName;
        try {
          Class<?> clazz = Class.forName(className);
          if ( ! clazz.isAnnotationPresent(RequestMapping.class)) continue;

          String path = clazz.getDeclaredAnnotation(RequestMapping.class).value();
          Controller controller = (Controller) clazz.newInstance();
          table.put(path, controller);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
          // do nothing
        }
      }
    } catch (IOException e) {
      System.err.println("fail loading routed classes.");
    }

    return table;
  }

  /**
   * ルーティングが設定されていればそのハンドラ、なければデフォルトのハンドラを返す
   * 
   * @param path
   * @return
   */
  public Controller route(String path) {
    String normalized = Paths.get(path).normalize().toString();

    for (Map.Entry<String, Controller> entry : routingTable.entrySet()) {
      if (normalized.startsWith(entry.getKey())) {
        return entry.getValue();
      }
    }

    return new BasicHttpController();
  }
}
