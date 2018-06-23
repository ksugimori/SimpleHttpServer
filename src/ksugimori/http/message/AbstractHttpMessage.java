package ksugimori.http.message;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHttpMessage {
  protected Map<String, String> headers;
  protected byte[] body;

  public AbstractHttpMessage() {
    this.headers = new HashMap<>();
    this.body = new byte[0];
  }
  
  public void addHeaderField(String name, String value) {
    this.headers.put(name, value);
  }

  public Map<String, String> getHeaders() {
    return headers;
  }
  
  public void setBody(byte[] body) {
    this.body = body;
  }
  
  public byte[] getBody() {
    return body;
  }
  
  protected abstract String getFirstLine();
  
  @Override
  public String toString() {
    return getFirstLine() + " headers: " + headers + " body: " + new String(body, StandardCharsets.UTF_8);
  }
}
