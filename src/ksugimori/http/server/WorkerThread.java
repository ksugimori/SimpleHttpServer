package ksugimori.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import ksugimori.http.exception.InvalidMessageException;
import ksugimori.http.message.Method;
import ksugimori.http.message.Parser;
import ksugimori.http.message.Request;
import ksugimori.http.message.Response;

public class WorkerThread extends Thread {
  private Socket socket;
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S");

  public WorkerThread(Socket socket) {
    super();
    this.socket = socket;
  }

  @Override
  public void run() {
    try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
      Request request = Parser.parseRequest(in);

      Method method = request.getMethod();
      RequestHandler handler = RequestHandler.of(method);
      Response response = handler.handle(request);

      Parser.writeResponse(out, response);

      log(request, response);

      socket.close();
    } catch (InvalidMessageException e) {
      System.out.println("invalid message");
      return;
    } catch (IOException e) {
      System.out.println("IOException");
      
    }
  }

  private void log(Request req, Response resp) {
    Date date = new Date();
    System.out.printf("[%s] \"%s\" %d%n", dateFormat.format(date), req.toString(),
        resp.getStatusCode());
  }
}
