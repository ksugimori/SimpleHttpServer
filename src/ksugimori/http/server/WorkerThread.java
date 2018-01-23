package ksugimori.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import ksugimori.http.exception.ParseException;
import ksugimori.http.exception.UnsupportedMethodException;
import ksugimori.http.handler.RequestHandler;
import ksugimori.http.message.Method;
import ksugimori.http.message.Parser;
import ksugimori.http.message.Request;
import ksugimori.http.message.Response;
import ksugimori.http.message.Status;

public class WorkerThread extends Thread {
  private Socket socket;
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

  public WorkerThread(Socket socket) {
    super();
    this.socket = socket;
  }

  @Override
  public void run() {
    try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
      Request request;
      Response response;

      try {
        request = Parser.parseRequest(in);
        Method method = request.getMethod();
        RequestHandler handler = RequestHandler.of( method );

        response = handler.handle(request);

        accessLog(request.getRequestLine(), response.getStatusCode());
      } catch (ParseException | UnsupportedMethodException e) {
        response = new Response(Parser.PROTOCOL_VERSION, Status.BAD_REQUEST);
        response.setBody(SimpleHttpServer.readErrorPage(Status.BAD_REQUEST));

        errorLog(e.getMessage());
      }

      Parser.writeResponse(out, response);

      socket.close();
    } catch (IOException e) {
      errorLog("socket closed by client?");
    }
  }

  /**
   * アクセスログを出力します
   * @param requestLine
   * @param responseCode
   */
  private void accessLog(String requestLine, int responseCode) {
    Date date = new Date();
    System.out.printf("[%s] \"%s\" %d%n", dateFormat.format(date), requestLine, responseCode);
  }

  /**
   * エラーログを出力します
   * @param message
   */
  private void errorLog(String message) {
    Date date = new Date();
    System.out.printf("[%s] [ERROR] %s%n", dateFormat.format(date), message);
  }
}
