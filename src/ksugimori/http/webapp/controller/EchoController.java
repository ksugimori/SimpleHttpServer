package ksugimori.http.webapp.controller;

import ksugimori.http.message.Request;
import ksugimori.http.message.Response;
import ksugimori.http.message.Status;
import ksugimori.http.webapp.Controller;
import ksugimori.http.webapp.RequestMapping;

/**
 * リクエストボディをそのまま返すコントローラ
 */
@RequestMapping("/webapp/echo")
public class EchoController extends Controller {
  public static String protocolVersion = "HTTP/1.1";

  @Override
  public Response doGet(Request request) {
    return echo(request);
  }

  @Override
  public Response doPost(Request request) {
    return echo(request);
  }
  
  private Response echo(Request request) {
    Response response = new Response(protocolVersion, Status.OK);
    response.setBody(request.getBody());
    return response;
  }
}
