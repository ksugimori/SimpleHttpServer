package ksugimori.http.handler;

import java.nio.charset.StandardCharsets;
import ksugimori.http.message.Request;
import ksugimori.http.message.Response;
import ksugimori.http.message.Status;

/**
 * POST リクエストのハンドラ
 */
public class PostRequestHandler implements RequestHandler {
  public static String protocolVersion = "HTTP/1.1";

  @Override
  public Response handle(Request request) {
    // nothing to do
    System.out.println("POST body: " + new String(request.getBody(), StandardCharsets.UTF_8));
    Response response = new Response(protocolVersion, Status.NO_CONTENT);
    return response;
  }

}
