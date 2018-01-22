package ksugimori.http.message;

/**
 * HTTP Request
 * 
 * @author ksugimori
 *
 */
public class Request {
  Method method;
  String target;
  String version;

  public Request(Method method, String target, String version) {
    this.method = method;
    this.target = target;
    this.version = version;
  }

  public Method getMethod() {
    return method;
  }

  public String getTarget() {
    return target;
  }

  public String getVersion() {
    return version;
  }

  public String getRequestLine() {
    return method.toString() + " " + target + " " + version;
  }
  
  @Override
  public String toString() {
    return getRequestLine();
  }

}
