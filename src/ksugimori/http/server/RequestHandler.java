package ksugimori.http.server;


import ksugimori.http.message.Method;
import ksugimori.http.message.Request;
import ksugimori.http.message.Response;

public interface RequestHandler {
  public Response handle(Request request);

  public static RequestHandler of(Method method) {
    RequestHandler handler;
    switch (method) {
      case GET:
        handler = new GetRequestHandler();
      case POST:
        handler = new PostRequestHandler();
      default:
        // TODO error case
        handler = new GetRequestHandler();
    }
    
    return handler;
  }
}
