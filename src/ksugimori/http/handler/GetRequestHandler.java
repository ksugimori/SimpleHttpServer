package ksugimori.http.handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import ksugimori.http.message.Request;
import ksugimori.http.message.Response;
import ksugimori.http.message.Status;
import ksugimori.http.server.SimpleHttpServer;

/**
 * GET リクエストのハンドラ
 */
public class GetRequestHandler implements RequestHandler {
  public static String protocolVersion = "HTTP/1.1";

  @Override
  public Response handle(Request request) {
    Path target = SimpleHttpServer.documentRoot.resolve(request.getTarget().replaceAll("^/", ""));
    if (Files.isDirectory(target)) {
      target = target.resolve("index.html");
    }

    Response response;
    try {
      response = new Response(protocolVersion, Status.OK);
      response.setBody(Files.readAllBytes(target));
      
      String fileName = target.getFileName().toString();
      String contentType = SimpleHttpServer.fileNameToContentType(fileName);
      
      response.addHeaderField("Content-Type", contentType);
    } catch (IOException e) {
      response = new Response(protocolVersion, Status.NOT_FOUND);
      response.setBody(SimpleHttpServer.readErrorPage(Status.NOT_FOUND));
    }

    return response;
  }

}
