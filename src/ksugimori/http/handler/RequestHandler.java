package ksugimori.http.handler;


import ksugimori.http.exception.UnsupportedMethodException;
import ksugimori.http.message.Method;
import ksugimori.http.message.Request;
import ksugimori.http.message.Response;

public interface RequestHandler {
  public Response handle(Request request);

  public static RequestHandler of(Method method) throws UnsupportedMethodException {
    RequestHandler handler;
    switch (method) {
      case GET:
        handler = new GetRequestHandler();
        break;
      case POST:
        handler = new PostRequestHandler();
        break;
      default:
        throw new UnsupportedMethodException(method.toString());
    }
    
    return handler;
  }
}
