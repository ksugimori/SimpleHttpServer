package ksugimori.http.message;

/**
 * HTTP Request
 * 
 * @author ksugimori
 *
 */
public class Request extends AbstractHttpMessage {
  Method method;
  String target;
  String version;

  public Request(Method method, String target, String version) {
    super();
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

  @Override
  public String getStartLine() {
    return method.toString() + " " + target + " " + version;
  }
}
