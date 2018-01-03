package ksugimori.http.message;

import java.util.HashMap;
import java.util.Map;

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

  Map<String, String> headers;

  String body;

  public Request(Method method, String target, String version) {
    this.method = method;
    this.target = target;
    this.version = version;
    this.headers = new HashMap<>();
    this.body = "";
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

  public void addHeaderField(String name, String value) {
    this.headers.put(name, value);
  }

  public String getHeaderField(String name) {
    return this.headers.get(name);
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getBody() {
    return this.body;
  }

  @Override
  public String toString() {
    return method.toString() + " " + target + " " + version;
  }

}
