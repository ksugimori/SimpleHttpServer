package ksugimori.http.message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP Request
 * 
 * @author ksugimori
 *
 */
public class Response {
  String version;
  Status status;

  Map<String, String> headers;

  byte[] body;

  public Response(String version, Status status) {
    this.version = version;
    this.status = status;
    this.headers = new HashMap<>();
    this.body = new byte[] {};
  }

  public String getVersion() {
    return version;
  }

  public void addHeaderField(String name, String value) {
    this.headers.put(name, value);
  }

  public void setBody(byte[] body) {
    this.body = body;
  }

  public int getStatusCode() {
    return status.getCode();
  }

  public String getReasonPhrase() {
    return status.getReasonPhrase();
  }

  public Map<String, String> getHeaders() {
    return headers;
  }
  
  public byte[] getBody() {
    return body;
  }

  @Override
  public String toString() {
    return "Response [version=" + version + ", status=" + status + ", headers=" + headers
        + ", body=" + Arrays.toString(body) + "]";
  }
}
