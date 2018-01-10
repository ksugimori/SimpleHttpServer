package ksugimori.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import ksugimori.http.exception.InvalidMessageException;
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
      try {
        request = Parser.parseRequest(in);
      } catch (InvalidMessageException e) {
        Response response = new Response(Parser.protocolVersion, Status.BAD_REQUEST);
        Parser.writeResponse(out, response);
        return;
      }

      Method method = request.getMethod();
      RequestHandler handler = RequestHandler.of(method);
      Response response = handler.handle(request);

      Parser.writeResponse(out, response);

      accessLog(request, response);

      socket.close();
    } catch (IOException e) {
      System.out.println("IOException");
      
    }
  }

  private void accessLog(Request req, Response resp) {
    Date date = new Date();
    System.out.printf("[%s] \"%s\" %d%n", dateFormat.format(date), req.toString(),
        resp.getStatusCode());
  }
}
